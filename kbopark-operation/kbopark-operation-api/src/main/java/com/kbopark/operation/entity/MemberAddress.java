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
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员收货地址
 *
 * @author pigx code generator
 * @date 2020-10-27 10:29:05
 */
@Data
@TableName("kboparkx_member_address")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "会员收货地址")
public class MemberAddress extends Model<MemberAddress> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 当前会员ID
	 */
	@ApiModelProperty(value = "当前会员ID")
	private Integer memberId;
	/**
	 * 收货人
	 */
	@ApiModelProperty(value = "收货人")
	private String takeName;
	/**
	 * 收货人手机号
	 */
	@ApiModelProperty(value = "收货人手机号")
	private String takePhone;
	/**
	 * 收货地址
	 */
	@ApiModelProperty(value = "收货地址")
	private String takeAddress;
	/**
	 * 收货详细地址
	 */
	@ApiModelProperty(value = "收货详细地址")
	private String takeAddressDetail;
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
	@TableLogic
	private String delFlag;
}
