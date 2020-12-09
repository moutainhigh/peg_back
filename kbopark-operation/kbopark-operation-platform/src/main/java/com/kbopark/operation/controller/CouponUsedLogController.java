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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.dto.CouponUsedDTO;
import com.kbopark.operation.entity.CouponUsedLog;
import com.kbopark.operation.service.CouponUsedLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 优惠券使用记录
 *
 * @author pigx code generator
 * @date 2020-09-04 10:15:04
 */
@RestController
@AllArgsConstructor
@RequestMapping("/couponusedlog")
@Api(value = "couponusedlog", tags = "优惠券使用记录管理")
public class CouponUsedLogController {

	private final CouponUsedLogService couponUsedLogService;

	/**
	 * 分页查询
	 *
	 * @param page          分页对象
	 * @param couponUsedLog 优惠券使用记录
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_couponusedlog_view')")
	public R getCouponUsedLogPage(Page page, CouponUsedLog couponUsedLog) {
		return R.ok(couponUsedLogService.page(page, Wrappers.query(couponUsedLog)));
	}


	/**
	 * 通过id查询优惠券使用记录
	 *
	 * @param recordId id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{recordId}")
	@PreAuthorize("@pms.hasPermission('operation_couponusedlog_view')")
	public R getById(@PathVariable("recordId") Integer recordId) {
		return R.ok(couponUsedLogService.getById(recordId));
	}

	/**
	 * 新增优惠券使用记录
	 *
	 * @param couponUsedLog 优惠券使用记录
	 * @return R
	 */
	@ApiOperation(value = "新增优惠券使用记录", notes = "新增优惠券使用记录")
	@SysLog("新增优惠券使用记录")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_couponusedlog_add')")
	public R save(@RequestBody CouponUsedLog couponUsedLog) {
		return R.ok(couponUsedLogService.save(couponUsedLog));
	}

	/**
	 * 修改优惠券使用记录
	 *
	 * @param couponUsedLog 优惠券使用记录
	 * @return R
	 */
	@ApiOperation(value = "修改优惠券使用记录", notes = "修改优惠券使用记录")
	@SysLog("修改优惠券使用记录")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_couponusedlog_edit')")
	public R updateById(@RequestBody CouponUsedLog couponUsedLog) {
		return R.ok(couponUsedLogService.updateById(couponUsedLog));
	}

	/**
	 * 通过id删除优惠券使用记录
	 *
	 * @param recordId id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除优惠券使用记录", notes = "通过id删除优惠券使用记录")
	@SysLog("通过id删除优惠券使用记录")
	@DeleteMapping("/{recordId}")
	@PreAuthorize("@pms.hasPermission('operation_couponusedlog_del')")
	public R removeById(@PathVariable Integer recordId) {
		return R.ok(couponUsedLogService.removeById(recordId));
	}


	/**
	 * 分页查询使用记录
	 *
	 * @param page
	 * @param couponUsedDTO
	 * @return
	 */
	@ApiOperation(value = "查询使用记录", notes = "查询使用记录")
	@SysLog("查询使用记录")
	@GetMapping("/usedRecord")
	public R getUsedRecord(Page page, CouponUsedDTO couponUsedDTO) {
		QueryWrapper<CouponUsedLog> query = Wrappers.query(couponUsedDTO);
		return R.ok(couponUsedLogService.page(page, query));
	}

}
