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

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.entity.MerchantWelfareSetting;
import com.kbopark.operation.enums.LockStatusEnum;
import com.kbopark.operation.service.MerchantService;
import com.kbopark.operation.service.MerchantWelfareSettingService;
import com.kbopark.operation.util.TicketRule;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 商家福利设置
 *
 * @author pigx code generator
 * @date 2020-09-09 15:53:58
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchantwelfaresetting")
@Api(value = "merchantwelfaresetting", tags = "商家福利设置管理")
public class MerchantWelfareSettingController {

	private final MerchantWelfareSettingService merchantWelfareSettingService;

	/**
	 * 分页查询
	 *
	 * @param page                   分页对象
	 * @param merchantWelfareSetting 商家福利设置
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R getMerchantWelfareSettingPage(Page page, MerchantWelfareSetting merchantWelfareSetting) {
		return R.ok(merchantWelfareSettingService.page(page, Wrappers.query(merchantWelfareSetting)));
	}


	/**
	 * 通过id查询商家福利设置
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(merchantWelfareSettingService.getById(id));
	}

	/**
	 * 新增商家福利设置
	 *
	 * @param merchantWelfareSetting 商家福利设置
	 * @return R
	 */
	@ApiOperation(value = "新增商家福利设置", notes = "新增商家福利设置")
	@SysLog("新增商家福利设置")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_merchant_welfare')")
	public R save(@RequestBody MerchantWelfareSetting merchantWelfareSetting) {
		return merchantWelfareSettingService.saveAndCheckSetting(merchantWelfareSetting);
	}

	/**
	 * 修改商家福利设置
	 *
	 * @param merchantWelfareSetting 商家福利设置
	 * @return R
	 */
	@ApiOperation(value = "修改商家福利设置", notes = "修改商家福利设置")
	@SysLog("修改商家福利设置")
	@PutMapping
	public R updateById(@RequestBody MerchantWelfareSetting merchantWelfareSetting) {
		return merchantWelfareSettingService.updateAndCheckSetting(merchantWelfareSetting);
	}

	/**
	 * 通过id删除商家福利设置
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除商家福利设置", notes = "通过id删除商家福利设置")
	@SysLog("通过id删除商家福利设置")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Integer id) {
		return R.ok(merchantWelfareSettingService.removeById(id));
	}

}
