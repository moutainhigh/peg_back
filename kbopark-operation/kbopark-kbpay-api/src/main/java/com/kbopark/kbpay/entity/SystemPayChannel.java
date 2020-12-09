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

package com.kbopark.kbpay.entity;

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
 * 系统支付通道
 *
 * @author laomst
 * @date 2020-09-03 10:05:23
 */
@Data
@TableName("kboparkx_system_pay_channel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统支付通道")
public class SystemPayChannel extends Model<SystemPayChannel> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 通道名称
	 */
	@ApiModelProperty(value = "通道名称")
	private String name;
	/**
	 * 通道值
	 */
	@ApiModelProperty(value = "通道值")
	private String value;
	/**
	 * 支付通道服务的url
	 */
	@ApiModelProperty(value = "支付通道服务的url")
	private String serviceUrl;
	/**
	 * 支付结果通知url
	 */
	@ApiModelProperty(value = "支付结果通知url")
	private String notifyUrl;
	/**
	 * 返回页面url
	 */
	@ApiModelProperty(value = "返回页面url")
	private String returnUrl;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String paramColumns;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime createTime;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime updateTime;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	@TableLogic
	private Integer delFlag;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer status;
}
