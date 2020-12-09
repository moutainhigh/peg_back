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

import cn.hutool.core.util.RandomUtil;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.entity.QuestionType;
import com.kbopark.operation.service.QuestionTypeService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 问题类型表
 *
 * @author pigx code generator
 * @date 2020-10-21 11:23:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/questiontype")
@Api(value = "questiontype", tags = "问题类型表管理")
public class QuestionTypeController {

	private final QuestionTypeService questionTypeService;

	/**
	 * 分页查询
	 *
	 * @param page         分页对象
	 * @param questionType 问题类型表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R getQuestionTypePage(Page page, QuestionType questionType) {
		return R.ok(questionTypeService.page(page, Wrappers.query(questionType)));
	}


	/**
	 * 通过id查询问题类型表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_questiontype_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(questionTypeService.getById(id));
	}

	/**
	 * 新增问题类型表
	 *
	 * @param questionType 问题类型表
	 * @return R
	 */
	@ApiOperation(value = "新增问题类型表", notes = "新增问题类型表")
	@SysLog("新增问题类型表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_questiontype_add')")
	public R save(@RequestBody QuestionType questionType) {
		questionType.setValue(RandomUtil.randomString(8));
		return R.ok(questionTypeService.save(questionType));
	}

	/**
	 * 修改问题类型表
	 *
	 * @param questionType 问题类型表
	 * @return R
	 */
	@ApiOperation(value = "修改问题类型表", notes = "修改问题类型表")
	@SysLog("修改问题类型表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_questiontype_edit')")
	public R updateById(@RequestBody QuestionType questionType) {
		return R.ok(questionTypeService.updateById(questionType));
	}

	/**
	 * 通过id删除问题类型表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除问题类型表", notes = "通过id删除问题类型表")
	@SysLog("通过id删除问题类型表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_questiontype_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(questionTypeService.removeById(id));
	}


	/***
	 * 问题类型字典
	 *
	 * @return
	 */
	@ApiOperation(value = "问题类型字典", notes = "问题类型字典")
	@SysLog("问题类型字典")
	@GetMapping("/list")
	@Inner(value = false)
	public R getTypeList() {
		return R.ok(questionTypeService.list());
	}



}
