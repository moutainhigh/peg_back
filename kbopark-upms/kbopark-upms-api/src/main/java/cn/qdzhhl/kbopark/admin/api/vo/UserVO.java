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

package cn.qdzhhl.kbopark.admin.api.vo;

import cn.qdzhhl.kbopark.admin.api.entity.SysRole;
import cn.qdzhhl.kbopark.common.core.sensitive.Sensitive;
import cn.qdzhhl.kbopark.common.core.sensitive.SensitiveTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author kbopark
 * @date 2017/10/29
 */
@Data
@ApiModel(value = "前端用户展示对象")
public class UserVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键")
	private Integer userId;

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;

	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名")
	private String realName;

	/**
	 * 邮箱
	 */
	@ApiModelProperty(value = "邮箱")
	private String email;

	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;

	/**
	 * 随机盐
	 */
	@ApiModelProperty(value = "随机盐")
	private String salt;

	/**
	 * 微信openid
	 */
	@ApiModelProperty(value = "微信open id")
	private String wxOpenid;

	/**
	 * QQ openid
	 */
	@ApiModelProperty(value = "qq open id")
	private String qqOpenid;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 0-正常，1-删除
	 */
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private String delFlag;

	/**
	 * 锁定标记
	 */
	@ApiModelProperty(value = "锁定标记,0:正常,9:已锁定")
	private String lockFlag;

	/**
	 * 手机号
	 */
//	@Sensitive(type = SensitiveTypeEnum.MOBILE_PHONE)
	@ApiModelProperty(value = "手机号")
	private String phone;

	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String avatar;

	/**
	 * 部门ID
	 */
	@ApiModelProperty(value = "所属部门")
	private Integer deptId;

	/**
	 * 商家ID
	 */
	@ApiModelProperty(value = "用户所属商家id")
	private Integer merchantId;

	/**
	 * 用户类别
	 */
	@ApiModelProperty(value = "用户类别")
	private Integer userType;

	/**
	 * 推广码
	 */
	@ApiModelProperty(value = "推广码")
	private String promoteCode;

	/**
	 * 租户ID
	 */
	@ApiModelProperty(value = "所属租户")
	private Integer tenantId;

	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "所属部门名称")
	private String deptName;

	/**
	 * 角色列表
	 */
	@ApiModelProperty(value = "拥有的角色列表")
	private List<SysRole> roleList;

}
