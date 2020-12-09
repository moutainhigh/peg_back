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

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 商家福利设置
 *
 * @author pigx code generator
 * @date 2020-09-09 15:53:58
 */
@Data
@TableName("kboparkx_merchant_welfare_setting")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商家福利设置")
public class MerchantWelfareSetting extends Model<MerchantWelfareSetting> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 商家ID
	 */
	@ApiModelProperty(value = "商家ID")
	private Integer merchantId;
	/**
	 * 消费金额峰值
	 */
	@ApiModelProperty(value = "消费金额峰值")
	private Double fullMoney;
	/**
	 * 权重
	 */
	@ApiModelProperty(value = "权重")
	private Integer weight;
	/**
	 * 赠送规则
	 */
	@ApiModelProperty(value = "赠送规则")
	private String ruleInfo;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	@ApiModelProperty(value = "")
	private String lockFlag;

	@TableField(exist = false)
	private String merOrderId;
}
