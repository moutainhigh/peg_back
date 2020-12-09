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

import cn.qdzhhl.kbopark.admin.api.dto.UserInfo;
import cn.qdzhhl.kbopark.admin.api.entity.SysUser;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.entity.MemberInfo;
import com.kbopark.operation.enums.LockStatusEnum;
import com.kbopark.operation.service.MemberInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 会员信息表
 *
 * @author laomst
 * @date 2020-08-31 14:50:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/memberinfo")
@Api(value = "memberinfo", tags = "会员信息表管理")
public class MemberInfoController {

	private final MemberInfoService memberInfoService;

	/**
	 * 分页查询
	 *
	 * @param page       分页对象
	 * @param memberInfo 会员信息表
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_memberinfo_view')")
	public R getMemberInfoPage(Page page, MemberInfo memberInfo) {
		return R.ok(memberInfoService.page(page, Wrappers.query(memberInfo)));
	}


	/**
	 * 通过id查询会员信息表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_memberinfo_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(memberInfoService.getById(id));
	}

	/**
	 * 新增会员信息表
	 *
	 * @param memberInfo 会员信息表
	 * @return R
	 */
	@ApiOperation(value = "新增会员信息表", notes = "新增会员信息表")
	@SysLog("新增会员信息表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_memberinfo_add')")
	public R save(@RequestBody MemberInfo memberInfo) {
		return R.ok(memberInfoService.save(memberInfo));
	}

	/**
	 * 修改会员信息表
	 *
	 * @param memberInfo 会员信息表
	 * @return R
	 */
	@ApiOperation(value = "修改会员信息表", notes = "修改会员信息表")
	@SysLog("修改会员信息表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_memberinfo_edit')")
	public R updateById(@RequestBody MemberInfo memberInfo) {
		return R.ok(memberInfoService.updateById(memberInfo));
	}

	/**
	 * 通过id删除会员信息表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除会员信息表", notes = "通过id删除会员信息表")
	@SysLog("通过id删除会员信息表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_memberinfo_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(memberInfoService.removeById(id));
	}


	/**
	 * 通过手机号查询会员信息
	 *
	 * @param phone
	 * @return
	 */
	@ApiOperation(value = "通过手机号会员信息表", notes = "通过手机号会员信息表")
	@SysLog("通过手机号会员信息表")
	@GetMapping("/findByPhone/{phone}")
	@Inner
	public R findByPhone(@PathVariable String phone) {
		MemberInfo memberInfo = memberInfoService.findByMobile(phone);
		if(memberInfo == null){
			memberInfo = new MemberInfo();
			memberInfo.setPhone(phone);
			memberInfo.setLockFlag(LockStatusEnum.UNLOCK.getCode());
			memberInfoService.save(memberInfo);
		}
		UserInfo userInfo = new UserInfo();
		SysUser sysUser = new SysUser();
		sysUser.setUsername(memberInfo.getPhone());
		sysUser.setPhone(memberInfo.getPhone());
		sysUser.setRealName(memberInfo.getRealName());
		sysUser.setAvatar(memberInfo.getAvatar());
		sysUser.setLockFlag(LockStatusEnum.UNLOCK.getCode());
		userInfo.setSysUser(sysUser);
		return R.ok(userInfo);
	}
}
