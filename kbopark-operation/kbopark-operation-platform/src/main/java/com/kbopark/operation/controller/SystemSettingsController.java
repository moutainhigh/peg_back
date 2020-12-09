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
import com.kbopark.operation.entity.SystemSettings;
import com.kbopark.operation.service.SystemSettingsService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 系统设置
 *
 * @author laomst
 * @date 2020-09-01 14:26:51
 */
@RestController
@AllArgsConstructor
@RequestMapping("/systemsettings" )
@Api(value = "systemsettings", tags = "系统设置管理")
public class SystemSettingsController {

    private final  SystemSettingsService systemSettingsService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param systemSettings 系统设置
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
//    @PreAuthorize("@pms.hasPermission('operation_systemsettings_view')" )
    public R getSystemSettingsPage(Page page, SystemSettings systemSettings) {
        return R.ok(systemSettingsService.page(page, Wrappers.query(systemSettings)));
    }


    /**
     * 通过id查询系统设置
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
//    @PreAuthorize("@pms.hasPermission('operation_systemsettings_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(systemSettingsService.getById(id));
    }
    @ApiOperation(value = "通过id查询", notes = "通过id查询")


    @GetMapping("get-system-settings" )
//    @PreAuthorize("@pms.hasPermission('operation_systemsettings_view')" )
    public R getSystemSettings() {
		SystemSettings entity = systemSettingsService.getById(1);
		return R.ok(entity);
    }

    /**
     * 新增系统设置
     * @param systemSettings 系统设置
     * @return R
     */
    @ApiOperation(value = "新增系统设置", notes = "新增系统设置")
    @SysLog("新增系统设置" )
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('operation_systemsettings_add')" )
    public R save(@RequestBody SystemSettings systemSettings) {
        return R.ok(systemSettingsService.save(systemSettings));
    }

    /**
     * 修改系统设置
     * @param systemSettings 系统设置
     * @return R
     */
    @ApiOperation(value = "修改系统设置", notes = "修改系统设置")
    @SysLog("修改系统设置" )
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('operation_systemsettings_edit')" )
    public R updateById(@RequestBody SystemSettings systemSettings) {
        return R.ok(systemSettingsService.updateById(systemSettings));
    }

    /**
     * 通过id删除系统设置
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除系统设置", notes = "通过id删除系统设置")
    @SysLog("通过id删除系统设置" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_systemsettings_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(systemSettingsService.removeById(id));
    }

}
