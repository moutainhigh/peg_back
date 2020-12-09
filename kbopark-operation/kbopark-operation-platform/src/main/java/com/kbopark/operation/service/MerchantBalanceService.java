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

package com.kbopark.operation.service;

import cn.qdzhhl.kbopark.common.data.mybatis.KbBaseService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.entity.MerchantBalance;

import java.util.Optional;

/**
 * 商家资金表
 *
 * @author laosmt
 * @date 2020-10-29 09:41:02
 */
public interface MerchantBalanceService extends KbBaseService<MerchantBalance> {

	/**
	 * 初始化商家资金信息
	 * @param merchantId
	 * @return
	 */
	boolean init4Merchant(Integer merchantId);

	Optional<MerchantBalance> getOfMerchant(Integer merchantId);

	/**
	 * 增加商家的待入账金额
	 * @param merchantId
	 * @param money
	 */
	void addToAccount(Integer merchantId, Double money);

	/**
	 * 分账审核通过的时候，把待入账金额转移到可用金额
	 * @param merchantId
	 * @param money
	 */
	void subAccount(Integer merchantId, Double money);

}
