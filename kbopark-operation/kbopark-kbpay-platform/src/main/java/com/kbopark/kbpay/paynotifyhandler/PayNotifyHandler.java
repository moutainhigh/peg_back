/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.kbopark.kbpay.paynotifyhandler;

import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.kbopark.kbpay.paynotifyhandler.service.UmsNotifyHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付订单 控制器
 *
 * @author BladeX
 * @since 2020-07-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/pay-notify")
@Api(value = "支付订单", tags = "支付订单接口")
public class PayNotifyHandler {

	private final UmsNotifyHandleService umsNotifyHandleService;

	@PostMapping("/ums-notify")
	@ApiOperation("银联支付回调")
	@Inner(value = false)
	public String umsNotifyHandler(@RequestBody String body) {
		System.out.println("================================================== 开始处理回调 ==================================================================");
		String res = umsNotifyHandleService.handle(body);
		System.out.println("================================================== 开始处理回调 ==================================================================");
		return res;
	}

}
