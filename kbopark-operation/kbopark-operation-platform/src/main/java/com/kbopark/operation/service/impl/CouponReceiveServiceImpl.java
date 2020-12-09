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
package com.kbopark.operation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.common.core.constant.CommonConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.apidto.MonthNumberStatistics;
import com.kbopark.operation.dto.*;
import com.kbopark.operation.entity.*;
import com.kbopark.operation.enums.*;
import com.kbopark.operation.mapper.*;
import com.kbopark.operation.service.CouponInfoService;
import com.kbopark.operation.service.CouponReceiveService;
import com.kbopark.operation.service.MerchantMemberService;
import com.kbopark.operation.subway.util.*;
import com.kbopark.operation.util.OperationConstants;
import com.kbopark.operation.util.OrderUtil;
import com.kbopark.operation.util.TicketRule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 优惠券领取记录
 *
 * @author pigx code generator
 * @date 2020-09-01 09:35:43
 */
@Service
@Slf4j
@AllArgsConstructor
public class CouponReceiveServiceImpl extends ServiceImpl<CouponReceiveMapper, CouponReceive> implements CouponReceiveService {

	private final MerchantMemberService merchantMemberService;

	private final CouponInfoService couponInfoService;

	private final ConsumerOrderMapper consumerOrderMapper;

	private final MerchantWelfareSettingMapper welfareSettingMapper;

	private final SubwayTicketMapper subwayTicketMapper;

	private final MerchantMapper merchantMapper;

	private final HttpConfig httpConfig;


	/***
	 * 创建逻辑处理:
	 * 1.新增或更新会员商户关联关系
	 * 2.新增或更新会员基本信息
	 * 3.新增会员优惠券领取记录
	 * @param couponReceiveDTO        对接数据
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<CouponReceive> receiveAndCreateRecord(CouponReceiveDTO couponReceiveDTO) {
		//1.根据券标识查询到券基本信息
		//2.根据基本信息判断券的有效期和剩余数量
		//3.根据券标识和用户手机号查询用户领取数量
		//4.根据券使用规则的每人限制领取数量判断
		//5.设置默认状态
		CouponInfo couponInfo = couponInfoService.getBySerialNumber(couponReceiveDTO.getCouponSerialNumber());
		R<CouponInfo> couponInfoR = couponInfoService.checkCouponAvailable(couponInfo);
		if (couponInfoR.getCode() == CommonConstants.FAIL) {
			return R.failed(couponInfoR.getMsg());
		}
		//用户领取券的数量
		List<CouponReceive> receiveList = findByMemberPhoneAndCouponCode(couponReceiveDTO.getPhone(),
				couponReceiveDTO.getCouponSerialNumber(), LockStatusEnum.UNLOCK.getCode());

		CouponReceive couponReceive = BeanUtil.copyProperties(couponReceiveDTO, CouponReceive.class);
		BeanUtil.copyProperties(couponInfo, couponReceive);
		couponReceive.setId(null);
		couponReceive.setMemberLogo(couponReceiveDTO.getImage());
		couponReceive.setMemberPhone(couponReceiveDTO.getPhone());
		couponReceive.setMemberName(couponReceiveDTO.getNickName());
		couponReceive.setCouponType(couponInfo.getType());
		couponReceive.setCouponName(couponInfo.getName());
		couponReceive.setCouponRule(couponInfo.getRuleInfo());
		couponReceive.setCouponStartTime(couponInfo.getStartTime());
		couponReceive.setCouponEndTime(couponInfo.getEndTime());
		couponReceive.setLockFlag(LockStatusEnum.UNLOCK.getCode());
		couponReceive.setUsedStatus(UsedStatusEnum.UNUSED.getCode());
		couponReceive.setOrderNumber(couponReceiveDTO.getOrderNumber());
		//优惠券判断
		if (CouponTypeEnum.COUPON_TYPE.getCode().equals(couponInfo.getType())) {
			Integer totalNumber = couponInfo.getTotalNumber();
			Integer usedNumber = couponInfo.getUsedNumber();
			totalNumber = totalNumber == null ? 0 : totalNumber;
			usedNumber = usedNumber == null ? 0 : usedNumber;
			if (totalNumber.equals(usedNumber)) {
				return R.failed(OperationConstants.COUPON_NO_STOCK);
			}
			if (!CollectionUtils.isEmpty(receiveList) && couponInfo.getLimitNumber().equals(receiveList.size())) {
				return R.failed(OperationConstants.COUPON_FULL_LIMIT);
			}
			couponInfo.setUsedNumber(usedNumber + 1);
			couponInfo.setRemainNumber(totalNumber - (usedNumber + 1));
			couponReceive.setCouponMoney(couponInfo.getSubMoney());
		} else {
			if (RedpackMiniTypeEnum.PLAIN.getCode().equals(couponInfo.getCouponType())) {
				Integer totalNumber = couponInfo.getTotalNumber() == null ? 0 : couponInfo.getTotalNumber();
				Integer usedNumber = couponInfo.getUsedNumber() == null ? 0 : couponInfo.getUsedNumber();
				//普通红包判断数量
				if (totalNumber.equals(usedNumber)) {
					return R.failed(OperationConstants.RED_PACK_FULL);
				}
				if (!CollectionUtils.isEmpty(receiveList) && couponInfo.getLimitNumber().equals(receiveList.size())) {
					return R.failed(OperationConstants.COUPON_FULL_LIMIT);
				}
				couponInfo.setUsedNumber(usedNumber + 1);
				couponInfo.setRemainNumber(totalNumber - (usedNumber + 1));
				//领取的红包金额
				couponReceive.setCouponMoney(couponInfo.getCostMoney());
			} else {
				//随机红包判断总金额
				Double costMoney = couponInfo.getCostMoney();
				Double addUpMoney = couponInfo.getAddUpMoney() == null ? 0 : couponInfo.getAddUpMoney();
				if (addUpMoney.equals(costMoney)) {
					return R.failed(OperationConstants.RED_PACK_FULL);
				}
				if (!CollectionUtils.isEmpty(receiveList) && couponInfo.getLimitNumber().equals(receiveList.size())) {
					return R.failed(OperationConstants.COUPON_FULL_LIMIT);
				}
				Double minMoney = couponInfo.getMinMoney();
				Double maxMoney = couponInfo.getMaxMoney();
				//生成随机红包
				double randomDouble = OrderUtil.getRandomDouble(minMoney, maxMoney, 2);
				if (addUpMoney + randomDouble > costMoney) {
					randomDouble = costMoney - addUpMoney;
					couponInfo.setAddUpMoney(costMoney);
				} else {
					couponInfo.setAddUpMoney(addUpMoney + randomDouble);
				}
				//领取的红包金额
				couponReceive.setCouponMoney(randomDouble);
			}
		}

		//生成一条领取记录
		//生成一条商家和会员绑定关系
		//生成一条会员基本信息
		//更新优惠券信息
		MerchantMember merchantMember = bandMerchantMember(couponReceiveDTO.getNickName(),
				couponReceiveDTO.getPhone(), couponReceiveDTO.getImage(), couponInfo.getMerchantId());
		couponReceive.setMemberId(merchantMember.getMemberId());
		couponReceive.setCreateTime(null);
		couponReceive.setUpdateTime(null);
		this.save(couponReceive);
		couponInfo.setCreateTime(null);
		couponInfo.setUpdateTime(null);
		couponInfoService.updateById(couponInfo);
		//返回领取记录
		return R.ok(couponReceive);
	}

	@Override
	public R<List<CouponReceive>> memberBatchReceive(final BatchReceiveCouponParam param, final MemberInfo memberInfo) {
		Objects.requireNonNull(param, "请设置领取参数");
		Objects.requireNonNull(memberInfo, "请设置领取人员");
		List<CouponReceive> receivedNumberList = param.getCouponSerialNumberList().stream()
				.map(item -> {
					ReceiveCouponParam receiveCouponParam = new ReceiveCouponParam();
					receiveCouponParam.setCouponSerialNumber(item);
					receiveCouponParam.setMerchantId(param.getMerchantId());
					return memberReceive(receiveCouponParam, memberInfo).getData();
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		return R.ok(receivedNumberList);
	}


	/***领取乘车券，系统自动发放**/
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R receiveTicket(CouponReceiveDTO couponReceiveDTO) {
		//1.根据商家乘车券福利规则判断发放给会员乘车券的规格和数量
		//2.请求地铁APP接口发放乘车券
		Integer merchantId = couponReceiveDTO.getMerchantId();
		String orderNumber = couponReceiveDTO.getOrderNumber();
		String phone = couponReceiveDTO.getPhone();
		String nickName = couponReceiveDTO.getNickName();
		String image = couponReceiveDTO.getImage();
		if (StringUtils.isBlank(orderNumber)) {
			return R.failed(OperationConstants.NOTIFY_ORDER_NO_DATA);
		}
		ConsumerOrder byOrderNumber = consumerOrderMapper.selectOne(Wrappers.<ConsumerOrder>lambdaQuery().eq(ConsumerOrder::getOrderNumber, orderNumber));
		if (byOrderNumber == null) {
			return R.failed(OperationConstants.NOTIFY_ORDER_NO_DATA);
		}
		if (!OrderStatusEnum.SUCCESS.getCode().equals(byOrderNumber.getNotifyStatus())) {
			return R.failed(OperationConstants.ORDER_NO_PAY);
		}
		LambdaQueryWrapper<MerchantWelfareSetting> eq = Wrappers.<MerchantWelfareSetting>lambdaQuery()
				.eq(MerchantWelfareSetting::getMerchantId, merchantId)
				.orderByDesc(MerchantWelfareSetting::getFullMoney);
		List<MerchantWelfareSetting> welfareSettings = welfareSettingMapper.selectList(eq);
		if (CollectionUtils.isEmpty(welfareSettings)) {
			return R.failed(OperationConstants.MERCHANT_NO_WELFARE_SETTING);
		}
		Double money = byOrderNumber.getMoney();
		MerchantWelfareSetting welfareSetting = null;
		for (int i = 0; i < welfareSettings.size(); i++) {
			MerchantWelfareSetting setting = welfareSettings.get(i);
			Double fullMoney = setting.getFullMoney();
			if (money >= fullMoney) {
				welfareSetting = setting;
				break;
			}
		}
		if (welfareSetting == null) {
			return R.failed(OperationConstants.MONEY_NOT_FLL_SETTING);
		}
		String ruleInfo = welfareSetting.getRuleInfo();
		if (StringUtils.isBlank(ruleInfo)) {
			return R.failed(OperationConstants.MERCHANT_NO_WELFARE_SETTING);
		}

		Merchant merchant = merchantMapper.selectById(merchantId);
		merchant = merchant == null ? new Merchant() : merchant;
		List<TicketRule> ticketRules = OrderUtil.collectFilter(ruleInfo);
		List<CouponReceive> receiveList = new ArrayList<>();
		for (int i = 0; i < ticketRules.size(); i++) {
			TicketRule ticketRule = ticketRules.get(i);
			CouponReceive receive = new CouponReceive();
			SubwayTicket subwayTicket = subwayTicketMapper.selectById(ticketRule.getCouponId());
			subwayTicket = subwayTicket == null ? new SubwayTicket() : subwayTicket;

			receive.setMerchantId(merchantId);
			receive.setMerchantName(merchant.getName());
			receive.setOperationId(merchant.getOperatorId());
			receive.setCouponSerialNumber(subwayTicket.getSerialNumber());
			receive.setCouponName(subwayTicket.getName());
			receive.setCouponType(CouponTypeEnum.SUBWAY_TICKET_TYPE.getCode());
			receive.setCouponMoney(subwayTicket.getValue());
			Date beginDay = null;
			Date endDay = null;
			if (EffectTypeEnum.BY_DAY.getCode().equals(subwayTicket.getEffectType())) {
				beginDay = DateUtil.beginOfDay(new Date());
				endDay = DateUtil.offsetDay(beginDay, subwayTicket.getEffectDay() - 1);
			} else {
				beginDay = subwayTicket.getEffectStartTime();
				endDay = subwayTicket.getEffectEndTime();
			}
			receive.setCouponStartTime(beginDay);
			receive.setCouponEndTime(endDay);
			receive.setCouponRule(subwayTicket.getRuleInfo());
			receive.setCouponNumber(ticketRule.getCouponNum());
			MerchantMember merchantMember = bandMerchantMember(nickName, phone, image, merchantId);
			receive.setMemberId(merchantMember.getMemberId());
			receive.setMemberName(nickName);
			receive.setMemberPhone(phone);
			receive.setMemberLogo(image);
			receive.setOrderNumber(orderNumber);
			receive.setUsedStatus(UsedStatusEnum.UNUSED.getCode());
			receive.setLockFlag(LockStatusEnum.UNLOCK.getCode());

			//先请求地铁APP发券
			CouponProvide couponProvide = new CouponProvide();
			couponProvide.setData(subwayTicket, receive);
			CouponResponse couponResponse = sendSubwayPost(couponProvide);
			if (couponResponse.getRespcod().equals(ResponseMsgEnum.SUCCESS.getCode())) {
				receiveList.add(receive);
			}
		}
		if (CollectionUtils.isEmpty(receiveList)) {
			return R.failed(OperationConstants.SUBWAY_POST_ERROR);
		}
		return R.ok(saveBatch(receiveList));
	}

	@Override
	public R getPage(Page page, CouponReceiveDTO couponReceiveDTO) {

		QueryWrapper<CouponReceive> query = Wrappers.query(couponReceiveDTO);
		if (StringUtils.isNotBlank(couponReceiveDTO.getSearchName())) {
			query.lambda()
					.like(CouponReceive::getCouponName, couponReceiveDTO.getSearchName())
					.or()
					.like(CouponReceive::getMemberName, couponReceiveDTO.getSearchName());
		}
		List<String> searchTime = couponReceiveDTO.getSearchReceiveTime();
		if (!CollectionUtils.isEmpty(searchTime)) {
			DateTime start = DateUtil.parse(searchTime.get(0));
			DateTime end = DateUtil.parse(searchTime.get(1));
			query.lambda().between(CouponReceive::getCreateTime, DateUtil.beginOfDay(start), DateUtil.endOfDay(end));
		}

		checkUserTypeAndSetCondition(query);

		return R.ok(page(page, query));
	}


	@Override
	public R getTicketRecord(String phone, String type) {
		QueryWrapper<CouponReceive> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(CouponReceive::getMemberPhone, phone)
				.eq(CouponReceive::getCouponType, type)
				.eq(CouponReceive::getLockFlag, LockStatusEnum.UNLOCK.getCode())
				.eq(CouponReceive::getUsedStatus, UsedStatusEnum.UNUSED.getCode());
		return R.ok(list(queryWrapper));
	}

	@Override
	public List<CouponReceive> findByMemberPhoneAndCouponCode(String memberPhone, String couponCode, String lockFlag) {
		QueryWrapper<CouponReceive> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(CouponReceive::getMemberPhone, memberPhone)
				.eq(CouponReceive::getCouponSerialNumber, couponCode)
				.eq(CouponReceive::getLockFlag, lockFlag)
				.eq(CouponReceive::getUsedStatus, UsedStatusEnum.UNUSED.getCode());
		return list(queryWrapper);
	}

	@Override
	public List<CouponReceive> findByMemberPhone(String memberPhone) {
		if (StringUtils.isBlank(memberPhone)) {
			memberPhone = SecurityUtils.getUser().getPhone();
		}
		QueryWrapper<CouponReceive> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("member_phone,coupon_serial_number,coupon_name")
				.lambda()
				.eq(CouponReceive::getMemberPhone, memberPhone).groupBy(CouponReceive::getMemberPhone, CouponReceive::getCouponSerialNumber);
		return list(queryWrapper);
	}

	/**
	 * 绑定会员商家关系
	 *
	 * @param nickName
	 * @param phone
	 * @param image
	 * @param merchantId
	 * @return
	 */
	private MerchantMember bandMerchantMember(String nickName, String phone, String image, Integer merchantId) {
		DtMemberDTO memberInfo = new DtMemberDTO();
		memberInfo.setNickName(nickName);
		memberInfo.setPhone(phone);
		memberInfo.setAvatar(image);
		MerchantMember merchantMember = merchantMemberService.getByDtAndMerchant(memberInfo, merchantId);
		return merchantMember;
	}

	/**
	 * 判断用户类型并设置查询条件
	 *
	 * @param query
	 */
	private void checkUserTypeAndSetCondition(QueryWrapper<CouponReceive> query) {
		KboparkUser user = SecurityUtils.getUser();
		//非总平台用户查看数据增加所属限制
		if (!UserTypeEnum.Administrator.getCode().equals(user.getUserType())) {
			if (UserTypeEnum.Operation.getCode().equals(user.getUserType())) {
				query.lambda().eq(CouponReceive::getOperationId, user.getDeptId());
			} else if (UserTypeEnum.Channel.getCode().equals(user.getUserType()) || UserTypeEnum.Promoter.getCode().equals(user.getUserType())) {
				query.lambda().eq(CouponReceive::getChannelId, user.getDeptId());
			} else if (UserTypeEnum.Merchant.getCode().equals(user.getUserType()) || UserTypeEnum.MerchantLower.getCode().equals(user.getUserType())) {
				query.lambda().eq(CouponReceive::getMerchantId, user.getMerchantId());
			} else {
				query.lambda().eq(CouponReceive::getMerchantId, -1);
			}
		}
	}


	@Override
	public IPage<MemberCouponDTO> selectMemberCouponPage(Page<MemberCouponDTO> page, CouponSearchParam param) {
		return page.setRecords(baseMapper.selectMemberCouponPage(page, param));
	}

	@Override
	public List<MemberCouponDTO> selectMemberCouponList(CouponSearchParam param) {
		return baseMapper.selectMemberCouponList(param);
	}


	@Override
	public CouponResponse sendSubwayPost(CouponProvide couponProvide) {
		String httpParams = RsaUtil.getHttpParams(couponProvide);
		log.info(">>>请求参数：" + httpParams);
		if (StringUtils.isBlank(httpParams)) {
			return new CouponResponse(ResponseMsgEnum.FAIL.getCode(), ResponseMsgEnum.FAIL.getMsg());
		}
		String result = HttpUtil.post(httpConfig.getRemoteUrl(), httpParams);
		return JSONObject.parseObject(result, CouponResponse.class);
	}

	@Override
	public List<MonthNumberStatistics> lastMonthCountGroup(Integer merchantId, Integer monthNumber) {
		List<Integer> offsetList = Stream.iterate(0, pre -> pre + 1).limit(monthNumber).collect(Collectors.toList());
		return baseMapper.lastMonthCountGroup(offsetList, merchantId);
	}

	@Override
	public R<Boolean> immediateUse(String serialNumber, String memberPhone) {
		R result = couponInfoService.checkCouponAvailable(serialNumber);
		if (result.getCode() == CommonConstants.FAIL) {
			return R.failed(result.getMsg());
		}
		//如果选择了优惠券验证会员领取记录
		List<CouponReceive> receives = findByMemberPhoneAndCouponCode(memberPhone, serialNumber, LockStatusEnum.UNLOCK.getCode());
		if (CollectionUtils.isEmpty(receives)) {
			return R.failed(OperationConstants.COUPON_NO_RECEIVE);
		}
		//如果验证通过可以使用优惠券则锁定任意一个相同类型的优惠券
		CouponReceive couponReceive = receives.get(0);
		couponReceive.setUsedStatus(1);
		couponReceive.setUsedTime(LocalDateTime.now());
		updateById(couponReceive);

		return R.ok(receives.size() > 1);
	}

}
