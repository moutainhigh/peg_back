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
import com.kbopark.operation.entity.MerchantBalance;
import com.kbopark.operation.service.MerchantBalanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * 商家资金表
 *
 * @author laosmt
 * @date 2020-10-29 09:41:02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchantbalance")
@Api(value = "merchantbalance", tags = "商家资金表管理")
public class MerchantBalanceController {

	private final MerchantBalanceService merchantBalanceService;

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param merchantBalance 商家资金表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
//    @PreAuthorize("@pms.hasPermission('operation_merchantbalance_view')" )
	public R getMerchantBalancePage(Page page, MerchantBalance merchantBalance) {
		return R.ok(merchantBalanceService.page(page, Wrappers.query(merchantBalance)));
	}

	/**
	 * 通过id查询商家资金表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_merchantbalance_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(merchantBalanceService.getById(id));
	}

	/**
	 * 新增商家资金表
	 *
	 * @param merchantBalance 商家资金表
	 * @return R
	 */
	@ApiOperation(value = "新增商家资金表", notes = "新增商家资金表")
	@SysLog("新增商家资金表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_merchantbalance_add')")
	public R save(@RequestBody MerchantBalance merchantBalance) {
		return R.ok(merchantBalanceService.save(merchantBalance));
	}

	/**
	 * 修改商家资金表
	 *
	 * @param merchantBalance 商家资金表
	 * @return R
	 */
	@ApiOperation(value = "修改商家资金表", notes = "修改商家资金表")
	@SysLog("修改商家资金表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_merchantbalance_edit')")
	public R updateById(@RequestBody MerchantBalance merchantBalance) {
		return R.ok(merchantBalanceService.updateById(merchantBalance));
	}

	/**
	 * 通过id删除商家资金表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除商家资金表", notes = "通过id删除商家资金表")
	@SysLog("通过id删除商家资金表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_merchantbalance_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(merchantBalanceService.removeById(id));
	}

}
