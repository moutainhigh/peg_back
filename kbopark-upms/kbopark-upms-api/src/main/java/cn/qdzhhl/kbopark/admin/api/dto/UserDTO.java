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

package cn.qdzhhl.kbopark.admin.api.dto;

import cn.qdzhhl.kbopark.admin.api.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author kbopark
 * @date 2017/11/5
 */
@Data
@ApiModel(value = "系统用户传输对象")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {

	/**
	 * 角色ID
	 */
	@ApiModelProperty(value = "角色id集合")
	private List<Integer> role;

	/**
	 * 部门id
	 */
	@ApiModelProperty(value = "部门id")
	private Integer deptId;

	/**
	 * 新密码
	 */
	@ApiModelProperty(value = "新密码")
	private String newpassword1;


	/**
	 * 归属部门限制
	 */
	@ApiModelProperty(value = "归属部门限制")
	private Integer limitDeptId;

	/**
	 * 商户ID是否是查询条件
	 */
	@ApiModelProperty(value = "商户ID是否是查询条件")
	private Boolean byMerchantId;

	/**
	 * 商家id
	 */
	@ApiModelProperty(value = "商家id")
	private Integer merchantId;


}
