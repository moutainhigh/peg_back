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
import com.kbopark.operation.entity.LedgerAuditLog;
import com.kbopark.operation.service.LedgerAuditLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 分账订单审核日志
 *
 * @author pigx code generator
 * @date 2020-10-16 13:56:49
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ledgerauditlog" )
@Api(value = "ledgerauditlog", tags = "分账订单审核日志管理")
public class LedgerAuditLogController {

    private final  LedgerAuditLogService ledgerAuditLogService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param ledgerAuditLog 分账订单审核日志
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('operation_ledgerauditlog_view')" )
    public R getLedgerAuditLogPage(Page page, LedgerAuditLog ledgerAuditLog) {
        return R.ok(ledgerAuditLogService.page(page, Wrappers.query(ledgerAuditLog)));
    }


    /**
     * 通过id查询分账订单审核日志
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_ledgerauditlog_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(ledgerAuditLogService.getById(id));
    }

    /**
     * 新增分账订单审核日志
     * @param ledgerAuditLog 分账订单审核日志
     * @return R
     */
    @ApiOperation(value = "新增分账订单审核日志", notes = "新增分账订单审核日志")
    @SysLog("新增分账订单审核日志" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('operation_ledgerauditlog_add')" )
    public R save(@RequestBody LedgerAuditLog ledgerAuditLog) {
        return R.ok(ledgerAuditLogService.save(ledgerAuditLog));
    }

    /**
     * 修改分账订单审核日志
     * @param ledgerAuditLog 分账订单审核日志
     * @return R
     */
    @ApiOperation(value = "修改分账订单审核日志", notes = "修改分账订单审核日志")
    @SysLog("修改分账订单审核日志" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('operation_ledgerauditlog_edit')" )
    public R updateById(@RequestBody LedgerAuditLog ledgerAuditLog) {
        return R.ok(ledgerAuditLogService.updateById(ledgerAuditLog));
    }

    /**
     * 通过id删除分账订单审核日志
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除分账订单审核日志", notes = "通过id删除分账订单审核日志")
    @SysLog("通过id删除分账订单审核日志" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_ledgerauditlog_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(ledgerAuditLogService.removeById(id));
    }


	/**
	 * 通过分账批次号查询审核记录
	 *
	 * @param batchNo
	 * @return
	 */
	@ApiOperation(value = "通过分账批次号查询审核记录", notes = "通过分账批次号查询审核记录")
	@SysLog("通过分账批次号查询审核记录" )
	@GetMapping("/record/{batchNo}" )
	public R getAuditRecord(@PathVariable String batchNo) {
		return R.ok(ledgerAuditLogService.findByLedgerBatchNo(batchNo));
	}

}
