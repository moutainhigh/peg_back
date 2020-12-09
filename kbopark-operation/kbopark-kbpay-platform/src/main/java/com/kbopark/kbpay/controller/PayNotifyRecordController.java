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

package com.kbopark.kbpay.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.kbpay.entity.PayNotifyRecord;
import com.kbopark.kbpay.service.PayNotifyRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 回调记录
 *
 * @author laomst
 * @date 2020-09-04 10:24:32
 */
@RestController
@AllArgsConstructor
@RequestMapping("/paynotifyrecord" )
@Api(value = "paynotifyrecord", tags = "回调记录管理")
public class PayNotifyRecordController {

    private final  PayNotifyRecordService payNotifyRecordService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param payNotifyRecord 回调记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('kbpay_paynotifyrecord_view')" )
    public R getPayNotifyRecordPage(Page page, PayNotifyRecord payNotifyRecord) {
        return R.ok(payNotifyRecordService.page(page, Wrappers.query(payNotifyRecord)));
    }


    /**
     * 通过id查询回调记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('kbpay_paynotifyrecord_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(payNotifyRecordService.getById(id));
    }

    /**
     * 新增回调记录
     * @param payNotifyRecord 回调记录
     * @return R
     */
    @ApiOperation(value = "新增回调记录", notes = "新增回调记录")
    @SysLog("新增回调记录" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('kbpay_paynotifyrecord_add')" )
    public R save(@RequestBody PayNotifyRecord payNotifyRecord) {
        return R.ok(payNotifyRecordService.save(payNotifyRecord));
    }

    /**
     * 修改回调记录
     * @param payNotifyRecord 回调记录
     * @return R
     */
    @ApiOperation(value = "修改回调记录", notes = "修改回调记录")
    @SysLog("修改回调记录" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('kbpay_paynotifyrecord_edit')" )
    public R updateById(@RequestBody PayNotifyRecord payNotifyRecord) {
        return R.ok(payNotifyRecordService.updateById(payNotifyRecord));
    }

    /**
     * 通过id删除回调记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除回调记录", notes = "通过id删除回调记录")
    @SysLog("通过id删除回调记录" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('kbpay_paynotifyrecord_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(payNotifyRecordService.removeById(id));
    }

}
