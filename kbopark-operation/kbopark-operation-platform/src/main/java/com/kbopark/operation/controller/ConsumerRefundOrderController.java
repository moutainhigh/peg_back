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
import com.kbopark.operation.entity.ConsumerRefundOrder;
import com.kbopark.operation.service.ConsumerRefundOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 用户退款申请
 *
 * @author pigx code generator
 * @date 2020-10-28 14:36:17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/consumerrefundorder" )
@Api(value = "consumerrefundorder", tags = "用户退款申请管理")
public class ConsumerRefundOrderController {

    private final  ConsumerRefundOrderService consumerRefundOrderService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param consumerRefundOrder 用户退款申请
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('operation_consumerrefundorder_view')" )
    public R getConsumerRefundOrderPage(Page page, ConsumerRefundOrder consumerRefundOrder) {
        return R.ok(consumerRefundOrderService.page(page, Wrappers.query(consumerRefundOrder)));
    }


    /**
     * 通过id查询用户退款申请
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_consumerrefundorder_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(consumerRefundOrderService.getById(id));
    }


	/**
	 * 审核更新退款申请状态
	 *
	 * @param consumerRefundOrder
	 * @return
	 */
	@ApiOperation(value = "审核更新退款申请状态", notes = "审核更新退款申请状态")
	@PostMapping("/updateState" )
	@PreAuthorize("@pms.hasPermission('operation_consumerrefundorder_audit')" )
	public R updateState(@RequestBody ConsumerRefundOrder consumerRefundOrder) {
		return consumerRefundOrderService.updateProcessState(consumerRefundOrder);
	}




    /**
     * 通过id删除用户退款申请
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除用户退款申请", notes = "通过id删除用户退款申请")
    @SysLog("通过id删除用户退款申请" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_consumerrefundorder_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(consumerRefundOrderService.removeById(id));
    }

}
