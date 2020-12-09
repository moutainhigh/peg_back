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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家会员
 *
 * @author laomst
 * @date 2020-08-31 15:16:27
 */
@Data
@TableName("kboparkx_merchant_member")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商家会员")
public class MerchantMember extends Model<MerchantMember> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 所属运营平台
	 */
	@ApiModelProperty(value = "所属运营平台")
	private Integer operatorId;
	/**
	 * 所属渠道商id
	 */
	@ApiModelProperty(value = "所属渠道商id")
	private Integer distributorId;
	/**
	 * 商家id
	 */
	@ApiModelProperty(value = "商家id")
	private Integer merchantId;
	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	private Integer memberId;
	/**
	 * 会员余额
	 */
	@ApiModelProperty(value = "会员余额")
	private BigDecimal balance;
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
