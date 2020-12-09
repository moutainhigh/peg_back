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

package cn.qdzhhl.kbopark.admin.controller;

import cn.qdzhhl.kbopark.admin.api.entity.SysMerchantMenu;
import cn.qdzhhl.kbopark.admin.api.vo.MerchantMenuVO;
import cn.qdzhhl.kbopark.admin.service.SysMerchantMenuService;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 商家菜单权限关联表
 *
 * @author pigx code generator
 * @date 2020-09-04 15:35:57
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysmerchantmenu")
@Api(value = "sysmerchantmenu", tags = "商家菜单权限关联表管理")
public class SysMerchantMenuController {

	private final SysMerchantMenuService sysMerchantMenuService;

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param sysMerchantMenu 商家菜单权限关联表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R getSysMerchantMenuPage(Page page, SysMerchantMenu sysMerchantMenu) {
		return R.ok(sysMerchantMenuService.page(page, Wrappers.query(sysMerchantMenu)));
	}


	/**
	 * 通过id查询商家菜单权限关联表
	 *
	 * @param merchantId id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{merchantId}")
	public R getById(@PathVariable("merchantId") Integer merchantId) {
		return R.ok(sysMerchantMenuService.getById(merchantId));
	}

	/**
	 * 新增商家菜单权限关联表
	 *
	 * @param sysMerchantMenu 商家菜单权限关联表
	 * @return R
	 */
	@ApiOperation(value = "新增商家菜单权限关联表", notes = "新增商家菜单权限关联表")
	@SysLog("新增商家菜单权限关联表")
	@PostMapping
	public R save(@RequestBody SysMerchantMenu sysMerchantMenu) {
		return R.ok(sysMerchantMenuService.save(sysMerchantMenu));
	}

	/**
	 * 修改商家菜单权限关联表
	 *
	 * @param sysMerchantMenu 商家菜单权限关联表
	 * @return R
	 */
	@ApiOperation(value = "修改商家菜单权限关联表", notes = "修改商家菜单权限关联表")
	@SysLog("修改商家菜单权限关联表")
	@PutMapping
	public R updateById(@RequestBody SysMerchantMenu sysMerchantMenu) {
		return R.ok(sysMerchantMenuService.updateById(sysMerchantMenu));
	}

	/**
	 * 通过id删除商家菜单权限关联表
	 *
	 * @param merchantId id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除商家菜单权限关联表", notes = "通过id删除商家菜单权限关联表")
	@SysLog("通过id删除商家菜单权限关联表")
	@DeleteMapping("/{merchantId}")
	public R removeById(@PathVariable Integer merchantId) {
		return R.ok(sysMerchantMenuService.removeById(merchantId));
	}


	/**
	 * 获取商家选中的菜单ID
	 *
	 * @param merchantId
	 * @return
	 */
	@GetMapping("/merchantMenuTree/{merchantId}")
	public R getRoleTree(@PathVariable Integer merchantId) {
		List<SysMerchantMenu> list = sysMerchantMenuService.list(Wrappers.<SysMerchantMenu>lambdaQuery().eq(SysMerchantMenu::getMerchantId, merchantId));
		return R.ok(list.stream().map(SysMerchantMenu::getMenuId).collect(Collectors.toList()));
	}

	/**
	 * 更新商家菜单
	 *
	 * @param merchantMenuVO
	 * @return
	 */
	@SysLog("更新部门菜单")
	@PostMapping("/menu")
	public R saveRoleMenus(@RequestBody MerchantMenuVO merchantMenuVO) {
		return R.ok(sysMerchantMenuService.saveMerchantMenus(merchantMenuVO.getMerchantId(), merchantMenuVO.getMenuIds()));
	}

}
