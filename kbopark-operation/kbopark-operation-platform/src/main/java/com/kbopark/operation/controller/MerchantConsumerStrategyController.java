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
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.dto.ConsumerStrategySearchParam;
import com.kbopark.operation.entity.MerchantConsumerStrategy;
import com.kbopark.operation.service.MerchantConsumerStrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 商家消费策论
 *
 * @author laomst
 * @date 2020-09-02 09:43:14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchantconsumerstrategy")
@Api(value = "merchantconsumerstrategy", tags = "商家消费策论管理")
public class MerchantConsumerStrategyController {

	private final MerchantConsumerStrategyService merchantConsumerStrategyService;

	/**
	 * 分页查询
	 *
	 * @param page                     分页对象
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_merchantconsumerstrategy_view')")
	public R getMerchantConsumerStrategyPage(Page<MerchantConsumerStrategy> page, ConsumerStrategySearchParam param) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (user.getUserType() < UserTypeEnum.Merchant.code) {
			return R.failed("只有商家相关的用户可以查询消费策略");
		}
		param.setMerchantIdKey(user.getMerchantId());
		return R.ok(merchantConsumerStrategyService.selectStrategyPage(page, param));
	}


	/**
	 * 通过id查询商家消费策论
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_merchantconsumerstrategy_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(merchantConsumerStrategyService.getById(id));
	}

	/**
	 * 新增商家消费策论
	 *
	 * @param merchantConsumerStrategy 商家消费策论
	 * @return R
	 */
	@ApiOperation(value = "新增商家消费策论", notes = "新增商家消费策论")
	@SysLog("新增商家消费策论")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_merchantconsumerstrategy_add')")
	public R save(@RequestBody MerchantConsumerStrategy merchantConsumerStrategy) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		if (user.getUserType() < UserTypeEnum.Merchant.code) {
			return R.failed("只有商家相关的用户可以添加消费策略");
		}
		merchantConsumerStrategy.setMerchantId(user.getMerchantId());
		return R.ok(merchantConsumerStrategyService.saveStrategy(merchantConsumerStrategy));
	}

	/**
	 * 修改商家消费策论
	 *
	 * @param merchantConsumerStrategy 商家消费策论
	 * @return R
	 */
	@ApiOperation(value = "修改商家消费策论", notes = "修改商家消费策论")
	@SysLog("修改商家消费策论")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_merchantconsumerstrategy_edit')")
	public R updateById(@RequestBody MerchantConsumerStrategy merchantConsumerStrategy) {
		return R.ok(merchantConsumerStrategyService.updateStrategy(merchantConsumerStrategy));
	}

	/**
	 * 通过id删除商家消费策论
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除商家消费策论", notes = "通过id删除商家消费策论")
	@SysLog("通过id删除商家消费策论")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_merchantconsumerstrategy_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(merchantConsumerStrategyService.removeConsumerStrategy(id));
	}

	@PutMapping("up-or-down/{id}")
	public R<Boolean> upOrDown(@PathVariable("id") Integer id) {
		KboparkUser user = SecurityUtils.getUser();
		return R.ok(merchantConsumerStrategyService.upOrDown(id, user));
	}

}
