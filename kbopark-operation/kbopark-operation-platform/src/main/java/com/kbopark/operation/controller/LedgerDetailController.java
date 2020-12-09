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
import com.kbopark.operation.entity.LedgerDetail;
import com.kbopark.operation.service.LedgerDetailService;
import com.kbopark.operation.unionpay.enums.TransCodeEnum;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 分账明细
 *
 * @author pigx code generator
 * @date 2020-09-28 15:26:39
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ledgerdetail")
@Api(value = "ledgerdetail", tags = "分账明细管理")
public class LedgerDetailController {

	private final LedgerDetailService ledgerDetailService;

	/**
	 * 分页查询
	 *
	 * @param page         分页对象
	 * @param ledgerDetail 分账明细
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_ledgerdetail_view')")
	public R getLedgerDetailPage(Page page, LedgerDetail ledgerDetail) {
		KboparkUser user = SecurityUtils.getUser();
		Integer deptId = user.getDeptId();
		if(user.getUserType() > UserTypeEnum.Operation.getCode() && user.getUserType() < UserTypeEnum.Merchant.getCode()){
			return R.failed("查询无权限");
		}
		//运营商查询设置运营商ID
		if(UserTypeEnum.Operation.getCode().equals(user.getUserType())){
			ledgerDetail.setOperationId(deptId);
		}
		//商家查询设置商家ID和交易码
		if(user.getUserType() >= UserTypeEnum.Merchant.getCode()){
			ledgerDetail.setMerchantId(user.getMerchantId());
			ledgerDetail.setTransCode(TransCodeEnum.T202002.getCode());
		}
		return R.ok(ledgerDetailService.page(page, Wrappers.query(ledgerDetail)));
	}


	/**
	 * 通过id查询分账明细
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_ledgerdetail_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(ledgerDetailService.getById(id));
	}





}
