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

package com.kbopark.kbpay.controller;

import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.kbpay.dto.PayParam;
import com.kbopark.kbpay.entity.PayTradeOrder;
import com.kbopark.kbpay.paysdk.PayResult;
import com.kbopark.kbpay.service.PayTradeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 支付订单
 *
 * @author laomst
 * @date 2020-09-03 16:55:15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/paytradeorder" )
@Api(value = "paytradeorder", tags = "支付订单管理")
public class PayTradeOrderController {

    private final  PayTradeOrderService payTradeOrderService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param payTradeOrder 支付订单
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('kbpay_paytradeorder_view')" )
    public R getPayTradeOrderPage(Page page, PayTradeOrder payTradeOrder) {
        return R.ok(payTradeOrderService.page(page, Wrappers.query(payTradeOrder)));
    }


    /**
     * 通过id查询支付订单
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('kbpay_paytradeorder_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(payTradeOrderService.getById(id));
    }

    /**
     * 新增支付订单
     * @param payTradeOrder 支付订单
     * @return R
     */
    @ApiOperation(value = "新增支付订单", notes = "新增支付订单")
    @SysLog("新增支付订单" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('kbpay_paytradeorder_add')" )
    public R save(@RequestBody PayTradeOrder payTradeOrder) {
        return R.ok(payTradeOrderService.save(payTradeOrder));
    }

    /**
     * 修改支付订单
     * @param payTradeOrder 支付订单
     * @return R
     */
    @ApiOperation(value = "修改支付订单", notes = "修改支付订单")
    @SysLog("修改支付订单" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('kbpay_paytradeorder_edit')" )
    public R updateById(@RequestBody PayTradeOrder payTradeOrder) {
        return R.ok(payTradeOrderService.updateById(payTradeOrder));
    }

    /**
     * 通过id删除支付订单
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除支付订单", notes = "通过id删除支付订单")
    @SysLog("通过id删除支付订单" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('kbpay_paytradeorder_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(payTradeOrderService.removeById(id));
    }


	/**
	 * 获取支付链接（统一下单）
	 * @param param
	 * @return
	 */
	@PostMapping("/js-pay")
	@Inner(value = false)
	public R<? extends PayResult> jsPay(@RequestBody PayParam param) {
		try {
			return R.ok(payTradeOrderService.jsPay(param));
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
	}

}
