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

package com.kbopark.operation.api.merchantapi;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.qdzhhl.kbopark.admin.api.feign.RemoteUserService;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.apidto.MerchantHomeStatistics;
import com.kbopark.operation.apidto.MerchantStatistics;
import com.kbopark.operation.apidto.ReviewLog;
import com.kbopark.operation.dto.*;
import com.kbopark.operation.entity.*;
import com.kbopark.operation.enums.CouponTypeEnum;
import com.kbopark.operation.enums.MerchantReviewStatusEnum;
import com.kbopark.operation.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 会员信息表
 *
 * @author laomst
 * @date 2020-08-31 14:50:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchant-api")
@Api(value = "merchant-api", tags = "【商家端相关接口】")
public class MerchantApi {

	private final MerchantService merchantService;

	private final RemoteUserService remoteUserService;

	private final MerchantMemberService merchantMemberService;

	private final CouponUsedLogService couponUsedLogService;

	private final CouponReceiveService couponReceiveService;

	private final ConsumerOrderService consumerOrderService;

	private final CouponInfoService couponInfoService;

	private final CouponReviewLogService couponReviewLogService;

	private final MerchantBalanceService merchantBalanceService;


	@ApiOperation("获取商家的详细信息, 供编辑使用")
	@GetMapping("/merchant-info/{id}")
	public R<Merchant> getMerchantInfo(@PathVariable Integer id) {
		Merchant entity = merchantService.getById(id);
		entity.setCanChange(true);
		if (ObjectUtil.equal(entity.getReviewStatus(), MerchantReviewStatusEnum.EDIT_DISTRIBUTOR_UN_CHECKED.code)
				|| ObjectUtil.equal(entity.getReviewStatus(), MerchantReviewStatusEnum.EDIT_OPERATOR_UN_CHECKED.code)) {
			entity = JSON.parseObject(entity.getTodoSnapshoot(), Merchant.class);
			entity.setCanChange(false);
		}
		return R.ok(entity);
	}

	@ApiOperation("获取商家的详细信息， 只供查看使用")
	@GetMapping("/merchant-view-info/{id}")
	public R<Merchant> getMerchantViewInfo(@PathVariable Integer id) {
		Merchant entity = merchantService.getById(id);
		entity.setCanChange(false);
		return R.ok(entity);
	}

	@ApiOperation("获取账号详细信息")
	@GetMapping("/account-info")
	public R getAccountInfo() {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		return remoteUserService.getById(user.getId());
	}

	@ApiOperation("商家会员卡分页列表")
	@GetMapping("/vip-card-page")
	public R<IPage<MerchantMemberPageDTO>> getMerchantMemberPage(@RequestParam(value = "current", defaultValue = "1") Integer current,
																 @RequestParam(value = "size", defaultValue = "10") Integer size) {
		MemberSearchParam param = new MemberSearchParam();
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (null == user.getMerchantId()) {
			return R.failed("获取商家信息失败");
		}
		param.setMerchantIdKey(user.getMerchantId());
		return R.ok(merchantMemberService.selectMemberPage(new Page<>(current, size), param));
	}

	@ApiOperation(value = "支付订单分页列表")
	@GetMapping("/payorder-page")
	public R<IPage<MerchantPayOrderDTO>> getConsumerOrderPage(@ApiParam("当前页")
															  @RequestParam(value = "current", defaultValue = "1") Integer current,
															  @ApiParam("每页条数")
															  @RequestParam(value = "size", defaultValue = "10") Integer size,
															  MerchantOrderSearchDTO param) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (null == user.getMerchantId()) {
			return R.failed("获取商家信息失败");
		}
		param.setMerchantId(user.getMerchantId());
		return R.ok(consumerOrderService.selectMerchantOrderPage(new Page<>(current, size), param));
	}

	@ApiOperation("优惠券领取记录")
	@GetMapping("/member-coupon-page")
	public R<IPage<MemberCouponDTO>> couponPage(@ApiParam("当前页")
												@RequestParam(value = "current", defaultValue = "1") Integer current,
												@ApiParam("每页条数")
												@RequestParam(value = "size", defaultValue = "10") Integer size,
												CouponSearchParam param) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (null == user.getMerchantId()) {
			return R.failed("获取商家信息失败");
		}
		param.setMerchantIdKey(user.getMerchantId());
		param.setTypeKey(CouponTypeEnum.COUPON_TYPE.getCode());
		return R.ok(couponReceiveService.selectMemberCouponPage(new Page<>(current, size), param));
	}

	@ApiOperation("红包领取记录")
	@GetMapping("/member-redpack-page")
	public R<IPage<MemberCouponDTO>> redpackPage(@ApiParam("当前页")
												 @RequestParam(value = "current", defaultValue = "1") Integer current,
												 @ApiParam("每页条数")
												 @RequestParam(value = "size", defaultValue = "10") Integer size,
												 CouponSearchParam param) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (null == user.getMerchantId()) {
			return R.failed("获取商家信息失败");
		}
		param.setMerchantIdKey(user.getMerchantId());
		param.setTypeKey(CouponTypeEnum.RED_PACK_TYPE.getCode());
		return R.ok(couponReceiveService.selectMemberCouponPage(new Page<>(current, size), param));
	}

	@ApiOperation("已发布的优惠券列表，供查询使用")
	@GetMapping("/public-coupon-list")
	public R<List<VantPickerModel>> publicCouponList() {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (null == user.getMerchantId()) {
			return R.failed("获取商家信息失败");
		}
		List<CouponInfo> list = couponInfoService.list(w -> w.lambda()
				.select(CouponInfo::getId, CouponInfo::getName)
				.eq(CouponInfo::getMerchantId, user.getMerchantId())
				.eq(CouponInfo::getType, CouponTypeEnum.COUPON_TYPE.getCode()));
		List<VantPickerModel> res = list.stream().map(item -> new VantPickerModel(item.getName(), item.getId())).collect(Collectors.toList());
		return R.ok(res);
	}

	@ApiOperation("优惠券/红包的审核记录列表")
	@GetMapping("/coupon-reviewlog-list")
	public R<List<ReviewLog>> couponReviewLogList(@RequestParam(value = "id", required = true) Integer couponId) {
		return R.ok(couponReviewLogService.getReviewProgress(couponId));
	}

	@ApiOperation("已发布的红包列表，供查询使用")
	@GetMapping("/public-redpack-list")
	public R<List<VantPickerModel>> publicRedpackList() {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (null == user.getMerchantId()) {
			return R.failed("获取商家信息失败");
		}
		List<CouponInfo> list = couponInfoService.list(w -> w.lambda()
				.select(CouponInfo::getId, CouponInfo::getName)
				.eq(CouponInfo::getMerchantId, user.getMerchantId())
				.eq(CouponInfo::getType, CouponTypeEnum.RED_PACK_TYPE.getCode()));
		List<VantPickerModel> res = list.stream().map(item -> new VantPickerModel(item.getName(), item.getId())).collect(Collectors.toList());
		return R.ok(res);
	}


	/**
	 * @param merchant 商家基本信息表
	 * @return R
	 */
	@ApiOperation(value = "商家自主入驻", notes = "商家自主入驻")
	@SysLog("商家自主入驻")
	@Inner(value = false)
	@PostMapping("merchant-enter-self")
	public R<Boolean> save(@RequestBody Merchant merchant) {
		return R.ok(merchantService.merchantEnterSelf(merchant));
	}

	@ApiOperation(value = "修改商家信息", notes = "修改商家基本信息表")
	@SysLog("修改商家基本信息表")
	@PutMapping("merchant-update")
	public R<Boolean> update(@RequestBody Merchant merchant) {
		return R.ok(merchantService.updateMerchant(merchant, SecurityUtils.getUser()));
	}

	@GetMapping("home-statistics")
	@ApiOperation("商家端首页统计接口")
	public R<MerchantHomeStatistics> homeStatistics() {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (null == user.getMerchantId()) {
			return R.failed("获取商家信息失败");
		}
		MerchantHomeStatistics resData = new MerchantHomeStatistics();

		// 资金信息
		MerchantBalance merchantBalance = merchantBalanceService.getOfMerchant(user.getMerchantId()).orElse(MerchantBalance.init4Merchant(-1));
		resData.setFreezeBalance(merchantBalance.getFreezeBalance().doubleValue());
		resData.setToAccountBalance(merchantBalance.getToAccountBalance().doubleValue());
		resData.setUsableBalance(merchantBalance.getUsableBalance().doubleValue());

		// 账单信息
		resData.setAllIncome(consumerOrderService.getMerchantIncomeMoney(user.getMerchantId(), null, null));
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
		// 近7天
		LocalDate sevenDaysAgo = today.minusDays(7);
		resData.appendIncomeInfo("近7天收益金额", consumerOrderService.getMerchantIncomeMoney(user.getMerchantId(), sevenDaysAgo, tomorrow));
		// 近3个月
		resData.appendIncomeInfo("近3个月收益金额", consumerOrderService.getMerchantIncomeMoney(user.getMerchantId(), firstDayOfMonth.minusMonths(2), tomorrow));
		// 近半年
		resData.appendIncomeInfo("近半年收益金额", consumerOrderService.getMerchantIncomeMoney(user.getMerchantId(), firstDayOfMonth.minusMonths(6), tomorrow));
		// 近1年收益金额
		resData.appendIncomeInfo("近1年收益金额", consumerOrderService.getMerchantIncomeMoney(user.getMerchantId(), firstDayOfMonth.minusMonths(12), tomorrow));

		// 优惠统计
		// 优惠券领取数量
		Integer couponReceivedNumber = couponReceiveService.count(w -> w.lambda()
				.eq(CouponReceive::getMerchantId, user.getMerchantId())
				.eq(CouponReceive::getCouponType, CouponTypeEnum.COUPON_TYPE.getCode()));
		// 优惠券使用数量
		Integer couponUsedNumber = couponReceiveService.count(w -> w.lambda()
				.eq(CouponReceive::getMerchantId, user.getMerchantId())
				.eq(CouponReceive::getCouponType, CouponTypeEnum.COUPON_TYPE.getCode())
				.eq(CouponReceive::getUsedStatus, 1));
		resData.setCouponNumberInfo(new MerchantHomeStatistics.CouponNumber(couponReceivedNumber, couponUsedNumber));

		// 红包领取数量
		Integer redpackReceivedNumber = couponReceiveService.count(w -> w.lambda()
				.eq(CouponReceive::getMerchantId, user.getMerchantId())
				.eq(CouponReceive::getCouponType, CouponTypeEnum.RED_PACK_TYPE.getCode()));
		// 红包使用数量
		Integer redpackUsedNumber = couponReceiveService.count(w -> w.lambda()
				.eq(CouponReceive::getMerchantId, user.getMerchantId())
				.eq(CouponReceive::getCouponType, CouponTypeEnum.RED_PACK_TYPE.getCode())
				.eq(CouponReceive::getUsedStatus, 1));
		resData.setRedpackNumberInfo(new MerchantHomeStatistics.CouponNumber(redpackReceivedNumber, redpackUsedNumber));
		return R.ok(resData);
	}

	@GetMapping("/merchant-statistics")
	@ApiOperation("商家统计接口")
	public R<MerchantStatistics> merchantStatistics(@RequestParam(value = "monthNumber", defaultValue = "6") Integer monthNumber) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (null == user.getMerchantId()) {
			return R.failed("获取商家信息失败");
		}
		if (monthNumber <= 0) {
			return R.failed("要查询的最近月数必须大于等于1");
		}

		Integer merchantId = user.getMerchantId();
		MerchantStatistics resData = new MerchantStatistics();
		resData.setIncomeList(consumerOrderService.lastMonthMoneyGroup(merchantId, monthNumber));
		resData.setMemberNumberList(merchantMemberService.lastMonthCountGroup(merchantId, monthNumber));
		resData.setOrderNumberList(consumerOrderService.lastMonthCountGroup(merchantId, monthNumber));

		// 查询今日业绩数据
		MerchantStatistics.TodayNumber todayNumber = new MerchantStatistics.TodayNumber();
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		LocalDate tomorrow = today.plusDays(1);
		// 今日会员
		todayNumber.setMemberNumber(merchantMemberService.count(w -> w.lambda()
				.eq(MerchantMember::getMerchantId, merchantId)
				.between(MerchantMember::getCreateTime, today, tomorrow)));
		todayNumber.setYesterdayMemberNumber(merchantMemberService.count(w -> w.lambda()
				.eq(MerchantMember::getMerchantId, merchantId)
				.between(MerchantMember::getCreateTime, yesterday, today)));
		// 今日订单数
		todayNumber.setOrderNumber(consumerOrderService.count(w -> w.lambda()
				.eq(ConsumerOrder::getMerchantId, merchantId)
				.eq(ConsumerOrder::getNotifyStatus, "SUCCESS")
				.between(ConsumerOrder::getNotifyTime, today, tomorrow)));
		todayNumber.setYesterdayOrderNumber(consumerOrderService.count(w -> w.lambda()
				.eq(ConsumerOrder::getMerchantId, merchantId)
				.eq(ConsumerOrder::getNotifyStatus, "SUCCESS")
				.between(ConsumerOrder::getNotifyTime, yesterday, today)));
		// 今日订单金额
		todayNumber.setMoney(consumerOrderService.getMerchantIncomeMoney(merchantId, today, tomorrow));
		todayNumber.setYesterdayMoney(consumerOrderService.getMerchantIncomeMoney(merchantId, yesterday, today));
		resData.setTodayNumber(todayNumber);
		return R.ok(resData);
	}

}
