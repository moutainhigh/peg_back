/*
 *
 *      Copyright (c) 2018-2025, kbopark All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: kbopark
 *
 */

package cn.qdzhhl.kbopark.admin.controller;

import cn.hutool.core.util.RandomUtil;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.admin.utils.CommonUtil;
import cn.qdzhhl.kbopark.common.core.constant.CommonConstants;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.admin.api.entity.SysRole;
import cn.qdzhhl.kbopark.admin.api.vo.RoleVO;
import cn.qdzhhl.kbopark.admin.service.SysRoleMenuService;
import cn.qdzhhl.kbopark.admin.service.SysRoleService;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author kbopark
 * @date 2020-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Api(value = "role", tags = "角色管理模块")
public class SysRoleController {

	private final SysRoleService sysRoleService;

	private final SysRoleMenuService sysRoleMenuService;

	/**
	 * 通过ID查询角色信息
	 *
	 * @param id ID
	 * @return 角色信息
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable Integer id) {
		return R.ok(sysRoleService.getById(id));
	}

	/**
	 * 添加角色
	 *
	 * @param sysRole 角色信息
	 * @return success、false
	 */
	@SysLog("添加角色")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_role_add')")
	public R save(@Valid @RequestBody SysRole sysRole) {
		KboparkUser user = SecurityUtils.getUser();
		if(user.getUserType() >= UserTypeEnum.Merchant.getCode()){
			sysRole.setMerchantId(user.getMerchantId());
		}else{
			sysRole.setDeptId(user.getDeptId());
		}
		String code = RandomUtil.randomString(10).toUpperCase();
		sysRole.setRoleCode(code);
		return R.ok(sysRoleService.save(sysRole));
	}

	/**
	 * 修改角色
	 *
	 * @param sysRole 角色信息
	 * @return success/false
	 */
	@SysLog("修改角色")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_role_edit')")
	public R update(@Valid @RequestBody SysRole sysRole) {
		return R.ok(sysRoleService.updateById(sysRole));
	}

	/**
	 * 删除角色
	 *
	 * @param id
	 * @return
	 */
	@SysLog("删除角色")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_role_del')")
	public R removeById(@PathVariable Integer id) {
		if(id == 1){
			return R.failed("系统默认角色，无删除权限");
		}
		return R.ok(sysRoleService.removeRoleById(id));
	}

	/**
	 * 获取角色列表
	 *
	 * @return 角色列表
	 */
	@GetMapping("/list")
	public R listRoles() {
		KboparkUser user = SecurityUtils.getUser();
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
		if(user.getUserType() >= UserTypeEnum.Merchant.getCode()){
			queryWrapper.lambda().eq(SysRole::getMerchantId, user.getMerchantId());
		}else{
			queryWrapper.lambda().eq(SysRole::getDeptId, user.getDeptId());
		}

		return R.ok(sysRoleService.list(queryWrapper));
	}

	/**
	 * 分页查询角色信息
	 *
	 * @param page 分页对象
	 * @return 分页对象
	 */
	@GetMapping("/page")
	public R getRolePage(Page page) {
		KboparkUser user = SecurityUtils.getUser();
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
		if(user.getUserType() >= UserTypeEnum.Merchant.getCode()){
			queryWrapper.lambda().eq(SysRole::getMerchantId, user.getMerchantId());
		}else{
			queryWrapper.lambda().eq(SysRole::getDeptId, user.getDeptId());
		}
		return R.ok(sysRoleService.page(page, queryWrapper));
	}

	/**
	 * 更新角色菜单
	 *
	 * @param roleVo 角色对象
	 * @return success、false
	 */
	@SysLog("更新角色菜单")
	@PutMapping("/menu")
	@PreAuthorize("@pms.hasPermission('sys_role_perm')")
	public R saveRoleMenus(@RequestBody RoleVO roleVo) {
		SysRole sysRole = sysRoleService.getById(roleVo.getRoleId());
		return R.ok(sysRoleMenuService.saveRoleMenus(sysRole.getRoleCode(), roleVo.getRoleId(), roleVo.getMenuIds()));
	}

	/**
	 * 通过角色ID 查询角色列表
	 *
	 * @param roleIdList 角色ID
	 * @return
	 */
	@PostMapping("/getRoleList")
	public R getRoleList(@RequestBody List<String> roleIdList) {
		return R.ok(sysRoleService.listByIds(roleIdList));
	}

}
