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
import com.kbopark.kbpay.entity.SystemPayChannel;
import com.kbopark.kbpay.service.MerchantPayChannelService;
import com.kbopark.kbpay.service.SystemPayChannelService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 系统支付通道
 *
 * @author laomst
 * @date 2020-09-03 10:05:23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/systempaychannel" )
@Api(value = "systempaychannel", tags = "系统支付通道管理")
public class SystemPayChannelController {

    private final  SystemPayChannelService systemPayChannelService;

    private final MerchantPayChannelService merchantPayChannelService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param systemPayChannel 系统支付通道
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('kbpay_systempaychannel_view')" )
    public R getSystemPayChannelPage(Page page, SystemPayChannel systemPayChannel) {
        return R.ok(systemPayChannelService.page(page, Wrappers.query(systemPayChannel)));
    }


    /**
     * 通过id查询系统支付通道
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('kbpay_systempaychannel_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(systemPayChannelService.getById(id));
    }

    /**
     * 新增系统支付通道
     * @param systemPayChannel 系统支付通道
     * @return R
     */
    @ApiOperation(value = "新增系统支付通道", notes = "新增系统支付通道")
    @SysLog("新增系统支付通道" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('kbpay_systempaychannel_add')" )
    public R save(@RequestBody SystemPayChannel systemPayChannel) {
        try {
			return R.ok(systemPayChannelService.saveSystemPayChannel(systemPayChannel));
		} catch (UnsupportedOperationException ex) {
        	return R.failed(ex.getMessage());
		}
    }

    /**
     * 修改系统支付通道
     * @param systemPayChannel 系统支付通道
     * @return R
     */
    @ApiOperation(value = "修改系统支付通道", notes = "修改系统支付通道")
    @SysLog("修改系统支付通道" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('kbpay_systempaychannel_edit')" )
    public R updateById(@RequestBody SystemPayChannel systemPayChannel) {
        try {
			return R.ok(systemPayChannelService.updateSystemPayChannel(systemPayChannel));
		} catch (UnsupportedOperationException ex) {
        	return R.failed(ex.getMessage());
		}
    }

    /**
     * 通过id删除系统支付通道
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除系统支付通道", notes = "通过id删除系统支付通道")
    @SysLog("通过id删除系统支付通道" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('kbpay_systempaychannel_del')" )
    public R removeById(@PathVariable Integer id) {
    	try {
			return R.ok(systemPayChannelService.removeSystemPayChannel(id));
		} catch (UnsupportedOperationException ex) {
    		return R.failed(ex.getMessage());
		}
    }

    @ApiModelProperty("获取所有通道类型的列表")
    @GetMapping("list")
    public R list() {
    	return R.ok(systemPayChannelService.list());
	}

	@PutMapping("up-or-down/{id}")
	public R<Boolean> upOrDown(@PathVariable("id") Integer id) {
    	try {
			return R.ok(systemPayChannelService.upOrDown(id));
		} catch (UnsupportedOperationException ex) {
    		return R.failed(ex.getMessage());
		}
	}
}
