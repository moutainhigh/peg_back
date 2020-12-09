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

import cn.qdzhhl.kbopark.common.core.util.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.kbpay.config.PublicParam;
import com.kbopark.kbpay.dto.PayParam;
import com.kbopark.kbpay.dto.RefundDTO;
import com.kbopark.kbpay.entity.MerchantPayChannel;
import com.kbopark.kbpay.entity.PayTradeOrder;
import com.kbopark.kbpay.paysdk.PayResult;
import com.kbopark.kbpay.response.RefundResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 支付订单
 *
 * @author laomst
 * @date 2020-09-03 16:55:15
 */
public interface PayTradeOrderService extends IService<PayTradeOrder> {

	// h5支付接口
	@Transactional
	PayResult jsPay(PayParam param);

	/**
	 * 根据业务流水号获取唯一的支付订单
	 * @param bizNumber
	 * @return
	 */
	PayTradeOrder getByBizNumber(String bizNumber);

	/**
	 * 生成支付订单
	 * @param param
	 * @return
	 */
	@Transactional
	PayTradeOrder generatorOrder(PayParam param);


	/**
	 * 通过业务流水号和商家支付通道参数构建请求公共参数
	 *
	 * @param orderNumber
	 * @param msgType
	 * @param merchantPayChannel
	 * @return
	 */
	PublicParam getPublicParam(String orderNumber, String msgType, MerchantPayChannel merchantPayChannel);

	/**
	 * 退款申请
	 *
	 * @param refundDTO
	 * @return
	 */
	RefundResult refundApply(RefundDTO refundDTO);

	/******************************* 模板工具方法 *****************************/
	default Integer count(Consumer<QueryWrapper<PayTradeOrder>> wrapperBuilder) {
		QueryWrapper<PayTradeOrder> query = Wrappers.<PayTradeOrder>query();
		if (null != wrapperBuilder) {
			wrapperBuilder.accept(query);
		}
		return count(query);
	}

	default PayTradeOrder getOne(Consumer<QueryWrapper<PayTradeOrder>> wrapperBuilder) {
		QueryWrapper<PayTradeOrder> query = Wrappers.<PayTradeOrder>query();
		if (null != wrapperBuilder) {
			wrapperBuilder.accept(query);
		}
		return getOne(query);
	}
}
