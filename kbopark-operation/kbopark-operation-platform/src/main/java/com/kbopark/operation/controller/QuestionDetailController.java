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
import com.kbopark.operation.entity.QuestionDetail;
import com.kbopark.operation.service.QuestionDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 问题解答详情
 *
 * @author pigx code generator
 * @date 2020-10-21 11:23:27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/questiondetail")
@Api(value = "questiondetail", tags = "问题解答详情管理")
public class QuestionDetailController {

	private final QuestionDetailService questionDetailService;

	/**
	 * 分页查询
	 *
	 * @param page           分页对象
	 * @param questionDetail 问题解答详情
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R getQuestionDetailPage(Page page, QuestionDetail questionDetail) {
		return R.ok(questionDetailService.page(page, Wrappers.query(questionDetail)));
	}


	/**
	 * 通过id查询问题解答详情
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_questiondetail_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(questionDetailService.getById(id));
	}

	/**
	 * 新增问题解答详情
	 *
	 * @param questionDetail 问题解答详情
	 * @return R
	 */
	@ApiOperation(value = "新增问题解答详情", notes = "新增问题解答详情")
	@SysLog("新增问题解答详情")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_questiondetail_add')")
	public R save(@RequestBody QuestionDetail questionDetail) {
		return R.ok(questionDetailService.save(questionDetail));
	}

	/**
	 * 修改问题解答详情
	 *
	 * @param questionDetail 问题解答详情
	 * @return R
	 */
	@ApiOperation(value = "修改问题解答详情", notes = "修改问题解答详情")
	@SysLog("修改问题解答详情")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_questiondetail_edit')")
	public R updateById(@RequestBody QuestionDetail questionDetail) {
		return R.ok(questionDetailService.updateById(questionDetail));
	}

	/**
	 * 通过id删除问题解答详情
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除问题解答详情", notes = "通过id删除问题解答详情")
	@SysLog("通过id删除问题解答详情")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_questiondetail_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(questionDetailService.removeById(id));
	}

}
