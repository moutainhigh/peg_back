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

package cn.qdzhhl.kbopark.admin.api.feign;

import cn.qdzhhl.kbopark.admin.api.dto.UserDTO;
import cn.qdzhhl.kbopark.admin.api.vo.UserVO;
import cn.qdzhhl.kbopark.common.core.constant.ServiceNameConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author kbopark
 * @date 2018/6/22
 */
@FeignClient(contextId = "remoteMerchantUserService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteMerchantUserService {

	String BASE_URL = "/merchantUser";

	/**
	 * 通过ID查询用户信息
	 *
	 * @param id ID
	 * @return 用户信息
	 */
	@GetMapping(BASE_URL + "/{id}")
	R<UserVO> user(@PathVariable("id") Integer id);


	/**
	 * 删除用户信息
	 *
	 * @param id ID
	 * @return R
	 */
	@DeleteMapping(BASE_URL + "/{id}")
	R userDel(@PathVariable("id") Integer id);

	/**
	 * 渠道商或者商家添加用户，可新增推广员和商家管理账号
	 *
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@PostMapping(BASE_URL)
	R user(@RequestBody UserDTO userDto);

	/**
	 * 更新用户信息
	 *
	 * @param userDto 用户信息
	 * @return R
	 */
	@PutMapping(BASE_URL)
	R updateUser(@Valid @RequestBody UserDTO userDto);

	/**
	 * 分页查询用户，需要区分推广员和商家
	 *
	 * @return 用户集合
	 */
	@GetMapping(BASE_URL + "/page")
	R getUserPage(@RequestParam("current") Integer current,@RequestParam("size") Integer size);

	/**
	 * 修改个人信息
	 *
	 * @param userDto userDto
	 * @return success/false
	 */
	@PutMapping(BASE_URL + "/edit")
	R updateUserInfo(@Valid @RequestBody UserDTO userDto);

	/**
	 * 重置用户密码（推广员、商家账户）
	 *
	 * @param userDto userDto
	 * @return success/false
	 */
	@PostMapping(BASE_URL + "/resetPass")
	R resetDefaultPass(@Valid @RequestBody UserDTO userDto);

}
