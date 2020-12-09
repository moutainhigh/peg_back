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

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.kbpay.entity.MerchantPayChannel;
import com.kbopark.kbpay.entity.PayTradeOrder;
import com.kbopark.kbpay.entity.SystemPayChannel;
import com.kbopark.kbpay.mapper.PayTradeOrderMapper;
import com.kbopark.kbpay.mapper.SystemPayChannelMapper;
import com.kbopark.kbpay.service.MerchantPayChannelService;
import com.kbopark.kbpay.service.PayTradeOrderService;
import com.kbopark.kbpay.service.SystemPayChannelService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统支付通道
 *
 * @author laomst
 * @date 2020-09-03 10:05:23
 */
@Service
@AllArgsConstructor
public class SystemPayChannelServiceImpl extends ServiceImpl<SystemPayChannelMapper, SystemPayChannel> implements SystemPayChannelService {

	private final MerchantPayChannelService merchantPayChannelService;

	private final PayTradeOrderMapper payTradeOrderMapper;

	@Override
	public Boolean removeSystemPayChannel(Integer id) {
		SystemPayChannel entity = getById(id);
		if (null == entity) {
			throw new UnsupportedOperationException("系统中没有对应的实体");
		}
		Integer count = merchantPayChannelService.count(w -> {
			w.lambda().eq(MerchantPayChannel::getChannelValue, entity.getValue());
		});
		if (count > 0) {
			throw new UnsupportedOperationException("已经有商家配置了该类型的通道，无法进行删除");
		}
		QueryWrapper<PayTradeOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(PayTradeOrder::getPayChannelValue, entity.getValue());
		count = payTradeOrderMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw new UnsupportedOperationException("该类型的支付通道已经产生支付订单了，无法进行删除");
		}
		return removeById(id);
	}

	@Override
	public Boolean saveSystemPayChannel(SystemPayChannel systemPayChannel) {
		SystemPayChannel one = getOne(w -> {
			w.lambda().eq(SystemPayChannel::getValue, systemPayChannel.getValue());
		});
		if (one != null) {
			throw new UnsupportedOperationException("系统中已经存在值为 " + systemPayChannel.getValue() + " 的支付通道了，不能重复");
		}
		return save(systemPayChannel);
	}

	@Override
	public Boolean updateSystemPayChannel(SystemPayChannel entity) {
		SystemPayChannel oldEntity = getById(entity.getId());
		if (null == oldEntity) {
			throw new UnsupportedOperationException("系统中没有对应的实体");
		}
		if (!ObjectUtil.equal(oldEntity.getValue(), entity.getValue())) {
			throw new UnsupportedOperationException("不能修改通道值");
		}
		return updateById(entity);
	}

	@Override
	public Boolean upOrDown(Integer id) {
		SystemPayChannel entity = getById(id);
		if (null == entity) {
			throw new UnsupportedOperationException("系统中没有对应的实体");
		}
		entity.setStatus(entity.getStatus() == 1 ? 2 : 1);
		return updateById(entity);
	}
}
