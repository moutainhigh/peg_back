/*
 *    Copyright (c) 2018-2025, kbopark All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: kbopark
 */

package com.kbopark.operation.api.memberapi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.dto.*;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.CouponReceive;
import com.kbopark.operation.entity.MemberInfo;
import com.kbopark.operation.entity.MerchantMember;
import com.kbopark.operation.enums.OrderStatusEnum;
import com.kbopark.operation.service.*;
import com.kbopark.operation.vo.ConsumerOrderVo;
import com.kbopark.operation.vo.MerchantCouponVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 会员信息表
 *
 * @author laomst
 * @date 2020-08-31 14:50:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/member-api")
@Api(value = "member-api", tags = "【用户端相关接口】")
public class MemberApi {

	private final MemberInfoService memberInfoService;

	private final MerchantMemberService merchantMemberService;

	private final CouponReceiveService couponReceiveService;

	private final ConsumerOrderService consumerOrderService;

	private final CouponInfoService couponInfoService;

	@ApiOperation("获取会员详细信息")
	@GetMapping("/info")
	public R<MemberInfoDTO> getMemberInfo(@ApiParam(value = "优惠券状态，can-use(可用),disabled(所有不可用的),overdue(过期),locked(禁用的、失效的),used(已使用)", allowableValues = "can-use,disabled,overdue,locked,used")
										  @RequestParam(value = "status", defaultValue = "can-use") String status) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		MemberInfo memberInfo = memberInfoService.getOne(w -> w.lambda()
				.select(MemberInfo::getId,
						MemberInfo::getAvatar, // 头像
						MemberInfo::getNickName, // 昵称
						MemberInfo::getRealName, // 姓名
						MemberInfo::getSex,  // 性别
						MemberInfo::getPhone,  // 手机号
						MemberInfo::getEmail) //邮箱
				.eq(MemberInfo::getId, currentMember.getId()));
		MemberInfoDTO dto = new MemberInfoDTO();
		BeanUtil.copyProperties(memberInfo, dto);
		dto.setVipCardNum(merchantMemberService.count(w -> w.lambda().eq(MerchantMember::getMemberId, currentMember.getId())));
		//cr.coupon_end_time &gt; current_time and cr.used_status = 0 and cr.lock_flag = 0
		CouponSearchParam param = new CouponSearchParam();
		param.setMemberIdKey(currentMember.getId());
		param.setStatusKey(status);
		param.setTypeKey("coupon");
		Integer couponCount = (int)couponReceiveService.selectMemberCouponPage(new Page<>(1,1), param).getTotal();
		param.setTypeKey("redpack");
		Integer redPackCount = (int)couponReceiveService.selectMemberCouponPage(new Page<>(1,1), param).getTotal();
		dto.setCouponNum(couponCount);
		dto.setRedpackNum(redPackCount);
		return R.ok(dto);
	}

	@ApiOperation("用户会员卡分页列表")
	@GetMapping("/vip-card-page")
	public R<IPage<VipCardDTO>> getMerchantMemberPage(@RequestParam(value = "current", defaultValue = "1") Integer current,
													  @RequestParam(value = "size", defaultValue = "10") Integer size,
													  @RequestParam(value = "merchantId", required = false) Integer merchantId) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		VipCardDTO param = new VipCardDTO();
		param.setMemberId(currentMember.getId());
		param.setMerchantId(merchantId);
		return R.ok(merchantMemberService.selectVipcardPage(new Page<>(current, size), param));
	}

	@ApiOperation(value = "支付订单统计查询")
	@GetMapping("/payorder-count")
	public R getConsumerOrderCount() {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		QueryWrapper<ConsumerOrder> query = Wrappers.query();
		query.select("notify_status", "COUNT(1) AS num").lambda()
				.eq(ConsumerOrder::getMemberPhone, currentMember.getPhone())
				.groupBy(ConsumerOrder::getNotifyStatus);
		List<ConsumerOrder> list = consumerOrderService.list(query);
		Map<String, Integer> selectMap = new HashMap<>();
		Map<String, Integer> dataMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (ConsumerOrder order : list) {
				selectMap.put(order.getNotifyStatus(), order.getNum());
			}
		}
		int waitCount = selectMap.get(OrderStatusEnum.WAIT.getCode()) == null ? 0 : selectMap.get(OrderStatusEnum.WAIT.getCode());
		int failCount = selectMap.get(OrderStatusEnum.FAIL.getCode()) == null ? 0 : selectMap.get(OrderStatusEnum.FAIL.getCode());
		int successCount = selectMap.get(OrderStatusEnum.SUCCESS.getCode()) == null ? 0 : selectMap.get(OrderStatusEnum.SUCCESS.getCode());
		int cancelCount = selectMap.get(OrderStatusEnum.CANCEL.getCode()) == null ? 0 : selectMap.get(OrderStatusEnum.CANCEL.getCode());
		dataMap.put(OrderStatusEnum.WAIT.getCode(), waitCount);
		dataMap.put(OrderStatusEnum.FAIL.getCode(), failCount);
		dataMap.put(OrderStatusEnum.SUCCESS.getCode(), successCount);
		dataMap.put(OrderStatusEnum.CANCEL.getCode(), cancelCount);
		return R.ok(dataMap);
	}

	@ApiOperation(value = "支付订单分页查询")
	@GetMapping("/payorder-page")
	public R<IPage<ConsumerOrder>> getConsumerOrderPage(Page page, ConsumerOrder consumerOrder) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		QueryWrapper<ConsumerOrder> query = Wrappers.query();
		if (StringUtils.isNotBlank(consumerOrder.getNotifyStatus())) {
			query.lambda().eq(ConsumerOrder::getNotifyStatus, consumerOrder.getNotifyStatus());
		}
		query.select("id",
				"(select name from kboparkx_merchant where kboparkx_merchant.id = kboparkx_consumer_order.merchant_id) as merchant_name",
				"(select logo from kboparkx_merchant where kboparkx_merchant.id = kboparkx_consumer_order.merchant_id) as merchant_logo",
				"(select concat(province_name,' ',city_name, ' ',area_name) from kboparkx_merchant where kboparkx_merchant.id = kboparkx_consumer_order.merchant_id) as merchant_city",
				"(select address from kboparkx_merchant where kboparkx_merchant.id = kboparkx_consumer_order.merchant_id) as merchant_address",
				"(select name from kboparkx_coupon_info where kboparkx_coupon_info.coupon_serial_number=kboparkx_consumer_order.coupon_serial_number) as coupon_name",
				"kboparkx_consumer_order.*"
		)
				.lambda()
				.eq(ConsumerOrder::getMemberPhone, currentMember.getPhone())
				.orderByDesc(ConsumerOrder::getCreateTime);

		page = consumerOrderService.page(page, query);

		//获取全部返回数据
		List<ConsumerOrder> consumerOrderList = page.getRecords();

		//创建返回集合Vo
		List<ConsumerOrderVo> result = Lists.newArrayList();

		//遍历集合并判断设置
		consumerOrderList.forEach(x -> {result.add(new ConsumerOrderVo(x));});

		page.setRecords(result);

		return R.ok(page);
	}


//	@ApiOperation("用户优惠券分页列表")
//	@GetMapping("/coupon-page")
//	public R<IPage<MemberCouponDTO>> couponPage(@ApiParam("当前页")
//												@RequestParam(value = "current", defaultValue = "1") Integer current,
//												@ApiParam("每页条数")
//												@RequestParam(value = "size", defaultValue = "10") Integer size,
//												@ApiParam("商家id")
//												@RequestParam(value = "merchantId", required = false) Integer merchantId,
//												@ApiParam(value = "优惠券类型 1满减券", allowableValues = "1")
//												@RequestParam(value = "couponType", required = false) Integer couponType,
//												@ApiParam(value = "优惠券状态，can-use(可用),disabled(所有不可用的),overdue(过期),locked(禁用的、失效的),used(已使用)",
//														allowableValues = "can-use,disabled,overdue,locked,used")
//												@RequestParam(value = "status", defaultValue = "can-use") String status) {
//		MemberInfo currentMember;
//		try {
//			currentMember = getMember(SecurityUtils.getUser());
//		} catch (UnsupportedOperationException ex) {
//			return R.failed(ex.getMessage());
//		}
//		CouponSearchParam param = new CouponSearchParam();
//		param.setMemberIdKey(currentMember.getId());
//		param.setMerchantIdKey(merchantId);
//		param.setStatusKey(status);
//		param.setTypeKey(CouponTypeEnum.COUPON_TYPE.getCode());
//		param.setCouponTypeKey(couponType);
//		return R.ok(couponReceiveService.selectMemberCouponPage(new Page<>(current, size), param));
//	}

	@ApiOperation("商家卡券分页查询")
	@GetMapping("merchant-coupon/{type}-page")
	R<Page<MerchantCouponVO>> selectMemberMerchantCouponPage(@ApiParam("当前页")
															 @RequestParam(value = "current", defaultValue = "1") Integer current,
															 @ApiParam("每页条数")
															 @RequestParam(value = "size", defaultValue = "10") Integer size,
															 @ApiParam(value = "类型， coupon 卡券， redpack 红包, all 全部", allowableValues = "coupon,redpack,all")
															 @PathVariable(name = "type") String type,
															 @ApiParam("商家id")
															 @RequestParam(value = "merchantId", required = true) Integer merchantId) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		String realType = CollectionUtil.set(false, "redpack", "coupon").contains(type) ? type : null;
		return R.ok(couponInfoService.selectMemberMerchantCouponPage(new Page<>(current, size), realType, merchantId, currentMember.getPhone()));
	}

	@ApiOperation("红包/卡券分页查询")
	@GetMapping("/coupon/{type}-page")
	public R<IPage<MemberCouponDTO>> redpackPage(@ApiParam("当前页")
												 @RequestParam(value = "current", defaultValue = "1") Integer current,
												 @ApiParam("每页条数")
												 @RequestParam(value = "size", defaultValue = "10") Integer size,
												 @ApiParam("商家id")
												 @RequestParam(value = "merchantId", required = false) Integer merchantId,
//												 @ApiParam(value = "红包类型 1 随机红包 2普通红包", allowableValues = "1,2")
//												 @RequestParam(value = "couponType", required = false) Integer couponType,
												 @PathVariable(name = "type") String type,
												 @ApiParam(value = "优惠券状态，can-use(可用),disabled(所有不可用的),overdue(过期),locked(禁用的、失效的),used(已使用)",
														 allowableValues = "can-use,disabled,overdue,locked,used")
												 @RequestParam(value = "status", defaultValue = "can-use") String status) {
		Page<MemberCouponDTO> page = new Page<>(current, size);
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		CouponSearchParam param = new CouponSearchParam();
		param.setMemberIdKey(currentMember.getId());
		param.setMerchantIdKey(merchantId);
		param.setStatusKey(status);
		if (CollectionUtil.set(false, "redpack", "coupon").contains(type)) {
			param.setTypeKey(type);
		}
//		param.setCouponTypeKey(couponType);
		return R.ok(couponReceiveService.selectMemberCouponPage(page, param));
	}

	@ApiOperation("红包/卡券列表查询")
	@GetMapping({"/coupon/{type}-list"})
	public R<List<MemberCouponDTO>> couponAllPage(
			@ApiParam(value = "优惠券状态，can-use(可用),disabled(所有不可用的),overdue(过期),locked(禁用的、失效的),used(已使用)",
					allowableValues = "can-use,disabled,overdue,locked,used")
			@RequestParam(value = "status", defaultValue = "can-use") String status,
			@PathVariable("type") String type,
			@ApiParam("商家id") @RequestParam(value = "merchantId", required = false) Integer merchantId) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		CouponSearchParam param = new CouponSearchParam();
		if (CollectionUtil.set(false, "redpack", "coupon").contains(type)) {
			param.setTypeKey(type);
		}
		param.setMemberIdKey(currentMember.getId());
		param.setMerchantIdKey(merchantId);
		param.setStatusKey(status);
		return R.ok(couponReceiveService.selectMemberCouponList(param));
	}

	@ApiOperation("红包/卡券列表查询")
	@GetMapping({"/merchant-coupon/{type}-list"})
	public R<List<MemberCouponDTO>> merchantCouponTypeList(
			@PathVariable("type") String type,
			@ApiParam("商家id") @RequestParam(value = "merchantId", required = true) Integer merchantId) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		CouponSearchParam param = new CouponSearchParam();
		if (CollectionUtil.set(false, "redpack", "coupon").contains(type)) {
			param.setTypeKey(type);
		}
		param.setMemberIdKey(currentMember.getId());
		param.setMerchantIdKey(merchantId);
		param.setStatusKey("can-use");
		return R.ok(couponReceiveService.selectMemberCouponList(param));
	}

	@ApiOperation("立即使用卡券")
	@PutMapping({"/coupon/immediate-use/{couponSerialNumber}"})
	public R<Boolean> immediateUseCoupon(@PathVariable("couponSerialNumber") String couponSerialNumber) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		return couponReceiveService.immediateUse(couponSerialNumber, currentMember.getPhone());
	}

	@ApiOperation(value = "领取优惠券", notes = "领取优惠券, 所有参数都是必填的")
	@SysLog("领取优惠券")
	@PostMapping("/coupon/receive")
	public R<CouponReceive> receiveCoupon(@Valid @RequestBody ReceiveCouponParam param) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		return couponReceiveService.memberReceive(param, currentMember);
	}

	@ApiOperation(value = "领取优惠券", notes = "领取优惠券, 所有参数都是必填的")
	@SysLog("领取优惠券")
	@PostMapping("/coupon/batch-receive")
	public R<List<CouponReceive>> BatchReceiveCoupon(@Valid @RequestBody BatchReceiveCouponParam param) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		return couponReceiveService.memberBatchReceive(param, currentMember);
	}


	private MemberInfo getMember(KboparkUser currentUser) {
		if (null == currentUser) {
			throw new UnsupportedOperationException("获取用户信息失败");
		}
		MemberInfo member = memberInfoService.getOne(w -> {
			w.lambda().eq(MemberInfo::getPhone, currentUser.getPhone());
		});
		if (null == member) {
			throw new UnsupportedOperationException("获取用户信息失败");
		}
		return member;
	}


}
