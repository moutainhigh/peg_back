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
import cn.qdzhhl.kbopark.admin.api.dto.UserDTO;
import cn.qdzhhl.kbopark.admin.api.entity.SysUser;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.admin.api.vo.UserVO;
import cn.qdzhhl.kbopark.admin.service.SysUserService;
import cn.qdzhhl.kbopark.admin.utils.CommonUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author kbopark
 * @date 2018/12/16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchantUser")
@Api(value = "user", tags = "用户管理模块")
public class MerchantUserController {

	private final SysUserService userService;

	/**
	 * 获取指定用户全部信息
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/info/{username}")
	public R info(@PathVariable String username) {
		SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
		if (user == null) {
			return R.failed(null, String.format("用户信息为空 %s", username));
		}
		return R.ok(userService.findUserInfo(user));
	}

	/**
	 * 通过ID查询用户信息
	 * @param id ID
	 * @return 用户信息
	 */
	@GetMapping("/{id}")
	public R<UserVO> user(@PathVariable Integer id) {
		return R.ok(userService.selectUserVoById(id));
	}

	/**
	 * 根据用户名查询用户信息
	 * @param username 用户名
	 * @return
	 */
	@GetMapping("/details/{username}")
	public R user(@PathVariable String username) {
		SysUser condition = new SysUser();
		condition.setUsername(username);
		return R.ok(userService.getOne(new QueryWrapper<>(condition)));
	}

	/**
	 * 删除用户信息
	 * @param id ID
	 * @return R
	 */
	@SysLog("删除用户信息")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('merchant_account_del')")
	@ApiOperation(value = "删除用户", notes = "根据ID删除用户")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
	public R userDel(@PathVariable Integer id) {
		if(id == 1){
			return R.failed("此用户不能删除");
		}
		SysUser sysUser = userService.getById(id);
		return R.ok(userService.deleteUserById(sysUser));
	}

	/**
	 * 渠道商或者商家添加用户，可新增推广员和商家管理账号
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@SysLog("添加用户")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('merchant_account_add')")
	public R user(@RequestBody UserDTO userDto) {
		KboparkUser user = SecurityUtils.getUser();
		userDto.setDeptId(user.getDeptId());
		if(user.getUserType() > userDto.getUserType().intValue()){
			return R.failed("权限不足,不能创建上级用户");
		}

		//如果是渠道商或推广员新增用户，用户类别由前方传入，如果是商家新增则默认商家下级
		if(UserTypeEnum.Merchant.getCode().equals(user.getUserType())){
			userDto.setUserType(UserTypeEnum.MerchantLower.getCode());
		}
		//创建推广员时自动生成推广码
		if(UserTypeEnum.Promoter.getCode().equals(userDto.getUserType())){
			userDto.setPromoteCode(RandomUtil.randomString(12).toLowerCase());
		}

		return R.ok(userService.saveUser(userDto));
	}

	/**
	 * 更新用户信息
	 * @param userDto 用户信息
	 * @return R
	 */
	@SysLog("更新用户信息")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('merchant_account_edit')")
	public R updateUser(@Valid @RequestBody UserDTO userDto) {
		return R.ok(userService.updateUser(userDto));
	}

	/**
	 * 分页查询用户，需要区分推广员和商家
	 * @param page 参数集
	 * @param userDTO 查询参数列表
	 * @return 用户集合
	 */
	@GetMapping("/page")
	public R getUserPage(Page page, UserDTO userDTO) {
		//此处功能是分配给商家和渠道商使用得
		KboparkUser user = SecurityUtils.getUser();
		SysUser byId = userService.getById(user.getId());
		if(byId.getMerchantId() != null){
			//商家查询自身用户限制
			userDTO.setMerchantId(byId.getMerchantId());
		}else{
			//渠道商查询商家用户
			if(UserTypeEnum.Merchant.getCode().equals(userDTO.getUserType())){
				userDTO.setByMerchantId(true);
			}
		}
		//防止遍历上级用户
		if(!CommonUtil.isSuperManager(user)){
			if(userDTO.getUserType().intValue() < user.getUserType().intValue()){
				return R.failed("权限不足");
			}
		}
		return R.ok(userService.getUsersWithRolePage(page, userDTO));
	}

	/**
	 * 修改个人信息
	 * @param userDto userDto
	 * @return success/false
	 */
	@SysLog("修改个人信息")
	@PutMapping("/edit")
	public R updateUserInfo(@Valid @RequestBody UserDTO userDto) {
		return userService.updateUserInfo(userDto);
	}

	/**
	 * 重置用户密码（推广员、商家账户）
	 * @param userDto userDto
	 * @return success/false
	 */
	@SysLog("重置用户密码")
	@PostMapping("/resetPass")
	@PreAuthorize("@pms.hasPermission('channel_promoter_reset','merchant_account_reset')")
	public R resetDefaultPass(@Valid @RequestBody UserDTO userDto) {
		return userService.resetPassWord(userDto);
	}

	/**
	 * @param username 用户名称
	 * @return 上级部门用户列表
	 */
	@GetMapping("/ancestor/{username}")
	public R listAncestorUsers(@PathVariable String username) {
		return R.ok(userService.listAncestorUsers(username));
	}

}
