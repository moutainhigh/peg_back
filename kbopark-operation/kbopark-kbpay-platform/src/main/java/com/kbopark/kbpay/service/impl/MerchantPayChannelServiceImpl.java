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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.kbpay.entity.MerchantPayChannel;
import com.kbopark.kbpay.entity.PayTradeOrder;
import com.kbopark.kbpay.mapper.MerchantPayChannelMapper;
import com.kbopark.kbpay.mapper.PayTradeOrderMapper;
import com.kbopark.kbpay.service.MerchantPayChannelService;
import com.kbopark.kbpay.service.PayTradeOrderService;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.fegin.RemoteMerchantService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户支付通道设置
 *
 * @author laomst
 * @date 2020-09-03 11:41:58
 */
@Service
@AllArgsConstructor
public class MerchantPayChannelServiceImpl extends ServiceImpl<MerchantPayChannelMapper, MerchantPayChannel> implements MerchantPayChannelService {

	private final PayTradeOrderMapper payTradeOrderMapper;

	private final RemoteMerchantService remoteMerchantService;

	@Override
	public Boolean removeMerchantPayChannel(Integer id) {
		QueryWrapper<PayTradeOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(PayTradeOrder::getMerchantPayChannelId, id);
		Integer count = payTradeOrderMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw new UnsupportedOperationException("该支付通道已经产生支付订单了，不能进行删除");
		}
		return removeById(id);
	}

	@Override
	public List<MerchantPayChannel> canUseOfMerchant(Integer merchantId) {
		return list(w -> w.eq("(select status from kboparkx_system_pay_channel a where kboparkx_merchant_pay_channel.channel_value=a.value)", 1)
				.lambda()
				.eq(MerchantPayChannel::getMerchantId, merchantId)
				.eq(MerchantPayChannel::getStatus, 1));
	}

	@Override
	public Boolean saveMerchantPayChannel(MerchantPayChannel merchantPayChannel) {
		Merchant merchant = remoteMerchantService.getById(merchantPayChannel.getMerchantId()).getData();
		if (null == merchant) {
			throw new UnsupportedOperationException("获取商家信息失败");
		}
		merchantPayChannel.setOperatorId(merchant.getOperatorId());
		merchantPayChannel.setDistributorId(merchant.getDistributorId());
		return save(merchantPayChannel);
	}

	@Override
	public Boolean updateMerchantPayChannel(MerchantPayChannel merchantPayChannel) {
		Merchant merchant = remoteMerchantService.getById(merchantPayChannel.getMerchantId()).getData();
		if (null == merchant) {
			throw new UnsupportedOperationException("获取商家信息失败");
		}
		merchantPayChannel.setOperatorId(merchant.getOperatorId());
		merchantPayChannel.setDistributorId(merchant.getDistributorId());
		return updateById(merchantPayChannel);
	}
}
