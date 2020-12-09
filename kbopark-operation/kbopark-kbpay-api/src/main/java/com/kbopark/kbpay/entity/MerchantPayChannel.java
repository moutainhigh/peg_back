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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 商户支付通道设置
 *
 * @author laomst
 * @date 2020-09-03 11:41:58
 */
@Data
@TableName("kboparkx_merchant_pay_channel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户支付通道设置")
public class MerchantPayChannel extends Model<MerchantPayChannel> {
	private static final long serialVersionUID = 1L;

	public static final String PAGE_COLUMNS = "kboparkx_merchant_pay_channel.*, (select status from kboparkx_system_pay_channel where kboparkx_system_pay_channel.value=kboparkx_merchant_pay_channel.channel_value) as channel_status";
	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer operatorId;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer distributorId;
	/**
	 * 机构id
	 */
	@ApiModelProperty(value = "机构id")
	private Integer merchantId;
	/**
	 * 支付通道类型
	 */
	@ApiModelProperty(value = "支付通道类型")
	private String channelValue;
	/**
	 * 参数设置
	 */
	@ApiModelProperty(value = "参数设置")
	private String params;
	/**
	 * 是否被激活，同一个机构下面，同时只能有一个处于激活状态
	 */
	@ApiModelProperty(value = "是否被激活，同一个机构下面，同时只能有一个处于激活状态")
	private Integer isActivated;
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
	private Integer status;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	@TableLogic
	private Integer delFlag;

	@ApiModelProperty("系统支付通道是否可用")
	@TableField(exist = false)
	private Integer channelStatus;

	@JsonIgnore
	public Map<String, String> getParamsMap() {
		return JSON.parseObject(params, new TypeReference<Map<String, String>>(){});
	}
}
