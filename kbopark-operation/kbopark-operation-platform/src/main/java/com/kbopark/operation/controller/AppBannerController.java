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
import com.kbopark.operation.entity.AppBanner;
import com.kbopark.operation.service.AppBannerService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 用户端首页图片关联
 *
 * @author laomst
 * @date 2020-09-30 11:38:14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/appbanner" )
@Api(value = "appbanner", tags = "用户端首页图片关联管理")
public class AppBannerController {

    private final AppBannerService appBannerService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param appBanner 用户端首页图片关联
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('operation_appbanner_view')" )
    public R getAppBannerPage(Page page, AppBanner appBanner) {
        return R.ok(appBannerService.page(page, Wrappers.query(appBanner).lambda().orderByAsc(AppBanner::getType, AppBanner::getSort)));
    }


    /**
     * 通过id查询用户端首页图片关联
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_appbanner_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(appBannerService.getById(id));
    }

    /**
     * 新增用户端首页图片关联
     * @param appBanner 用户端首页图片关联
     * @return R
     */
    @ApiOperation(value = "新增用户端首页图片关联", notes = "新增用户端首页图片关联")
    @SysLog("新增用户端首页图片关联" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('operation_appbanner_add')" )
    public R save(@RequestBody AppBanner appBanner) {
        return R.ok(appBannerService.save(appBanner));
    }

    /**
     * 修改用户端首页图片关联
     * @param appBanner 用户端首页图片关联
     * @return R
     */
    @ApiOperation(value = "修改用户端首页图片关联", notes = "修改用户端首页图片关联")
    @SysLog("修改用户端首页图片关联" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('operation_appbanner_edit')" )
    public R updateById(@RequestBody AppBanner appBanner) {
        return R.ok(appBannerService.updateById(appBanner));
    }

    /**
     * 通过id删除用户端首页图片关联
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除用户端首页图片关联", notes = "通过id删除用户端首页图片关联")
    @SysLog("通过id删除用户端首页图片关联" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_appbanner_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(appBannerService.removeById(id));
    }

}
