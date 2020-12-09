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

package com.kbopark.kbpay.service;

import cn.qdzhhl.kbopark.common.data.mybatis.KbBaseService;
import com.kbopark.kbpay.entity.MerchantPayChannel;

import java.util.List;

/**
 * 商户支付通道设置
 *
 * @author laomst
 * @date 2020-09-03 11:41:58
 */
public interface MerchantPayChannelService extends KbBaseService<MerchantPayChannel> {

	// 删除
	Boolean removeMerchantPayChannel(Integer id);

	// 一个商家所有可用的支付通道
	List<MerchantPayChannel> canUseOfMerchant(Integer merchantId);

	// 新增
	Boolean saveMerchantPayChannel(MerchantPayChannel merchantPayChannel);

	// 修改
	Boolean updateMerchantPayChannel(MerchantPayChannel merchantPayChannel);

}
