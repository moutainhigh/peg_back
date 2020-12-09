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
import com.kbopark.kbpay.entity.MerchantPayChannel;
import com.kbopark.kbpay.service.MerchantPayChannelService;
import com.kbopark.operation.fegin.RemoteMerchantService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 商户支付通道设置
 *
 * @author laomst
 * @date 2020-09-03 11:41:58
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchantpaychannel" )
@Api(value = "merchantpaychannel", tags = "商户支付通道设置管理")
public class MerchantPayChannelController {

    private final  MerchantPayChannelService merchantPayChannelService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param merchantPayChannel 商户支付通道设置
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('kbpay_merchantpaychannel_view')" )
    public R getMerchantPayChannelPage(Page page, MerchantPayChannel merchantPayChannel) {
        return R.ok(merchantPayChannelService.page(page, Wrappers.query(merchantPayChannel).select(MerchantPayChannel.PAGE_COLUMNS)));
    }


    /**
     * 通过id查询商户支付通道设置
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('kbpay_merchantpaychannel_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(merchantPayChannelService.getById(id));
    }

    /**
     * 新增商户支付通道设置
     * @param merchantPayChannel 商户支付通道设置
     * @return R
     */
    @ApiOperation(value = "新增商户支付通道设置", notes = "新增商户支付通道设置")
    @SysLog("新增商户支付通道设置" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('kbpay_merchantpaychannel_add')" )
    public R save(@RequestBody MerchantPayChannel merchantPayChannel) {
        try {
			return R.ok(merchantPayChannelService.saveMerchantPayChannel(merchantPayChannel));
		} catch (UnsupportedOperationException ex) {
        	return R.failed(ex.getMessage());
		}
    }

    /**
     * 修改商户支付通道设置
     * @param merchantPayChannel 商户支付通道设置
     * @return R
     */
    @ApiOperation(value = "修改商户支付通道设置", notes = "修改商户支付通道设置")
    @SysLog("修改商户支付通道设置" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('kbpay_merchantpaychannel_edit')" )
    public R updateById(@RequestBody MerchantPayChannel merchantPayChannel) {
        try {
			return R.ok(merchantPayChannelService.updateMerchantPayChannel(merchantPayChannel));
		} catch (UnsupportedOperationException ex) {
        	return R.failed(ex.getMessage());
		}
    }

    /**
     * 通过id删除商户支付通道设置
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商户支付通道设置", notes = "通过id删除商户支付通道设置")
    @SysLog("通过id删除商户支付通道设置" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('kbpay_merchantpaychannel_del')" )
    public R removeById(@PathVariable Integer id) {
        try {
			return R.ok(merchantPayChannelService.removeMerchantPayChannel(id));
		} catch (UnsupportedOperationException ex) {
        	return R.failed(ex.getMessage());
		}
    }

}
