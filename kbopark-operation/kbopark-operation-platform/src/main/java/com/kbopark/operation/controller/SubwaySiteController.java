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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.entity.SubwayLine;
import com.kbopark.operation.entity.SubwaySite;
import com.kbopark.operation.service.SubwaySiteService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author pigx code generator
 * @date 2020-08-27 11:28:52
 */
@RestController
@AllArgsConstructor
@RequestMapping("/subwaysite")
@Api(value = "subwaysite", tags = "站点管理")
public class SubwaySiteController {

	private final SubwaySiteService subwaySiteService;

	/**
	 * 分页查询
	 *
	 * @param page       分页对象
	 * @param subwaySite
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_subwaysite_view')")
	public R getSubwaySitePage(Page page, SubwaySite subwaySite) {
		return R.ok(subwaySiteService.page(page, Wrappers.query(subwaySite).lambda().orderByAsc(SubwaySite::getSortNumber)));
	}


	/**
	 * 通过id查询
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_subwaysite_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(subwaySiteService.getById(id));
	}

	/**
	 * 新增
	 *
	 * @param subwaySite
	 * @return R
	 */
	@ApiOperation(value = "新增", notes = "新增")
	@SysLog("新增")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_subwaysite_add')")
	public R save(@RequestBody SubwaySite subwaySite) {
		List<SubwaySite> list = subwaySiteService.list(Wrappers.<SubwaySite>lambdaQuery()
				.eq(SubwaySite::getName, subwaySite.getName())
				.eq(SubwaySite::getLineCode, subwaySite.getLineCode()));
		if (!CollectionUtils.isEmpty(list)) {
			return R.failed("站点名称已存在");
		}
		//站点编号
		subwaySite.setSiteCode(RandomUtil.randomStringUpper(12));
		return R.ok(subwaySiteService.save(subwaySite));
	}

	/**
	 * 修改
	 *
	 * @param subwaySite
	 * @return R
	 */
	@ApiOperation(value = "修改", notes = "修改")
	@SysLog("修改")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_subwaysite_edit')")
	public R updateById(@RequestBody SubwaySite subwaySite) {
		List<SubwaySite> list = subwaySiteService.list(Wrappers.<SubwaySite>lambdaQuery()
				.ne(SubwaySite::getId, subwaySite.getId())
				.eq(SubwaySite::getName, subwaySite.getName())
				.eq(SubwaySite::getLineCode, subwaySite.getLineCode()));
		if (!CollectionUtils.isEmpty(list)) {
			return R.failed("站点名称已存在");
		}
		return R.ok(subwaySiteService.updateById(subwaySite));
	}

	/**
	 * 通过id删除
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除", notes = "通过id删除")
	@SysLog("通过id删除")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_subwaysite_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(subwaySiteService.deleteById(id));
	}


	/**
	 * 根据线路编号查询站点列表
	 * @return
	 */
	@ApiOperation(value = "根据线路编号查询站点列表", notes = "根据线路编号查询站点列表")
	@GetMapping("/list/{code}")
	public R getSubwaySiteList(@PathVariable String code) {
		LambdaQueryWrapper<SubwaySite> queryWrapper = Wrappers.<SubwaySite>lambdaQuery().orderByAsc(SubwaySite::getSortNumber);
		queryWrapper.eq(SubwaySite::getLineCode, code);
		return R.ok(subwaySiteService.list(queryWrapper));
	}

}
