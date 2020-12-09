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

package com.kbopark.operation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kbopark.operation.dto.DtMemberDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 会员信息表
 *
 * @author laomst
 * @date 2020-08-31 14:50:53
 */
@Data
@TableName("kboparkx_member_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "会员信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberInfo extends Model<MemberInfo> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 会员编号
	 */
	@ApiModelProperty(value = "会员编号")
	private String serialNumber;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty("会员真实姓名")
	private String realName;
	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	private String nickName;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;
	/**
	 * 盐
	 */
	@ApiModelProperty(value = "盐")
	private String salt;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String phone;
	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String avatar;
	/**
	 * 性别 : 男  女
	 */
	@ApiModelProperty(value = "性别 : 男  女")
	private String sex;
	/**
	 * 邮箱
	 */
	@ApiModelProperty(value = "邮箱")
	private String email;
	/**
	 * 微信登录openId
	 */
	@ApiModelProperty(value = "微信登录openId")
	private String wxOpenid;
	/**
	 * 小程序openId
	 */
	@ApiModelProperty(value = "小程序openId")
	private String miniOpenid;
	/**
	 * QQ openId
	 */
	@ApiModelProperty(value = "QQ openId")
	private String qqOpenid;
	/**
	 * 码云 标识
	 */
	@ApiModelProperty(value = "码云 标识")
	private String giteeLogin;
	/**
	 * 开源中国 标识
	 */
	@ApiModelProperty(value = "开源中国 标识")
	private String oscId;
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
	 *
	 */
	@ApiModelProperty(value = "")
	private String delFlag;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String lockFlag;
	/**
	 * 所属租户
	 */
	@ApiModelProperty(value = "所属租户", hidden = true)
	private Integer tenantId;

	public MemberInfo() {
	}

	public MemberInfo(DtMemberDTO dtMemberDTO) {
		updateByDt(dtMemberDTO);
	}

	/**
	 * 根据地铁穿过来的信息修改系统中的用户信息
	 * @param dtMemberDTO
	 * @return
	 */
	public MemberInfo updateByDt(DtMemberDTO dtMemberDTO) {
		phone = dtMemberDTO.getPhone();
		avatar = dtMemberDTO.getAvatar();
		nickName = dtMemberDTO.getNickName();
		return this;
	}
}
