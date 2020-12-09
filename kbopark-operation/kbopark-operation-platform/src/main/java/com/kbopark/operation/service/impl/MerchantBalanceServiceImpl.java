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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.entity.MerchantBalance;
import com.kbopark.operation.mapper.MerchantBalanceMapper;
import com.kbopark.operation.service.MerchantBalanceService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * 商家资金表
 *
 * @author laosmt
 * @date 2020-10-29 09:41:02
 */
@Service
public class MerchantBalanceServiceImpl extends ServiceImpl<MerchantBalanceMapper, MerchantBalance> implements MerchantBalanceService {

	@Override
	public boolean init4Merchant(Integer merchantId) {
		Objects.requireNonNull(merchantId, "商家id不能为空");
		Optional<MerchantBalance> optionalMerchantBalance = getOfMerchant(merchantId);
		boolean res = true;
		if (optionalMerchantBalance.isPresent()) {
			res = save(MerchantBalance.init4Merchant(merchantId));
		}
		return res;
	}

	@Override
	public Optional<MerchantBalance> getOfMerchant(Integer merchantId) {
		return Optional.ofNullable(getOne(w -> w.lambda().eq(MerchantBalance::getMerchantId, merchantId)));
	}

	@Override
	public void addToAccount(Integer merchantId, Double money) {
		MerchantBalance merchantBalance = getOfMerchant(merchantId).orElse(null);
		Objects.requireNonNull(merchantBalance, "获取商家资金信息失败");
		merchantBalance.addToAccount(money);
		updateById(merchantBalance);
	}

	@Override
	public void subAccount(Integer merchantId, Double money) {
		MerchantBalance merchantBalance = getOfMerchant(merchantId).orElse(null);
		Objects.requireNonNull(merchantBalance, "获取商家资金信息失败");
		merchantBalance.toAccount(money);
		updateById(merchantBalance);
	}
}
