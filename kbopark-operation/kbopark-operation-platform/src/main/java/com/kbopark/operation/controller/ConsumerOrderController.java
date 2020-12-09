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

package com.kbopark.operation.controller;

import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.dto.ConsumerOrderDTO;
import com.kbopark.operation.dto.CreateOrderDTO;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.service.ConsumerOrderService;
import com.kbopark.operation.service.MerchantService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 消费订单
 *
 * @author pigx code generator
 * @date 2020-09-03 10:17:02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/consumerorder")
@Api(value = "consumerorder", tags = "消费订单管理")
public class ConsumerOrderController {

	private final ConsumerOrderService consumerOrderService;

	private final MerchantService merchantService;

	/**
	 * 分页查询
	 *
	 * @param page             分页对象
	 * @param consumerOrderDTO 消费订单
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_consumerorder_view')")
	public R getConsumerOrderPage(Page page, ConsumerOrderDTO consumerOrderDTO) {
		return consumerOrderService.getOrderPage(page, consumerOrderDTO);
	}

	/**
	 * 通过订单号查询订单信息
	 *
	 * @param orderNumber 订单号
	 * @return R
	 */
	@ApiOperation(value = "通过订单号查询", notes = "通过订单号查询")
	@GetMapping("/info/{orderNumber}")
	@Inner(value = false)
	public R<ConsumerOrder> getByOrderNumber(@PathVariable("orderNumber") String orderNumber) {
		ConsumerOrder consumerOrder = consumerOrderService.findByOrderNumber(orderNumber);
		if(consumerOrder != null){
			Merchant merchant = merchantService.getById(consumerOrder.getMerchantId());
			consumerOrder.setMerchantCity(merchant.getProvinceName() + merchant.getCityName() + merchant.getAreaName());
			consumerOrder.setMerchantAddress(merchant.getAddress());
			consumerOrder.setMerchantLogo(merchant.getLogo());
		}
		return R.ok(consumerOrder);
	}


	/**
	 * 通过id查询消费订单
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_consumerorder_view')")
	public R getById(@PathVariable("id") Integer id) {
		return consumerOrderService.getOrderById(id);
	}


	/**
	 * 订单完成异步通知
	 *
	 * @param orderNumber 消费订单号
	 * @return R
	 */
	@ApiOperation(value = "订单完成异步通知", notes = "订单完成异步通知")
	@SysLog("订单完成异步通知")
	@GetMapping("/notify/{orderNumber}")
	@Inner(value = false)
	public R notifyOrder(@PathVariable("orderNumber") String orderNumber) {
		return consumerOrderService.notifyChangeStatus(orderNumber);
	}

}
