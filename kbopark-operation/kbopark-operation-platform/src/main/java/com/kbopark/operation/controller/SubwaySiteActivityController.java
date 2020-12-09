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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.entity.SubwaySiteActivity;
import com.kbopark.operation.service.SubwaySiteActivityService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 地铁站点活动设置
 *
 * @author pigx code generator
 * @date 2020-09-02 10:10:33
 */
@RestController
@AllArgsConstructor
@RequestMapping("/subwaysiteactivity")
@Api(value = "subwaysiteactivity", tags = "地铁站点活动设置管理")
public class SubwaySiteActivityController {

	private final SubwaySiteActivityService subwaySiteActivityService;

	/**
	 * 分页查询
	 *
	 * @param page               分页对象
	 * @param subwaySiteActivity 地铁站点活动设置
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_subwaysiteactivity_setting')")
	public R getSubwaySiteActivityPage(Page page, SubwaySiteActivity subwaySiteActivity) {
		return R.ok(subwaySiteActivityService.page(page, Wrappers.query(subwaySiteActivity)));
	}


	/**
	 * 通过id查询地铁站点活动设置
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_subwaysiteactivity_setting')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(subwaySiteActivityService.getById(id));
	}

	/**
	 * 通过code查询地铁站点活动设置
	 *
	 * @param code code
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/code/{code}")
	@PreAuthorize("@pms.hasPermission('operation_subwaysiteactivity_setting')")
	public R getByCode(@PathVariable("code") String code) {

		return R.ok(subwaySiteActivityService.findBySiteCode(code));
	}

	/**
	 * 新增地铁站点活动设置
	 *
	 * @param subwaySiteActivity 地铁站点活动设置
	 * @return R
	 */
	@ApiOperation(value = "新增地铁站点活动设置", notes = "新增地铁站点活动设置")
	@SysLog("新增地铁站点活动设置")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_subwaysiteactivity_setting')")
	public R save(@RequestBody SubwaySiteActivity subwaySiteActivity) {
		SubwaySiteActivity bySiteCode = subwaySiteActivityService.findBySiteCode(subwaySiteActivity.getSiteCode());
		if(bySiteCode != null){
			subwaySiteActivity.setId(bySiteCode.getId());
		}
		subwaySiteActivity.setUpdateTime(LocalDateTime.now());
		if(subwaySiteActivity.getMerchantDistance() == null){
			subwaySiteActivity.setMerchantDistance(0.0);
		}
		if(subwaySiteActivity.getMerchantNumber() == null){
			subwaySiteActivity.setMerchantNumber(0);
		}
		if(subwaySiteActivity.getRedpackDistance() == null){
			subwaySiteActivity.setRedpackDistance(0.0);
		}
		if(subwaySiteActivity.getRedpackNumber() == null){
			subwaySiteActivity.setRedpackNumber(0);
		}
		if(subwaySiteActivity.getCouponDistance() == null){
			subwaySiteActivity.setCouponDistance(0.0);
		}
		if(subwaySiteActivity.getCouponNumber() == null){
			subwaySiteActivity.setCouponNumber(0);
		}
		return R.ok(subwaySiteActivityService.saveOrUpdate(subwaySiteActivity));
	}

	/**
	 * 修改地铁站点活动设置
	 *
	 * @param subwaySiteActivity 地铁站点活动设置
	 * @return R
	 */
	@ApiOperation(value = "修改地铁站点活动设置", notes = "修改地铁站点活动设置")
	@SysLog("修改地铁站点活动设置")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_subwaysiteactivity_setting')")
	public R updateById(@RequestBody SubwaySiteActivity subwaySiteActivity) {
		return R.ok(subwaySiteActivityService.updateById(subwaySiteActivity));
	}

	/**
	 * 通过id删除地铁站点活动设置
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除地铁站点活动设置", notes = "通过id删除地铁站点活动设置")
	@SysLog("通过id删除地铁站点活动设置")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_subwaysiteactivity_setting')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(subwaySiteActivityService.removeById(id));
	}

}
