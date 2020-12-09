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
import com.kbopark.operation.entity.MerchantSubwayRel;
import com.kbopark.operation.service.MerchantSubwayRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 商家和地铁线关联关系表
 *
 * @author pigx code generator
 * @date 2020-10-12 16:12:42
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchantsubwayrel")
@Api(value = "merchantsubwayrel", tags = "商家和地铁线关联关系表管理")
public class MerchantSubwayRelController {

	private final MerchantSubwayRelService merchantSubwayRelService;

	/**
	 * 分页查询
	 *
	 * @param page              分页对象
	 * @param merchantSubwayRel 商家和地铁线关联关系表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R getMerchantSubwayRelPage(Page page, MerchantSubwayRel merchantSubwayRel) {
		return R.ok(merchantSubwayRelService.page(page, Wrappers.query(merchantSubwayRel)));
	}


	/**
	 * 通过商家id查询商家和地铁线关联关系表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过商家id查询", notes = "通过商家id查询")
	@GetMapping("/merchant/{id}")
	public R getById(@PathVariable("id") Integer id) {
		QueryWrapper<MerchantSubwayRel> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(MerchantSubwayRel::getMerchantId, id);
		return R.ok(merchantSubwayRelService.getOne(queryWrapper, false));
	}

	/**
	 * 新增商家和地铁线关联关系表
	 *
	 * @param merchantSubwayRel 商家和地铁线关联关系表
	 * @return R
	 */
	@ApiOperation(value = "新增商家和地铁线关联关系表", notes = "新增商家和地铁线关联关系表")
	@SysLog("新增商家和地铁线关联关系表")
	@PostMapping
	public R saveOrUpdate(@RequestBody MerchantSubwayRel merchantSubwayRel) {
		return R.ok(merchantSubwayRelService.saveOrUpdate(merchantSubwayRel));
	}

	/**
	 * 通过id删除商家和地铁线关联关系表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除商家和地铁线关联关系表", notes = "通过id删除商家和地铁线关联关系表")
	@SysLog("通过id删除商家和地铁线关联关系表")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Integer id) {
		return R.ok(merchantSubwayRelService.removeById(id));
	}

}
