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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统设置
 *
 * @author laomst
 * @date 2020-09-01 14:26:51
 */
@Data
@TableName("kboparkx_system_settings")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统设置")
public class SystemSettings extends Model<SystemSettings> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 系统名称
	 */
	@ApiModelProperty(value = "系统名称")
	private String systemName;
	/**
	 * 备案号
	 */
	@ApiModelProperty(value = "备案号")
	private String aqNumber;
	/**
	 * 客服电话
	 */
	@ApiModelProperty(value = "客服电话")
	private String serviceTel;
	/**
	 * 注册协议
	 */
	@ApiModelProperty(value = "注册协议")
	private String registrationProtocol;
	/**
	 * 注册协议
	 */
	@ApiModelProperty(value = "使用协议")
	private String usageProtocol;
	/**
	 * 免责声明
	 */
	@ApiModelProperty(value = "免责声明")
	private String disclaimer;

	/**
	 * 卡券使用说明
	 */
	@ApiModelProperty(value = "卡券使用说明")
	private String couponRule;

	/**
	 * 优惠券预警比例
	 */
	@ApiModelProperty(value = "优惠券预警比例")
	private Double couponAlertPercent;
	/**
	 * 优惠券预警提示
	 */
	@ApiModelProperty(value = "优惠券预警提示")
	private String couponAlertTip;
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
}
