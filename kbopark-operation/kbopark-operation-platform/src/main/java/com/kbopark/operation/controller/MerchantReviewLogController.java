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
import com.kbopark.operation.entity.MerchantReviewLog;
import com.kbopark.operation.service.MerchantReviewLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 商家审核记录
 *
 * @author laomst
 * @date 2020-08-28 23:33:55
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchantreviewlog" )
@Api(value = "merchantreviewlog", tags = "商家审核记录管理")
public class MerchantReviewLogController {

    private final  MerchantReviewLogService merchantReviewLogService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param merchantReviewLog 商家审核记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
//    @PreAuthorize("@pms.hasPermission('operation_merchantreviewlog_view')" )
    public R getMerchantReviewLogPage(Page page, MerchantReviewLog merchantReviewLog) {
        return R.ok(merchantReviewLogService.page(page, Wrappers.query(merchantReviewLog).orderByDesc("submit_time")));
    }


    /**
     * 通过id查询商家审核记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
//    @PreAuthorize("@pms.hasPermission('operation_merchantreviewlog_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(merchantReviewLogService.getById(id));
    }

    /**
     * 新增商家审核记录
     * @param merchantReviewLog 商家审核记录
     * @return R
     */
    @ApiOperation(value = "新增商家审核记录", notes = "新增商家审核记录")
    @SysLog("新增商家审核记录" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('operation_merchantreviewlog_add')" )
    public R save(@RequestBody MerchantReviewLog merchantReviewLog) {
        return R.ok(merchantReviewLogService.save(merchantReviewLog));
    }

    /**
     * 修改商家审核记录
     * @param merchantReviewLog 商家审核记录
     * @return R
     */
    @ApiOperation(value = "修改商家审核记录", notes = "修改商家审核记录")
    @SysLog("修改商家审核记录" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('operation_merchantreviewlog_edit')" )
    public R updateById(@RequestBody MerchantReviewLog merchantReviewLog) {
        return R.ok(merchantReviewLogService.updateById(merchantReviewLog));
    }

    /**
     * 通过id删除商家审核记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商家审核记录", notes = "通过id删除商家审核记录")
    @SysLog("通过id删除商家审核记录" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_merchantreviewlog_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(merchantReviewLogService.removeById(id));
    }

}
