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

import cn.qdzhhl.kbopark.admin.api.entity.SysDept;
import cn.qdzhhl.kbopark.admin.api.entity.SysDeptMenu;
import cn.qdzhhl.kbopark.admin.api.entity.SysDeptRelation;
import cn.qdzhhl.kbopark.admin.api.entity.SysUser;
import cn.qdzhhl.kbopark.admin.api.enums.DeptLevelEnum;
import cn.qdzhhl.kbopark.admin.api.vo.DeptMenuVO;
import cn.qdzhhl.kbopark.admin.service.SysDeptMenuService;
import cn.qdzhhl.kbopark.admin.service.SysDeptRelationService;
import cn.qdzhhl.kbopark.admin.service.SysDeptService;
import cn.qdzhhl.kbopark.admin.service.SysUserService;
import cn.qdzhhl.kbopark.admin.utils.CommonUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 前端控制器
 * </p>
 *
 * @author kbopark
 * @since 2018-01-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dept")
@Api(value = "dept", tags = "部门管理模块")
public class SysDeptController {

	private final SysDeptRelationService relationService;

	private final SysDeptService sysDeptService;

	private final SysDeptMenuService sysDeptMenuService;

	private final SysUserService sysUserService;


	/**
	 * 通过ID查询
	 *
	 * @param id ID
	 * @return SysDept
	 */
	@GetMapping("/remote/{id}")
	@Inner
	public R remoteQueryId(@PathVariable Integer id) {
		return R.ok(sysDeptService.getById(id));
	}
	/**
	 * 通过ID查询
	 *
	 * @param id ID
	 * @return SysDept
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable Integer id) {
		return R.ok(sysDeptService.getById(id));
	}

	/**
	 * 返回树形菜单集合
	 *
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree")
	public R getTree() {
		return R.ok(sysDeptService.selectTree());
	}

	/**
	 * 添加
	 *
	 * @param sysDept 实体
	 * @return success/false
	 */
	@SysLog("添加部门")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_dept_add')")
	public R save(@Valid @RequestBody SysDept sysDept) {
		KboparkUser user = SecurityUtils.getUser();
		boolean superManager = CommonUtil.isSuperManager(user);
		//非总管理员添加
		if (!superManager) {
			sysDept.setParentId(user.getDeptId());
			sysDept.setDeptLevel(DeptLevelEnum.THREE.getCode());
		}
		return R.ok(sysDeptService.saveDept(sysDept));
	}

	/**
	 * 删除
	 *
	 * @param id ID
	 * @return success/false
	 */
	@SysLog("删除部门")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_dept_del')")
	public R removeById(@PathVariable Integer id) {
		KboparkUser user = SecurityUtils.getUser();
		if (id == user.getDeptId().intValue()) {
			return R.failed("不能删除根目录");
		}
		if(!CommonUtil.isSuperManager(user)){
			List<SysDeptRelation> list = relationService.list(Wrappers.<SysDeptRelation>lambdaQuery().eq(SysDeptRelation::getAncestor, user.getDeptId()));
			List<Integer> collect = list.stream().map(SysDeptRelation::getDescendant).collect(Collectors.toList());
			if(!collect.contains(id)){
				return R.failed("操作无权限");
			}
		}
		List<SysUser> list = sysUserService.list(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDeptId, id));
		if(!CollectionUtils.isEmpty(list)){
			return R.failed("该部门下存在用户，请先移除用户再执行操作");
		}
		return R.ok(sysDeptService.removeDeptById(id));
	}

	/**
	 * 编辑
	 *
	 * @param sysDept 实体
	 * @return success/false
	 */
	@SysLog("编辑部门")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_dept_edit')")
	public R update(@Valid @RequestBody SysDept sysDept) {
		sysDept.setUpdateTime(LocalDateTime.now());
		return R.ok(sysDeptService.updateDeptById(sysDept));
	}

	/**
	 * 查收子级列表
	 *
	 * @return 返回子级
	 */
	@GetMapping(value = "/getDescendantList/{deptId}")
	public R getDescendantList(@PathVariable Integer deptId) {
		return R.ok(
				relationService.list(Wrappers.<SysDeptRelation>lambdaQuery().eq(SysDeptRelation::getAncestor, deptId)));
	}

	/**
	 * 查收子级机构部门
	 *
	 * @return 返回子级
	 */
	@GetMapping(value = "/getDeptList")
	public R getSelfDeptList() {
		QueryWrapper<SysDept> sysDeptQueryWrapper = new QueryWrapper<>();
		sysDeptQueryWrapper.lambda()
				.select(SysDept::getDeptId,SysDept::getName,SysDept::getParentId,SysDept::getDeptLevel);
		KboparkUser user = SecurityUtils.getUser();
		if(!CommonUtil.isSuperManager(user)){
			sysDeptQueryWrapper.lambda()
					.eq(SysDept::getDeptId,user.getDeptId())
					.or()
					.eq(SysDept::getParentId,user.getDeptId());
		}
		List<SysDept> list = sysDeptService.list(sysDeptQueryWrapper);
		return R.ok(list);
	}


	/**
	 * 获取机构选中的菜单ID
	 * @param deptId
	 * @return
	 */
	@GetMapping("/deptMenuTree/{deptId}")
	public R getRoleTree(@PathVariable Integer deptId) {
		List<SysDeptMenu> list = sysDeptMenuService.list(Wrappers.<SysDeptMenu>lambdaQuery().eq(SysDeptMenu::getDeptId, deptId));
		return R.ok(list.stream().map(SysDeptMenu::getMenuId).collect(Collectors.toList()));
	}

	/**
	 * 更新部门菜单
	 *
	 * @param deptMenuVO
	 * @return
	 */
	@SysLog("更新部门菜单")
	@PutMapping("/menu")
	@PreAuthorize("@pms.hasPermission('sys_dept_perm')")
	public R saveRoleMenus(@RequestBody DeptMenuVO deptMenuVO) {
		return R.ok(sysDeptMenuService.saveDeptMenus(deptMenuVO.getDeptId(), deptMenuVO.getMenuIds()));
	}


	/***
	 * 根据等级查询
	 * @param level
	 * @return
	 */
	@GetMapping("/listByLevel/{level}")
	public R listByDeptLevel(@PathVariable Integer level) {
		QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().le(SysDept::getDeptLevel, level);
		return R.ok(sysDeptService.list(queryWrapper));
	}

}
