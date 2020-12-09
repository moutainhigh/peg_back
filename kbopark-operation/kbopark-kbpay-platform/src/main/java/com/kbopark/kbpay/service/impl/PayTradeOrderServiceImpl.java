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
package com.kbopark.kbpay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.kbpay.config.PayUrl;
import com.kbopark.kbpay.config.PublicParam;
import com.kbopark.kbpay.config.RefundParam;
import com.kbopark.kbpay.dto.BizConsumerOrderDTO;
import com.kbopark.kbpay.dto.PayParam;
import com.kbopark.kbpay.dto.RefundDTO;
import com.kbopark.kbpay.entity.MerchantPayChannel;
import com.kbopark.kbpay.entity.PayTradeOrder;
import com.kbopark.kbpay.entity.SystemPayChannel;
import com.kbopark.kbpay.enums.MsgTypeEnum;
import com.kbopark.kbpay.h5pay.util.H5PayUtil;
import com.kbopark.kbpay.mapper.PayTradeOrderMapper;
import com.kbopark.kbpay.paysdk.IKbPayService;
import com.kbopark.kbpay.paysdk.PayChannelValueEnum;
import com.kbopark.kbpay.paysdk.PayResult;
import com.kbopark.kbpay.response.RefundResult;
import com.kbopark.kbpay.service.MerchantPayChannelService;
import com.kbopark.kbpay.service.PayTradeOrderService;
import com.kbopark.kbpay.service.SystemPayChannelService;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.enums.OrderStatusEnum;
import com.kbopark.operation.fegin.RemoteMerchantService;
import com.kbopark.operation.fegin.RemoteOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付订单
 *
 * @author laomst
 * @date 2020-09-03 16:55:15
 */
@Service
@Slf4j
@AllArgsConstructor
public class PayTradeOrderServiceImpl extends ServiceImpl<PayTradeOrderMapper, PayTradeOrder> implements PayTradeOrderService {

	private final RemoteOrderService remoteOrderService;

	private final MerchantPayChannelService merchantPayChannelService;

	private final SystemPayChannelService systemPayChannelService;

	private final IKbPayService payService;

	private final RemoteMerchantService remoteMerchantService;

	private final PayUrl payUrl;

	@Override
	@Transactional
	public PayResult jsPay(PayParam param) {

		PayTradeOrder order = generatorOrder(param);
		// 处理生成传递给第三方支付的订单
		String payChannelOrderId = payService.generatePayChannelOrderId(order);
		if (StrUtil.isNotBlank(payChannelOrderId)) {
			order.setPayChannelOrderId(payChannelOrderId);
		}
		updateById(order);
		//如果配置使用H5支付通道则创建H5支付链接，否则创建扫码支付链接
		if(PayChannelValueEnum.CHINA_H5.getCode().equals(order.getPayChannelValue())){
			return payService.createH5PayUrl(order);
		}else{
			return payService.jsPay(order);
		}
	}

	@Override
	public PayTradeOrder getByBizNumber(String bizNumber) {
		return getOne(w -> w.lambda()
				.eq(PayTradeOrder::getTradeBizNumber, bizNumber));
	}

	@Override
	@Transactional
	public PayTradeOrder generatorOrder(PayParam param) {
		ConsumerOrder consumerOrder = remoteOrderService.getByOrderNumber(param.getTradeBizNumber()).getData();
		if (null == consumerOrder) {
			throw new UnsupportedOperationException("获取订单信息失败！");
		}
		Merchant merchant = remoteMerchantService.getById(consumerOrder.getMerchantId()).getData();
		if (null == merchant) {
			throw new UnsupportedOperationException("获取商家信息失败");
		}
		if (merchant.getReviewStatus() < 50) {
			throw new UnsupportedOperationException("商家新增未审核通过，不能创建支付订单");
		}
		if (merchant.getStatus() == 2) {
			throw new UnsupportedOperationException("商家处于禁用状态，不能创建支付订单");
		}
		BizConsumerOrderDTO bizOrder = BeanUtil.toBean(consumerOrder, BizConsumerOrderDTO.class);
		bizOrder.setOperationId(merchant.getOperatorId());
		bizOrder.setChannelId(merchant.getDistributorId());
		List<MerchantPayChannel> merchantPayChannels = merchantPayChannelService.canUseOfMerchant(bizOrder.getMerchantId());// 从远程查到的订单中获取商家id
		merchantPayChannels = merchantPayChannels.stream().filter(item -> PayChannelValueEnum.codeOf(item.getChannelValue()) != null).collect(Collectors.toList());
		if (CollectionUtil.isEmpty(merchantPayChannels)) {
			throw new UnsupportedOperationException("商家没有可用的支付通道");
		}
		MerchantPayChannel merchantPayChannel = merchantPayChannels.get(0);
		SystemPayChannel systemPayChannel = systemPayChannelService.getOne(w -> w.lambda().eq(SystemPayChannel::getValue, merchantPayChannel.getChannelValue()));
		if (null == systemPayChannel || systemPayChannel.getStatus() == 2) {
			throw new UnsupportedOperationException("对应的系统支付通道不可用");
		}
		// 创建支付订单
		PayTradeOrder entity = getOne(w -> w.lambda().eq(PayTradeOrder::getTradeBizNumber, param.getTradeBizNumber()));
		if (null == entity) {
			entity = new PayTradeOrder(bizOrder, merchantPayChannel, systemPayChannel, param.getReturnUrl());
		} else {
			entity.update(bizOrder, merchantPayChannel, systemPayChannel, param.getReturnUrl());
		}
		saveOrUpdate(entity);
		return entity;
	}


	@Override
	public PublicParam getPublicParam(String orderNumber, String msgType, MerchantPayChannel merchantPayChannel) {
		Map<String, String> paramsMap = merchantPayChannel.getParamsMap();
		PublicParam publicParam = new PublicParam();
		publicParam.setMsgSrc(paramsMap.get("msgSrc"));
		publicParam.setMsgType(msgType);
		publicParam.setInstMid(paramsMap.get("instMid"));
		publicParam.setMid(paramsMap.get("mid"));
		publicParam.setTid(paramsMap.get("tid"));
		publicParam.setSignType("SHA256");
		publicParam.setRequestTimestamp(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		publicParam.setMerOrderId(paramsMap.get("systemId") + orderNumber);
		return publicParam;
	}

	@Override
	public RefundResult refundApply(RefundDTO refundDTO) {

		//根据订单编号获取消费订单
		ConsumerOrder consumerOrder = remoteOrderService.getByOrderNumber(refundDTO.getTradeBizNumber()).getData();

		//如果消费订单为空
		if (null == consumerOrder) {
			throw new UnsupportedOperationException("获取订单信息失败！");
		}

		if(!OrderStatusEnum.SUCCESS.getCode().equals(consumerOrder.getNotifyStatus())){
			throw new UnsupportedOperationException("订单未支付！");
		}
		// 从远程查到的订单中获取商家id
		List<MerchantPayChannel> merchantPayChannels = merchantPayChannelService.canUseOfMerchant(consumerOrder.getMerchantId());
		merchantPayChannels = merchantPayChannels.stream().filter(item -> PayChannelValueEnum.codeOf(item.getChannelValue()) != null).collect(Collectors.toList());
		if (CollectionUtil.isEmpty(merchantPayChannels)) {
			throw new UnsupportedOperationException("商家没有可用的支付通道");
		}
		MerchantPayChannel merchantPayChannel = merchantPayChannels.get(0);
		Map<String, String> paramsMap = merchantPayChannel.getParamsMap();
		PublicParam publicParam = getPublicParam(refundDTO.getTradeBizNumber(), MsgTypeEnum.REFUND.getCode(), merchantPayChannel);

		RefundParam refundParam = BeanUtil.copyProperties(publicParam, RefundParam.class);

		//计算退款金额
		Double refundMoney = refundDTO.getTotalPrice() * 100;

		refundParam.setRefundAmount(String.valueOf(refundMoney.intValue()));
		refundParam.setRefundDesc("申请退款");
		String md5SecretKey = paramsMap.get("md5SecretKey");
		Map map = JSONObject.parseObject(JSONObject.toJSONString(refundParam), Map.class);
		String sign = H5PayUtil.makeSign(md5SecretKey, map);
		refundParam.setSign(sign);
		log.info(">>>申请退款参数："+JSONObject.toJSONString(refundParam));
		String result = HttpUtil.post(payUrl.getRefundPostUrl(), JSONObject.toJSONString(refundParam));
		log.info(">>>申请退款响应："+ result);
		return JSONObject.parseObject(result, RefundResult.class);
	}
}
