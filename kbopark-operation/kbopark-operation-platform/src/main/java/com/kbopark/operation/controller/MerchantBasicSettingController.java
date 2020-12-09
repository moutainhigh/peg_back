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
import com.kbopark.operation.entity.MerchantBasicSetting;
import com.kbopark.operation.service.MerchantBasicSettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 商家基础设置
 *
 * @author pigx code generator
 * @date 2020-10-09 11:35:34
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchantbasicsetting" )
@Api(value = "merchantbasicsetting", tags = "商家基础设置管理")
public class MerchantBasicSettingController {

    private final  MerchantBasicSettingService merchantBasicSettingService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param merchantBasicSetting 商家基础设置
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('operation_merchantbasicsetting_edit')" )
    public R getMerchantBasicSettingPage(Page page, MerchantBasicSetting merchantBasicSetting) {
        return R.ok(merchantBasicSettingService.page(page, Wrappers.query(merchantBasicSetting)));
    }


    /**
     * 通过id查询商家基础设置
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_merchantbasicsetting_edit')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(merchantBasicSettingService.getById(id));
    }

    /**
     * 新增商家基础设置
     * @param merchantBasicSetting 商家基础设置
     * @return R
     */
    @ApiOperation(value = "新增商家基础设置", notes = "新增商家基础设置")
    @SysLog("新增商家基础设置" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('operation_merchantbasicsetting_edit')" )
    public R save(@RequestBody MerchantBasicSetting merchantBasicSetting) {
        return R.ok(merchantBasicSettingService.saveOrUpdate(merchantBasicSetting));
    }

    /**
     * 通过id删除商家基础设置
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商家基础设置", notes = "通过id删除商家基础设置")
    @SysLog("通过id删除商家基础设置" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_merchantbasicsetting_edit')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(merchantBasicSettingService.removeById(id));
    }


	/**
	 * 通过商家ID查询设置信息
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过商家ID查询设置信息", notes = "通过商家ID查询设置信息")
	@SysLog("通过商家ID查询设置信息" )
	@GetMapping("/merchant/{id}" )
	public R findByMerchantId(@PathVariable Integer id) {
		QueryWrapper<MerchantBasicSetting> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(MerchantBasicSetting::getMerchantId, id);
		return R.ok(merchantBasicSettingService.getOne(queryWrapper, false));
	}

}
