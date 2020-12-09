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

package com.kbopark.kbpay.api;

import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.kbopark.kbpay.JPush.service.JPushService;
import com.kbopark.kbpay.dto.PayParam;
import com.kbopark.kbpay.dto.RefundDTO;
import com.kbopark.kbpay.paysdk.PayResult;
import com.kbopark.kbpay.service.PayTradeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * 支付订单
 *
 * @author laomst
 * @date 2020-09-03 16:55:15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/pay-api")
@Api(value = "pay-api", tags = "支付相关接口")
public class PayApi {

	private final PayTradeOrderService payTradeOrderService;

	private final JPushService pushService;

	/**
	 * 扫商家支付码下单请求支付链接<br>
	 * 接口开放
	 *
	 * @param param 参数是流水号
	 * @return
	 */
	@PostMapping("/js-pay")
	@ApiModelProperty("h5支付下单接口")
	@Inner(value = false)
	public R<? extends PayResult> jsPay(@RequestBody PayParam param) {
		try {
			return R.ok(payTradeOrderService.jsPay(param));
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
	}

	/***
	 * 退款申请
	 *
	 * @param refundDTO
	 * @return
	 */
	@PostMapping("/refund")
	@ApiModelProperty("退款申请")
	@Inner(value = false)
	public R refundApi(@RequestBody RefundDTO refundDTO) {
		try {
			return R.ok(payTradeOrderService.refundApply(refundDTO));
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
	}

}
