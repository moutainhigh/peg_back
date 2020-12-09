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

import cn.hutool.core.util.ObjectUtil;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.dto.MerchantProfitParam;
import com.kbopark.operation.dto.MerchantReviewParam;
import com.kbopark.operation.dto.MerchantSearchParam;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.enums.MerchantReviewStatusEnum;
import com.kbopark.operation.service.MerchantService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 商家基本信息表
 *
 * @author laomst
 * @date 2020-08-25 17:59:54
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchant")
@Api(value = "merchant", tags = "商家基本信息表管理")
public class MerchantController {

	private final MerchantService merchantService;


	/**
	 * 分页查询
	 *
	 * @param page 分页对象
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
//	@PreAuthorize("@pms.hasPermission('operation_merchant_view')")
	public R<IPage<Merchant>> getMerchantPage(Page<Merchant> page, MerchantSearchParam param) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		// 如果当前用户是推广员
		if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Promoter.code)) {
			param.setPromoterId(user.getId());
		}
		// 如果当前用户是渠道商
		else if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Channel.code)) {
			param.setDistributorId(user.getDeptId());
		}
		// 如果当前用户是运营平台
		else if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Operation.code)) {
			param.setOperatorId(user.getDeptId());
		}
		// 除此之外，如果不是总平台
		else if (!ObjectUtil.equal(user.getUserType(), UserTypeEnum.Administrator.code)) {
			return R.failed("没有权限");
		}
		return R.ok(merchantService.selectMerchantPage(page, param));
	}


	/**
	 * 通过id查询商家基本信息表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
//	@PreAuthorize("@pms.hasPermission('operation_merchant_view')")
	public R getById(@PathVariable("id") Integer id, @RequestParam(value = "review", defaultValue = "false") Boolean review) {
		Merchant entity = merchantService.getById(id);
		// 如果是审核查询，并且是编辑审核的状态下，需要把表单反序列化
		if (review) {
			if (ObjectUtil.equal(entity.getReviewStatus(), MerchantReviewStatusEnum.EDIT_DISTRIBUTOR_UN_CHECKED.code)
					|| ObjectUtil.equal(entity.getReviewStatus(), MerchantReviewStatusEnum.EDIT_OPERATOR_UN_CHECKED.code)) {
				entity = JSON.parseObject(entity.getTodoSnapshoot(), Merchant.class);
			}
		}
		return R.ok(entity);
	}

	/**
	 * 新增商家基本信息表
	 *
	 * @param merchant 商家基本信息表
	 * @return R
	 */
	@ApiOperation(value = "新增商家基本信息表", notes = "新增商家基本信息表")
	@SysLog("新增商家基本信息表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_merchant_add')")
	public R save(@RequestBody Merchant merchant) {
		KboparkUser user = SecurityUtils.getUser();
		return R.ok(merchantService.saveMerchant(merchant, user));
	}

	/**
	 * 修改商家基本信息表
	 *
	 * @param merchant 商家基本信息表
	 * @return R
	 */
	@ApiOperation(value = "修改商家基本信息表", notes = "修改商家基本信息表")
	@SysLog("修改商家基本信息表")
	@PutMapping
//	@PreAuthorize("@pms.hasPermission('operation_merchant_edit')")
	public R updateById(@RequestBody Merchant merchant) {
		return R.ok(merchantService.updateMerchant(merchant, SecurityUtils.getUser()));
	}

	/**
	 * 通过id删除商家基本信息表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除商家基本信息表", notes = "通过id删除商家基本信息表")
	@SysLog("通过id删除商家基本信息表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_merchant_del')")
	public R removeById(@PathVariable Integer id) {
		Merchant entity = merchantService.getById(id);
		if (null == entity) {
			return R.failed("系统中没有当前实体");
		}
		if (entity.getReviewStatus() > 50) {
			return R.failed("已经通过审核的商家不能进行删除");
		}
		return R.ok(merchantService.removeById(id));
	}

	@GetMapping({"selector-list", "list-of-distributor"})
	@ApiModelProperty("查询用户可见的商家的选择器列表列表")
	public R<List<Merchant>> listOfDistributor(@RequestParam(value = "operatorId", required = false) Integer operatorId,
											   @RequestParam(value = "distributorId", required = false) Integer distributorId) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		LambdaQueryWrapper<Merchant> query = Wrappers.<Merchant>lambdaQuery();
		query.select(Merchant::getId, Merchant::getName, Merchant::getOperatorId, Merchant::getDistributorId)
				.eq(null != operatorId, Merchant::getOperatorId, operatorId)
				.eq(null != distributorId, Merchant::getDistributorId, distributorId)
				.gt(Merchant::getReviewStatus, 50);
		// 如果当前用户是推广员
		if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Promoter.code)) {
			query.eq(Merchant::getPromoterId, user.getId());
		}
		// 如果当前用户是渠道商
		else if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Channel.code)) {
			query.eq(Merchant::getDistributorId, user.getDeptId());
		}
		// 如果当前用户是运营平台
		else if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Operation.code)) {
			query.eq(Merchant::getOperatorId, user.getDeptId());
		}
		// 除此之外，如果不是总平台
		else if (!ObjectUtil.equal(user.getUserType(), UserTypeEnum.Administrator.code)) {
			return R.failed("没有权限");
		}
		List<Merchant> resList = merchantService.list(query);
		return R.ok(resList);
	}

	/**
	 * 审核
	 *
	 * @param param
	 * @return
	 */
	@PutMapping("review")
	public R<Boolean> review(@RequestBody MerchantReviewParam param) {
		KboparkUser user = SecurityUtils.getUser();
		return R.ok(merchantService.review(param, user));
	}

	@PutMapping("up-or-down/{id}")
	public R<Boolean> upOrDown(@PathVariable("id") Integer id) {
		KboparkUser user = SecurityUtils.getUser();
		return R.ok(merchantService.upOrDown(id, user));
	}

	@PostMapping("set-profit")
	public R<Boolean> setProfit(@RequestBody MerchantProfitParam param) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		Boolean update = merchantService.update(w -> {
			w.lambda().eq(Merchant::getId, param.getMerchantId())
					.set(Merchant::getProfitPercent, param.getProfitPercent())
					.set(Merchant::getOperatorProfitPercent, param.getOperatorProfitPercent())
					.set(Merchant::getWelfarePercent, param.getWelfarePercent());
		});
		return R.ok(update);
	}

}
