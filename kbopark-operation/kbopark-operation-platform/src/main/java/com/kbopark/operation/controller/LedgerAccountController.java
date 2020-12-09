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

import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.entity.LedgerAccount;
import com.kbopark.operation.service.LedgerAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 分账账户管理
 *
 * @author pigx code generator
 * @date 2020-09-29 10:24:33
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ledgeraccount")
@Api(value = "ledgeraccount", tags = "分账账户管理管理")
public class LedgerAccountController {

	private final LedgerAccountService ledgerAccountService;

	/**
	 * 分页查询
	 *
	 * @param page          分页对象
	 * @param ledgerAccount 分账账户管理
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_ledgeraccount_view')")
	public R getLedgerAccountPage(Page page, LedgerAccount ledgerAccount) {
		return R.ok(ledgerAccountService.page(page, Wrappers.query(ledgerAccount)));
	}


	/***
	 * 分类账户数据查询
	 * @param type
	 * @return
	 */
	@ApiOperation(value = "分类账户数据查询", notes = "分类账户数据查询")
	@GetMapping("/list/{type}")
	@PreAuthorize("@pms.hasPermission('operation_ledgeraccount_view')")
	public R getLedgerAccountList(@PathVariable String type) {
		return R.ok(ledgerAccountService.findListByType(type));
	}


	/**
	 * 通过id查询分账账户管理
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_ledgeraccount_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(ledgerAccountService.getById(id));
	}

	/**
	 * 新增分账账户管理
	 *
	 * @param ledgerAccount 分账账户管理
	 * @return R
	 */
	@ApiOperation(value = "新增分账账户管理", notes = "新增分账账户管理")
	@SysLog("新增分账账户管理")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_ledgeraccount_add')")
	public R save(@RequestBody LedgerAccount ledgerAccount) {
		KboparkUser user = SecurityUtils.getUser();
		ledgerAccount.setCreateBy(user.getRealName());
		LedgerAccount byDeptId = ledgerAccountService.findByDeptId(ledgerAccount.getRelationId());
		if(byDeptId != null){
			return R.failed("该机构已配置账户");
		}
		LedgerAccount byMerNo = ledgerAccountService.findByMerNo(ledgerAccount.getMerNo());
		if(byMerNo != null){
			return R.failed("企业用户号已存在");
		}
		return R.ok(ledgerAccountService.save(ledgerAccount));
	}

	/**
	 * 修改分账账户管理
	 *
	 * @param ledgerAccount 分账账户管理
	 * @return R
	 */
	@ApiOperation(value = "修改分账账户管理", notes = "修改分账账户管理")
	@SysLog("修改分账账户管理")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_ledgeraccount_edit')")
	public R updateById(@RequestBody LedgerAccount ledgerAccount) {
		KboparkUser user = SecurityUtils.getUser();
		ledgerAccount.setUpdateTime(LocalDateTime.now());
		ledgerAccount.setUpdateBy(user.getRealName());
		LedgerAccount byMerNo = ledgerAccountService.findByMerNo(ledgerAccount.getMerNo(),ledgerAccount.getId());
		if(byMerNo != null){
			return R.failed("企业用户号已存在");
		}
		return R.ok(ledgerAccountService.updateById(ledgerAccount));
	}

	/**
	 * 通过id删除分账账户管理
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除分账账户管理", notes = "通过id删除分账账户管理")
	@SysLog("通过id删除分账账户管理")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_ledgeraccount_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(ledgerAccountService.removeById(id));
	}

}
