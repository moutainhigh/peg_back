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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.dto.LedgerAuditDTO;
import com.kbopark.operation.entity.LedgerOrder;
import com.kbopark.operation.service.LedgerOrderService;
import com.kbopark.operation.unionpay.dto.MoneySubAccountDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 分账订单记录
 *
 * @author pigx code generator
 * @date 2020-09-14 13:50:42
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ledgerorder")
@Api(value = "ledgerorder", tags = "分账订单记录管理")
public class LedgerOrderController {

	private final LedgerOrderService ledgerOrderService;


	/**
	 * 分页查询
	 *
	 * @param page        分页对象
	 * @param ledgerOrder 分账订单记录
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_ledgerorder_view')")
	public R getLedgerOrderPage(Page page, LedgerOrder ledgerOrder) {
		KboparkUser user = SecurityUtils.getUser();
		Integer deptId = user.getDeptId();
		if(user.getUserType() > UserTypeEnum.Operation.getCode() && user.getUserType() < UserTypeEnum.Merchant.getCode()){
			return R.failed("查询无权限");
		}
		if(UserTypeEnum.Operation.getCode().equals(user.getUserType())){
			ledgerOrder.setOperationId(deptId);
		}
		if(user.getUserType() >= UserTypeEnum.Merchant.getCode()){
			ledgerOrder.setMerchantId(user.getMerchantId());
		}
		return R.ok(ledgerOrderService.page(page, Wrappers.query(ledgerOrder)));
	}


	/**
	 * 通过id查询分账订单记录
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_ledgerorder_view')")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(ledgerOrderService.getById(id));
	}

	/**
	 * 通过分账批次号查询分账订单记录
	 *
	 * @return R
	 */
	@ApiOperation(value = "通过分账批次号查询", notes = "通过分账批次号查询")
	@GetMapping("/list")
	public R getByLedgerBatchNo(Page page, LedgerOrder ledgerOrder) {
		return R.ok(ledgerOrderService.page(page, Wrappers.query(ledgerOrder)));
	}


	/**
	 * 通过id删除分账订单记录
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除分账订单记录", notes = "通过id删除分账订单记录")
	@SysLog("通过id删除分账订单记录")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_ledgerorder_del')")
	public R removeById(@PathVariable Long id) {
		return R.ok(ledgerOrderService.removeById(id));
	}



}
