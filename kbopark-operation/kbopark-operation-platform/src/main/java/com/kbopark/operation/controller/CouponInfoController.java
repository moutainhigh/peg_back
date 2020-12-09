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

import cn.hutool.core.util.StrUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.dto.CouponReviewParam;
import com.kbopark.operation.entity.CouponInfo;
import com.kbopark.operation.enums.CouponAuditStatusEnum;
import com.kbopark.operation.service.CouponInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 优惠券表
 *
 * @author pigx code generator
 * @date 2020-08-28 16:15:36
 */
@RestController
@AllArgsConstructor
@RequestMapping("/couponinfo")
@Api(value = "couponinfo", tags = "【优惠券表管理】")
public class CouponInfoController {

	private final CouponInfoService couponInfoService;

	/**
	 * 分页查询
	 *
	 * @param page       分页对象
	 * @param couponInfo 优惠券表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_couponinfo_view','operation_redpack_view')")
	public R getCouponInfoPage(Page page, CouponInfo couponInfo) {
		if (StrUtil.isBlank(couponInfo.getLockFlag())) {
			couponInfo.setLockFlag(null);
		}
		return couponInfoService.getPage(page, couponInfo);
	}


	/**
	 * 当前商家的优惠券列表
	 *
	 * @return
	 */
	@ApiOperation(value = "当前商家的优惠券列表", notes = "当前商家的优惠券列表")
	@GetMapping("/list")
	@PreAuthorize("@pms.hasPermission('operation_couponinfo_view','operation_redpack_view')")
	public R getCouponInfoList() {
		KboparkUser user = SecurityUtils.getUser();
		if (user.getMerchantId() == null) {
			return R.ok();
		}
		QueryWrapper<CouponInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(CouponInfo::getMerchantId, user.getMerchantId())
				.eq(CouponInfo::getAuditStatus, CouponAuditStatusEnum.SUCCESS.getCode());
		return R.ok(couponInfoService.list(queryWrapper));
	}


	/**
	 * 通过id查询优惠券表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_couponinfo_view','operation_redpack_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(couponInfoService.getById(id));
	}

	/**
	 * 新增优惠券表
	 *
	 * @param couponInfo 优惠券表
	 * @return R
	 */
	@ApiOperation(value = "新增优惠券表", notes = "新增优惠券表")
	@SysLog("新增优惠券表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_couponinfo_add','operation_redpack_add')")
	public R save(@RequestBody CouponInfo couponInfo) {
		return couponInfoService.saveAndCheck(couponInfo);
	}

	/**
	 * 修改优惠券表
	 *
	 * @param couponInfo 优惠券表
	 * @return R
	 */
	@ApiOperation(value = "修改优惠券", notes = "修改优惠券表")
	@SysLog("修改优惠券表")
	@PostMapping("/edit")
	@PreAuthorize("@pms.hasPermission('operation_couponinfo_edit','operation_redpack_edit')")
	public R editCouponStatus(@RequestBody CouponInfo couponInfo) {
		return couponInfoService.editCouponStatus(couponInfo);
	}

	/**
	 * 修改优惠券表状态
	 *
	 * @param couponInfo 优惠券表
	 * @return R
	 */
	@ApiOperation(value = "修改优惠券表状态", notes = "修改优惠券表")
	@SysLog("修改优惠券表状态")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_couponinfo_edit'," +
			"'operation_couponinfo_audit'," +
			"'operation_couponinfo_up'," +
			"'operation_couponinfo_down'," +
			"'operation_couponinfo_lock'," +
			"'operation_couponinfo_unlock')")
	public R updateById(@RequestBody CouponInfo couponInfo) {
		couponInfo.setUpdateTime(LocalDateTime.now());
		return R.ok(couponInfoService.updateById(couponInfo));
	}

	@ApiOperation("设置权重")
	@SysLog("设置优惠券的权重")
	@PutMapping("/set-weight/{couponId}/{weight}")
	public R<Boolean> setWeight(@PathVariable("couponId") Integer couponId, @PathVariable("weight") Integer weight) {
		return R.ok(couponInfoService.setWeight(couponId, weight));
	}

	/**
	 * 修改红包状态
	 *
	 * @param couponInfo 红包信息
	 * @return R
	 */
	@ApiOperation(value = "修改红包状态", notes = "修改红包信息")
	@SysLog("修改红包状态")
	@PostMapping("/redpack")
	@PreAuthorize("@pms.hasPermission('operation_redpack_edit'," +
			"'operation_redpack_audit'," +
			"'operation_redpack_up'," +
			"'operation_redpack_down'," +
			"'operation_redpack_lock'," +
			"'operation_redpack_unlock')")
	public R updateRedPackById(@RequestBody CouponInfo couponInfo) {
		couponInfo.setUpdateTime(LocalDateTime.now());
		return R.ok(couponInfoService.updateById(couponInfo));
	}


	/**
	 * 审核更新
	 *
	 * @return
	 */
	@ApiOperation(value = "审核更新优惠券", notes = "审核更新优惠券")
	@SysLog("审核更新优惠券")
	@PostMapping("/audit")
	@PreAuthorize("@pms.hasPermission('operation_couponinfo_audit','operation_redpack_audit')")
	public R auditById(@RequestBody CouponReviewParam param) {
		return couponInfoService.reviewAndLog(param);
	}

	/**
	 * 通过id删除优惠券表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除优惠券表", notes = "通过id删除优惠券表")
	@SysLog("通过id删除优惠券表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_couponinfo_del','operation_redpack_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(couponInfoService.removeById(id));
	}

}
