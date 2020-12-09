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

package com.kbopark.operation.api.merchantapi;

import cn.qdzhhl.kbopark.admin.api.dto.UserDTO;
import cn.qdzhhl.kbopark.admin.api.feign.RemoteMerchantUserService;
import cn.qdzhhl.kbopark.admin.api.vo.UserVO;
import cn.qdzhhl.kbopark.common.core.util.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/merchant-user-api")
@Api(value = "merchant-user-api", tags = "【商家端用户管理相关接口】")
public class MerchantUserApi {

	private final RemoteMerchantUserService remoteMerchantUserService;

	@ApiOperation("分页列表查询")
	@GetMapping("/page")
	public R getUserPage(@RequestParam("current") Integer current, @RequestParam("size") Integer size) {
		return remoteMerchantUserService.getUserPage(current, size);
	}

	@ApiOperation("用户详情")
	@GetMapping("/info/{id}")
	public R<UserVO> userInfo(@PathVariable("id") Integer id) {
		return remoteMerchantUserService.user(id);
	}

	@ApiOperation("删除用户")
	@DeleteMapping("/remove/{id}")
	public R removeUser(@PathVariable("id") Integer id) {
		return remoteMerchantUserService.userDel(id);
	}

	@ApiOperation("添加用户")
	@PostMapping("/add")
	public R addUser(@RequestBody UserDTO userDto) {
		return remoteMerchantUserService.user(userDto);
	}

	@ApiOperation("修改用户")
	@PutMapping("/update")
	public R getUserInfo(@RequestBody UserDTO userDto) {
		return remoteMerchantUserService.updateUser(userDto);
	}


	@ApiOperation("用户详情")
	@PostMapping("/reset-passwork")
	public R resetPassword(@RequestBody UserDTO userDto) {
		return remoteMerchantUserService.resetDefaultPass(userDto);
	}

	@ApiOperation("修改个人信息")
	@PostMapping("/update-mine")
	public R updateMine(@RequestBody UserDTO userDto) {
		return remoteMerchantUserService.updateUserInfo(userDto);
	}


}
