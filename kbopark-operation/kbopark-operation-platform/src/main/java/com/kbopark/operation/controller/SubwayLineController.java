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
import com.kbopark.operation.service.SubwayLineService;
import com.kbopark.operation.service.SubwaySiteService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author pigx code generator
 * @date 2020-08-27 11:30:36
 */
@RestController
@AllArgsConstructor
@RequestMapping("/subwayline")
@Api(value = "subwayline", tags = "管理")
public class SubwayLineController {

	private final SubwayLineService subwayLineService;

	private final SubwaySiteService subwaySiteService;

	/**
	 * 分页查询
	 *
	 * @param page       分页对象
	 * @param subwayLine
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_subwayline_view')")
	public R getSubwayLinePage(Page page, SubwayLine subwayLine) {
		return R.ok(subwayLineService.page(page, Wrappers.query(subwayLine).lambda().orderByAsc(SubwayLine::getSortNumber)));
	}


	/**
	 * 查询列表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/list")
	public R getSubwayLineList() {
		LambdaQueryWrapper<SubwayLine> queryWrapper = Wrappers.<SubwayLine>lambdaQuery().orderByAsc(SubwayLine::getSortNumber);
		return R.ok(subwayLineService.list(queryWrapper));
	}


	/**
	 * 通过id查询
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_subwayline_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(subwayLineService.getById(id));
	}

	/**
	 * 新增
	 *
	 * @param subwayLine
	 * @return R
	 */
	@ApiOperation(value = "新增", notes = "新增")
	@SysLog("新增")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_subwayline_add')")
	public R save(@RequestBody SubwayLine subwayLine) {
		List<SubwayLine> list = subwayLineService.list(Wrappers.<SubwayLine>lambdaQuery().eq(SubwayLine::getName, subwayLine.getName()));
		if(!CollectionUtils.isEmpty(list)){
			return R.failed("线路名称已存在");
		}
		subwayLine.setCode(RandomUtil.randomStringUpper(8));
		return R.ok(subwayLineService.save(subwayLine));
	}

	/**
	 * 修改
	 *
	 * @param subwayLine
	 * @return R
	 */
	@ApiOperation(value = "修改", notes = "修改")
	@SysLog("修改")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_subwayline_edit')")
	public R updateById(@RequestBody SubwayLine subwayLine) {
		List<SubwayLine> list = subwayLineService.list(Wrappers.<SubwayLine>lambdaQuery()
				.ne(SubwayLine::getId, subwayLine.getId())
				.eq(SubwayLine::getName, subwayLine.getName()));
		if(!CollectionUtils.isEmpty(list)){
			return R.failed("线路名称已存在");
		}
		return R.ok(subwayLineService.updateById(subwayLine));
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
	@PreAuthorize("@pms.hasPermission('operation_subwayline_del')")
	public R removeById(@PathVariable Integer id) {
		SubwayLine line = subwayLineService.getById(id);
		if(line == null){
			return R.failed("地铁线路不存在");
		}
		List<SubwaySite> list = subwaySiteService.list(Wrappers.<SubwaySite>lambdaQuery().eq(SubwaySite::getLineCode, line.getCode()));
		if(!CollectionUtils.isEmpty(list)){
			return R.failed("当前线路存在站点，不可删除");
		}

		return R.ok(subwayLineService.removeById(id));
	}

}
