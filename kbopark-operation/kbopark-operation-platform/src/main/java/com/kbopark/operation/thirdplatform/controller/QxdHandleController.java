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

package com.kbopark.operation.thirdplatform.controller;

import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.thirdplatform.utils.XmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 接收青小岛订单通知信息。核销，确认等
 *
 * @author pigx code generator
 * @date 2020-09-03 10:17:02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/orderAdvise")
@Api(value = "orderAdvise", tags = "接收青小岛订单通知信息")
@Slf4j
public class QxdHandleController {


	/**
	 * 接收青小岛订单核销通知
	 *
	 * @param param 参数
	 */
	@ApiOperation(value = "接收青小岛订单核销通知", notes = "接收青小岛订单核销通知")
	@PostMapping("/used")
	@Inner(value = false)
	public String getByOrderNumber(@RequestBody String param) {
		log.info(">>>青小岛订单核销通知："+ param);
		return XmlUtil.getResultXml(true);
	}




}
