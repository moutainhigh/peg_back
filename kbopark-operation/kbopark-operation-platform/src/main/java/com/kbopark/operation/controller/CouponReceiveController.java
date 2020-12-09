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

import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.dto.CouponReceiveDTO;
import com.kbopark.operation.entity.CouponReceive;
import com.kbopark.operation.service.CouponReceiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 优惠券领取记录
 *
 * @author pigx code generator
 * @date 2020-09-01 09:35:43
 */
@RestController
@AllArgsConstructor
@RequestMapping("/couponreceive")
@Api(value = "couponreceive", tags = "优惠券领取记录管理")
public class CouponReceiveController {

	private final CouponReceiveService couponReceiveService;

	/**
	 * 分页查询
	 *
	 * @param page             分页对象
	 * @param couponReceiveDTO 优惠券领取记录
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_couponreceive_view')")
	public R getCouponReceivePage(Page page, CouponReceiveDTO couponReceiveDTO) {

		return couponReceiveService.getPage(page, couponReceiveDTO);
	}


	/**
	 * 通过id查询优惠券领取记录
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_couponreceive_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(couponReceiveService.getById(id));
	}



	/**
	 * 修改优惠券领取记录
	 *
	 * @param couponReceive 优惠券领取记录
	 * @return R
	 */
	@ApiOperation(value = "修改优惠券领取记录", notes = "修改优惠券领取记录")
	@SysLog("修改优惠券领取记录")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_couponreceive_edit')")
	public R updateById(@RequestBody CouponReceive couponReceive) {
		return R.ok(couponReceiveService.updateById(couponReceive));
	}


	/**
	 * 领取优惠券，新增领取记录【优惠券和红包的领取使用此接口】
	 *
	 * @param couponReceiveDTO 优惠券领取记录
	 * @return R
	 */
	@ApiOperation(value = "新增优惠券领取记录", notes = "新增优惠券领取记录")
	@SysLog("新增优惠券领取记录")
	@PostMapping("/coupon")
	public R createReceiveRecord(@Valid @RequestBody CouponReceiveDTO couponReceiveDTO) {
		return couponReceiveService.receiveAndCreateRecord(couponReceiveDTO);
	}


	/**
	 * 领取乘车券接口，新增领取记录【乘车券的领取使用此接口】
	 *
	 * @param couponReceiveDTO 乘车券领取记录
	 * @return R
	 */
	@ApiOperation(value = "新增乘车券领取记录", notes = "新增乘车券领取记录")
	@SysLog("新增乘车券领取记录")
	@PostMapping("/ticket")
	public R createTicketReceiveRecord(@Valid @RequestBody CouponReceiveDTO couponReceiveDTO) {
		return couponReceiveService.receiveTicket(couponReceiveDTO);
	}

}
