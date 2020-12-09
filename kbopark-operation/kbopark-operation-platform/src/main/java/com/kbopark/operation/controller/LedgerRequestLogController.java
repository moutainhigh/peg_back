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
import com.kbopark.operation.entity.LedgerRequestLog;
import com.kbopark.operation.service.LedgerRequestLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 分账请求日志
 *
 * @author pigx code generator
 * @date 2020-09-24 17:30:47
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ledgerrequestlog")
@Api(value = "ledgerrequestlog", tags = "分账请求日志管理")
public class LedgerRequestLogController {

	private final LedgerRequestLogService ledgerRequestLogService;

	/**
	 * 分页查询
	 *
	 * @param page             分页对象
	 * @param ledgerRequestLog 分账请求日志
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_ledgerrequestlog_view')")
	public R getLedgerRequestLogPage(Page page, LedgerRequestLog ledgerRequestLog) {
		return R.ok(ledgerRequestLogService.page(page, Wrappers.query(ledgerRequestLog)));
	}


	/**
	 * 通过id查询分账请求日志
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_ledgerrequestlog_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(ledgerRequestLogService.getById(id));
	}

	/**
	 * 新增分账请求日志
	 *
	 * @param ledgerRequestLog 分账请求日志
	 * @return R
	 */
	@ApiOperation(value = "新增分账请求日志", notes = "新增分账请求日志")
	@SysLog("新增分账请求日志")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_ledgerrequestlog_add')")
	public R save(@RequestBody LedgerRequestLog ledgerRequestLog) {
		return R.ok(ledgerRequestLogService.save(ledgerRequestLog));
	}

	/**
	 * 修改分账请求日志
	 *
	 * @param ledgerRequestLog 分账请求日志
	 * @return R
	 */
	@ApiOperation(value = "修改分账请求日志", notes = "修改分账请求日志")
	@SysLog("修改分账请求日志")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_ledgerrequestlog_edit')")
	public R updateById(@RequestBody LedgerRequestLog ledgerRequestLog) {
		return R.ok(ledgerRequestLogService.updateById(ledgerRequestLog));
	}

	/**
	 * 通过id删除分账请求日志
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除分账请求日志", notes = "通过id删除分账请求日志")
	@SysLog("通过id删除分账请求日志")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_ledgerrequestlog_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(ledgerRequestLogService.removeById(id));
	}

}
