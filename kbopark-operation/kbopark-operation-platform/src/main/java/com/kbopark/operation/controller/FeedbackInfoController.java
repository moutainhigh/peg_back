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
import com.kbopark.operation.entity.FeedbackInfo;
import com.kbopark.operation.service.FeedbackInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 用户反馈信息
 *
 * @author pigx code generator
 * @date 2020-10-12 08:51:25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/feedbackinfo")
@Api(value = "feedbackinfo", tags = "用户反馈信息管理")
public class FeedbackInfoController {

	private final FeedbackInfoService feedbackInfoService;

	/**
	 * 分页查询
	 *
	 * @param page         分页对象
	 * @param feedbackInfo 用户反馈信息
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_feedbackinfo_view')")
	public R getFeedbackInfoPage(Page page, FeedbackInfo feedbackInfo) {
		return R.ok(feedbackInfoService.page(page, Wrappers.query(feedbackInfo)));
	}


	/**
	 * 通过id查询用户反馈信息
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_feedbackinfo_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(feedbackInfoService.getById(id));
	}

	/**
	 * 新增用户反馈信息
	 *
	 * @param feedbackInfo 用户反馈信息
	 * @return R
	 */
	@ApiOperation(value = "新增用户反馈信息", notes = "新增用户反馈信息")
	@SysLog("新增用户反馈信息")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_feedbackinfo_add')")
	public R save(@RequestBody FeedbackInfo feedbackInfo) {
		return R.ok(feedbackInfoService.save(feedbackInfo));
	}

	/**
	 * 修改用户反馈信息
	 *
	 * @param feedbackInfo 用户反馈信息
	 * @return R
	 */
	@ApiOperation(value = "修改用户反馈信息", notes = "修改用户反馈信息")
	@SysLog("修改用户反馈信息")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_feedbackinfo_edit')")
	public R updateById(@RequestBody FeedbackInfo feedbackInfo) {
		return R.ok(feedbackInfoService.updateById(feedbackInfo));
	}

	/**
	 * 通过id删除用户反馈信息
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除用户反馈信息", notes = "通过id删除用户反馈信息")
	@SysLog("通过id删除用户反馈信息")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_feedbackinfo_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(feedbackInfoService.removeById(id));
	}

}
