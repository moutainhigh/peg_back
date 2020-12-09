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

import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.entity.LedgerAuditStrategy;
import com.kbopark.operation.service.LedgerAuditStrategyService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 财务审核策略设置
 *
 * @author pigx code generator
 * @date 2020-10-15 16:15:31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ledgerauditstrategy")
@Api(value = "ledgerauditstrategy", tags = "财务审核策略设置管理")
public class LedgerAuditStrategyController {

	private final LedgerAuditStrategyService ledgerAuditStrategyService;

	/**
	 * 分页查询
	 *
	 * @param page                分页对象
	 * @param ledgerAuditStrategy 财务审核策略设置
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_ledgerauditstrategy_view')")
	public R getLedgerAuditStrategyPage(Page page, LedgerAuditStrategy ledgerAuditStrategy) {
		return R.ok(ledgerAuditStrategyService.page(page, Wrappers.query(ledgerAuditStrategy)));
	}


	/**
	 * 通过id查询财务审核策略设置
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_ledgerauditstrategy_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(ledgerAuditStrategyService.getById(id));
	}

	/**
	 * 查询当前财务审核策略设置
	 *
	 * @return R
	 */
	@ApiOperation(value = "查询当前财务审核策略设置", notes = "查询当前财务审核策略设置")
	@GetMapping("/getOwnAuditStrategy")
	public R getOwnAuditStrategy() {
		KboparkUser user = SecurityUtils.getUser();
		if(user.getUserType() > UserTypeEnum.Operation.getCode()){
			return R.failed("无操作权限");
		}
		QueryWrapper<LedgerAuditStrategy> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(LedgerAuditStrategy::getDeptId, user.getDeptId());
		return R.ok(ledgerAuditStrategyService.getOne(queryWrapper, false));
	}


	/**
	 * 新增或者更新财务审核策略设置
	 *
	 * @param ledgerAuditStrategy 财务审核策略设置
	 * @return R
	 */
	@ApiOperation(value = "新增财务审核策略设置", notes = "新增财务审核策略设置")
	@SysLog("新增财务审核策略设置")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_ledgerauditstrategy_add')")
	public R save(@RequestBody LedgerAuditStrategy ledgerAuditStrategy) {
		KboparkUser user = SecurityUtils.getUser();
		if(user.getUserType() > UserTypeEnum.Operation.getCode()){
			return R.failed("无操作权限");
		}
		QueryWrapper<LedgerAuditStrategy> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(LedgerAuditStrategy::getDeptId, user.getDeptId());
		LedgerAuditStrategy one = ledgerAuditStrategyService.getOne(queryWrapper, false);
		if(one != null){
			ledgerAuditStrategy.setId(one.getId());
			ledgerAuditStrategy.setUpdateTime(LocalDateTime.now());
			ledgerAuditStrategy.setUpdateBy(user.getRealName());
		}
		ledgerAuditStrategy.setDeptId(user.getDeptId());
		return R.ok(ledgerAuditStrategyService.saveOrUpdate(ledgerAuditStrategy));
	}

	/**
	 * 修改财务审核策略设置
	 *
	 * @param ledgerAuditStrategy 财务审核策略设置
	 * @return R
	 */
	@ApiOperation(value = "修改财务审核策略设置", notes = "修改财务审核策略设置")
	@SysLog("修改财务审核策略设置")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_ledgerauditstrategy_edit')")
	public R updateById(@RequestBody LedgerAuditStrategy ledgerAuditStrategy) {
		return R.ok(ledgerAuditStrategyService.updateById(ledgerAuditStrategy));
	}

	/**
	 * 通过id删除财务审核策略设置
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除财务审核策略设置", notes = "通过id删除财务审核策略设置")
	@SysLog("通过id删除财务审核策略设置")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_ledgerauditstrategy_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(ledgerAuditStrategyService.removeById(id));
	}

}
