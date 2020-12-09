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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.qdzhhl.kbopark.admin.api.entity.SysDept;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.admin.api.feign.RemoteDeptService;
import cn.qdzhhl.kbopark.common.core.constant.CommonConstants;
import cn.qdzhhl.kbopark.common.core.constant.SecurityConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.apidto.MemberOrderDTO;
import com.kbopark.operation.apidto.MonthNumberStatistics;
import com.kbopark.operation.dto.*;
import com.kbopark.operation.entity.*;
import com.kbopark.operation.enums.*;
import com.kbopark.operation.mapper.ConsumerOrderMapper;
import com.kbopark.operation.mapper.MemberAddressMapper;
import com.kbopark.operation.mapper.MerchantMapper;
import com.kbopark.operation.service.*;
import com.kbopark.operation.thirdplatform.dto.OrderPayDTO;
import com.kbopark.operation.thirdplatform.service.OrderApiService;
import com.kbopark.operation.thirdplatform.service.ProductApiService;
import com.kbopark.operation.util.OperationConstants;
import com.kbopark.operation.util.OrderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 消费订单
 *
 * @author pigx code generator
 * @date 2020-09-03 10:17:02
 */
@Service
@Slf4j
@AllArgsConstructor
public class ConsumerOrderServiceImpl extends ServiceImpl<ConsumerOrderMapper, ConsumerOrder> implements ConsumerOrderService {

	private final MerchantMapper merchantMapper;

	private final RemoteDeptService remoteDeptService;

	private final CouponInfoService couponInfoService;

	private final CouponReceiveService couponReceiveService;

	private final CouponUsedLogService couponUsedLogService;

	private final LedgerOrderService ledgerOrderService;

	private final OrderApiService orderApiService;

	private final MemberAddressMapper memberAddressMapper;

	private final MemberInfoService memberInfoService;

	private final ProductApiService productApiService;

	@Override
	public R getOrderById(Integer id) {
		QueryWrapper<ConsumerOrder> query = new QueryWrapper<>();
		query.lambda().eq(ConsumerOrder::getId, id);
		//当前用户判断
		checkUserTypeAndSetCondition(query);
		return R.ok(getOne(query));
	}

	@Override
	public R getOrderPage(Page page, ConsumerOrderDTO consumerOrderDTO) {
		//前端查询条件
		QueryWrapper<ConsumerOrder> query = Wrappers.query(consumerOrderDTO);
		if (StringUtils.isNotBlank(consumerOrderDTO.getSearchName())) {
			query.lambda()
					.like(ConsumerOrder::getMemberPhone, consumerOrderDTO.getSearchName())
					.or()
					.like(ConsumerOrder::getMemberName, consumerOrderDTO.getSearchName());
		}
		List<String> searchTime = consumerOrderDTO.getSearchReceiveTime();
		if (!CollectionUtils.isEmpty(searchTime)) {
			DateTime start = DateUtil.parse(searchTime.get(0));
			DateTime end = DateUtil.parse(searchTime.get(1));
			query.lambda().between(ConsumerOrder::getCreateTime, DateUtil.beginOfDay(start), DateUtil.endOfDay(end));
		}

		//当前用户判断
		checkUserTypeAndSetCondition(query);

		return R.ok(page(page, query));
	}

	@Override
	public ConsumerOrder findByOrderNumber(String orderNumber) {
		QueryWrapper<ConsumerOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ConsumerOrder::getOrderNumber, orderNumber);
		return getOne(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<ConsumerOrder> createOrder(CreateOrderDTO createOrderDTO) {
		//会员信息附带传入
		ConsumerOrder consumerOrder = BeanUtil.copyProperties(createOrderDTO, ConsumerOrder.class);
		//查询商家信息
		Merchant merchant = merchantMapper.selectById(consumerOrder.getMerchantId());
		if(merchant == null){
			return R.failed(OperationConstants.MERCHANT_EMPTY);
		}
		//商家信息
		consumerOrder.setMerchantName(merchant.getSimpleName());
		//渠道商信息
		consumerOrder.setChannelId(merchant.getDistributorId());
		R<SysDept> sysDept = remoteDeptService.remoteQueryId(merchant.getDistributorId(), SecurityConstants.FROM_IN);
		if(sysDept.getData() != null){
			consumerOrder.setChannelName(sysDept.getData().getName());
		}
		//运营商信息
		consumerOrder.setOperationId(merchant.getOperatorId());
		R<SysDept> deptR = remoteDeptService.remoteQueryId(merchant.getOperatorId(), SecurityConstants.FROM_IN);
		if(deptR.getData() != null){
			consumerOrder.setOperationName(deptR.getData().getName());
		}
		//应付金额，根据应付金额和优惠券信息计算实际付款金额
		Double payable = consumerOrder.getPayable();
		double discount = 0;
		//优惠券信息,传入了优惠券编号要验证优惠券是否可用
		if(StringUtils.isNotBlank(consumerOrder.getCouponSerialNumber())){
			R result = couponInfoService.checkCouponAvailable(consumerOrder.getCouponSerialNumber());
			if(result.getCode() == CommonConstants.FAIL){
				return result;
			}
			//如果选择了优惠券验证会员领取记录
			List<CouponReceive> receives = couponReceiveService.findByMemberPhoneAndCouponCode(consumerOrder.getMemberPhone(),
					consumerOrder.getCouponSerialNumber(), LockStatusEnum.UNLOCK.getCode());
			if(CollectionUtils.isEmpty(receives)){
				return R.failed(OperationConstants.COUPON_NO_RECEIVE);
			}
			//如果验证通过可以使用优惠券则锁定任意一个相同类型的优惠券
			CouponReceive couponReceive = receives.get(0);
			couponReceive.setLockFlag(LockStatusEnum.LOCKED.getCode());
			couponReceiveService.updateById(couponReceive);

			//设置优惠券信息
			CouponInfo couponInfo = (CouponInfo)result.getData();
			consumerOrder.setCouponName(couponInfo.getName());
			consumerOrder.setCouponType(couponInfo.getType());
			consumerOrder.setCouponRule(couponInfo.getRuleInfo());
			consumerOrder.setCouponUsed(UsedStatusEnum.USED.getCode());
			//红包直接抵扣红包面值
			if(CouponTypeEnum.RED_PACK_TYPE.getCode().equals(couponInfo.getType())){
				discount = couponReceive.getCouponMoney();
			}else if(CouponMiniTypeEnum.FULL_CUT.getCode().equals(couponInfo.getCouponType())){
				//满减券,超过满额设置扣除金额
				discount = payable >= couponInfo.getFullMoney() ? couponInfo.getSubMoney() : 0;
			}
		}else{
			consumerOrder.setCouponUsed(UsedStatusEnum.UNUSED.getCode());
		}

		//设置实际付款金额和折扣金额
		double money = payable - discount < 0 ? 0 : payable - discount;
		consumerOrder.setDiscount(discount);
		consumerOrder.setMoney(money);

		//设置流水号
		if(StringUtils.isBlank(consumerOrder.getOrderNumber())){
			consumerOrder.setOrderNumber(OrderUtil.getSerialNumber(null));
		}

		if(StringUtils.isBlank(consumerOrder.getOrderType())){
			consumerOrder.setOrderType(OrderTypeEnum.SCAN_CODE.getCode());
		}

		//设置商品描述信息，使用商家简称
		if(StringUtils.isBlank(consumerOrder.getProductDes())){
			consumerOrder.setProductDes(merchant.getSimpleName());
		}

		//状态信息
		consumerOrder.setNotifyStatus(OrderStatusEnum.WAIT.getCode());

		//保存订单信息
		consumerOrder.setCreateTime(null);
		consumerOrder.setUpdateTime(null);
		boolean save = save(consumerOrder);
		if(save){
			return R.ok(consumerOrder);
		}else{
			return R.failed(OperationConstants.ORDER_ERROR);
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<ConsumerOrder> createOrder(MemberOrderDTO memberOrderDTO, String orderNumber, String thirdOrderNumber) {

		//获取到当前登录的会员信息
		KboparkUser user = SecurityUtils.getUser();
		CreateOrderDTO createOrder = new CreateOrderDTO();

		//订单号
		createOrder.setOrderNumber(orderNumber);
		createOrder.setThirdNumber(thirdOrderNumber);
		createOrder.setOrderType(OrderTypeEnum.QXD_OD.getCode());

		//设置会员信息
		createOrder.setMemberPhone(user.getPhone());
		createOrder.setMemberName(user.getRealName());
		createOrder.setMemberAvatar(user.getAvatar());

		//设置商家ID,青小岛拉取的商品目前指向聚银汇众，待完善后再修改
		createOrder.setMerchantId(27);
		createOrder.setCouponSerialNumber(memberOrderDTO.getCouponSerialNumber());
		createOrder.setProductCode(memberOrderDTO.getProductNo());
		createOrder.setProductDes(memberOrderDTO.getProductName());
		createOrder.setPayable(memberOrderDTO.getPayable());
		createOrder.setTravelTime(memberOrderDTO.getTravelTime());
		createOrder.setSpecType(memberOrderDTO.getSpecType());
		createOrder.setBuyNum(memberOrderDTO.getBuyNum());

		createOrder.setTakeAddressId(memberOrderDTO.getTakeAddressId());
		createOrder.setTreeId(memberOrderDTO.getTreeId());

		//根据前端地址ID获取对应地址信息
		//TODO 现行版本为卷码，去掉地址
//		MemberAddress memberAddress = memberAddressMapper.selectById(memberOrderDTO.getTakeAddressId());

		createOrder.setTakeName(user.getUsername());
		createOrder.setTakePhone(user.getPhone());
//		createOrder.setTakeAddress(memberAddress.getTakeAddress());
//		createOrder.setTakeAddressDetail(memberAddress.getTakeAddressDetail());
		createOrder.setProductPrice(OrderUtil.div(memberOrderDTO.getPayable()/memberOrderDTO.getBuyNum(),2));

		//调用另一个下单接口统一处理
		return createOrder(createOrder);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R notifyChangeStatus(String orderNumber) {
		if(StringUtils.isBlank(orderNumber)){
			return R.failed(OperationConstants.NOTIFY_ORDER_NO_DATA);
		}
		ConsumerOrder byOrderNumber = findByOrderNumber(orderNumber);
		if(byOrderNumber == null){
			return R.failed(OperationConstants.NOTIFY_ORDER_NO_DATA);
		}
		//更新订单状态
		LocalDateTime now = LocalDateTime.now();
		byOrderNumber.setNotifyStatus(OrderStatusEnum.SUCCESS.getCode());
		byOrderNumber.setUpdateTime(now);
		byOrderNumber.setNotifyTime(now.format(DateTimeFormatter.ofPattern(OperationConstants.FORMAT_DATE_TIME)));
		//如果使用了优惠券
		if(UsedStatusEnum.USED.getCode().equals(byOrderNumber.getCouponUsed())){
			//生成使用记录
			CouponUsedLog couponUsedLog = BeanUtil.copyProperties(byOrderNumber, CouponUsedLog.class);
			//更新会员已锁定待更新的优惠券
			List<CouponReceive> receives = couponReceiveService.findByMemberPhoneAndCouponCode(byOrderNumber.getMemberPhone(),
					byOrderNumber.getCouponSerialNumber(), LockStatusEnum.LOCKED.getCode());
			if(!CollectionUtils.isEmpty(receives)){
				CouponReceive couponReceive = receives.get(0);
				couponReceive.setUsedStatus(UsedStatusEnum.USED.getCode());
				couponReceive.setUsedTime(now);
				couponReceiveService.updateById(couponReceive);
				//设置优惠券信息并生成日志
				BeanUtil.copyProperties(couponReceive, couponUsedLog);
				couponUsedLog.setCreateTime(now);
				couponUsedLog.setReceiveId(couponReceive.getId());
				couponUsedLogService.save(couponUsedLog);
			}
		}

		//更新订单
		updateById(byOrderNumber);

		//自动发放乘车券
		CouponReceiveDTO couponReceiveDTO = new CouponReceiveDTO();
		couponReceiveDTO.setMerchantId(byOrderNumber.getMerchantId());
		couponReceiveDTO.setOrderNumber(byOrderNumber.getOrderNumber());
		couponReceiveDTO.setPhone(byOrderNumber.getMemberPhone());
		couponReceiveDTO.setImage(byOrderNumber.getMemberAvatar());
		couponReceiveDTO.setNickName(byOrderNumber.getMemberName());
		couponReceiveDTO.setCouponSerialNumber("-1");
		//扫商家码付款订单自动赠送乘车券
		if(OrderTypeEnum.SCAN_CODE.getCode().equals(byOrderNumber.getOrderType())){
			R r = couponReceiveService.receiveTicket(couponReceiveDTO);
			log.info(">>>支付成功，自动发放乘车券返回结果："+ JSONObject.toJSONString(r));
		}

		//TODO
		//支付成功创建待分账订单
		R ledgerOrder = ledgerOrderService.createLedgerOrder(byOrderNumber);
		log.info(">>>支付成功，创建待分账订单返回结果："+ JSONObject.toJSONString(ledgerOrder));

		//如果是青小岛的订单则发送支付成功通知
		if(OrderTypeEnum.QXD_OD.getCode().equals(byOrderNumber.getOrderType())){
			OrderPayDTO orderPayDTO = new OrderPayDTO();
			orderPayDTO.setOrderId(byOrderNumber.getThirdNumber());
			R r1 = orderApiService.notifySuccess(orderPayDTO);
			log.info(">>>支付成功，请求青小岛返回结果："+ JSONObject.toJSONString(r1));
		}


		return R.ok();
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public R cancelPayOrder(String orderNumber, boolean hasPrefix) {
		if(StringUtils.isBlank(orderNumber)){
			return R.failed(OperationConstants.NOTIFY_ORDER_NO_DATA);
		}
		orderNumber = hasPrefix ? orderNumber.substring(4) : orderNumber;
		ConsumerOrder byOrderNumber = findByOrderNumber(orderNumber);
		if(byOrderNumber == null){
			return R.failed(OperationConstants.NOTIFY_ORDER_NO_DATA);
		}
		byOrderNumber.setUpdateTime(LocalDateTime.now());
		byOrderNumber.setNotifyStatus(OrderStatusEnum.CANCEL.getCode());

		//如果使用了优惠券
		if(UsedStatusEnum.USED.getCode().equals(byOrderNumber.getCouponUsed())){
			//查询会员已锁定待更新的优惠券
			List<CouponReceive> receives = couponReceiveService.findByMemberPhoneAndCouponCode(byOrderNumber.getMemberPhone(),
					byOrderNumber.getCouponSerialNumber(), LockStatusEnum.LOCKED.getCode());
			if(!CollectionUtils.isEmpty(receives)){
				CouponReceive couponReceive = receives.get(0);
				couponReceive.setUsedStatus(UsedStatusEnum.UNUSED.getCode());
				couponReceive.setLockFlag(LockStatusEnum.UNLOCK.getCode());
				couponReceive.setUsedTime(null);
				couponReceive.setUpdateTime(LocalDateTime.now());
				//解锁未使用的优惠券
				couponReceiveService.updateById(couponReceive);
				//删除使用记录
				couponUsedLogService.remove(Wrappers.<CouponUsedLog>lambdaQuery().eq(CouponUsedLog::getReceiveId,couponReceive.getId()));
			}
		}

		return R.ok(updateById(byOrderNumber));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R checkPayOrder() {
		//检测当前用户是否存在未支付订单
		//存在未支付订单则设置为失效状态，并还原订单中使用的优惠券锁定状态
		KboparkUser user = SecurityUtils.getUser();
		String phone = user.getPhone();
		QueryWrapper<ConsumerOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(ConsumerOrder::getMemberPhone, phone)
				.eq(ConsumerOrder::getNotifyStatus, OrderStatusEnum.WAIT.getCode());
		List<ConsumerOrder> list = list(queryWrapper);
		List<CouponReceive> receiveList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(list)){
			for (ConsumerOrder order : list) {
				order.setNotifyStatus(OrderStatusEnum.CANCEL.getCode());
				String couponSerialNumber = order.getCouponSerialNumber();
				if(StringUtils.isNotBlank(couponSerialNumber)){
					List<CouponReceive> receives = couponReceiveService.findByMemberPhoneAndCouponCode(phone, couponSerialNumber, LockStatusEnum.LOCKED.getCode());
					receiveList.addAll(receives);
				}
			}
			this.updateBatchById(list);
			if(CollectionUtils.isNotEmpty(receiveList)){
				for (CouponReceive receive : receiveList) {
					receive.setLockFlag(LockStatusEnum.UNLOCK.getCode());
				}
				couponReceiveService.updateBatchById(receiveList);
			}
		}
		return R.ok();
	}

	@Override
	public IPage<MerchantPayOrderDTO> selectMerchantOrderPage(Page<MerchantPayOrderDTO> page, MerchantOrderSearchDTO param) {
		return page.setRecords(baseMapper.selectMerchantOrderPage(page, param));
	}

	@Override
	public Double getMerchantIncomeMoney(Integer merchantId, LocalDate startDate, LocalDate endDate) {
		return baseMapper.getMerchantIncomeMoney(merchantId, startDate, endDate);
	}

	@Override
	public List<MonthNumberStatistics> lastMonthCountGroup(Integer merchantId, Integer monthNumber) {
		List<Integer> offsetList = Stream.iterate(0, pre -> pre + 1).limit(monthNumber).collect(Collectors.toList());
		return baseMapper.lastMonthCountGroup(offsetList, merchantId);
	}

	@Override
	public List<MonthNumberStatistics> lastMonthMoneyGroup(Integer merchantId, Integer monthNumber) {
		List<Integer> offsetList = Stream.iterate(0, pre -> pre + 1).limit(monthNumber).collect(Collectors.toList());
		return baseMapper.lastMonthMoneyGroup(offsetList, merchantId);
	}

	/**
	 * 判断用户类型并设置查询条件
	 *
	 * @param queryWrapper
	 */
	private void checkUserTypeAndSetCondition(QueryWrapper<ConsumerOrder> queryWrapper) {
		//当前用户判断
		KboparkUser user = SecurityUtils.getUser();
		//非总平台用户查看数据增加所属限制
		if (!UserTypeEnum.Administrator.getCode().equals(user.getUserType())) {
			if (UserTypeEnum.Operation.getCode().equals(user.getUserType())) {
				queryWrapper.lambda().eq(ConsumerOrder::getOperationId, user.getDeptId());
			}else if (UserTypeEnum.Channel.getCode().equals(user.getUserType()) || UserTypeEnum.Promoter.getCode().equals(user.getUserType())) {
				queryWrapper.lambda().eq(ConsumerOrder::getChannelId, user.getDeptId());
			}else if (UserTypeEnum.Merchant.getCode().equals(user.getUserType()) || UserTypeEnum.MerchantLower.getCode().equals(user.getUserType())) {
				queryWrapper.lambda().eq(ConsumerOrder::getMerchantId, user.getMerchantId());
			}else{
				queryWrapper.lambda().eq(ConsumerOrder::getMerchantId, -1);
			}
		}
	}

}
