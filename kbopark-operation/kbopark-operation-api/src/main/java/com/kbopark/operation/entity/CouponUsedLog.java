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
 * 优惠券使用记录
 *
 * @author pigx code generator
 * @date 2020-09-04 10:15:04
 */
@Data
@TableName("kboparkx_coupon_used_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "优惠券使用记录")
public class CouponUsedLog extends Model<CouponUsedLog> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	@ApiModelProperty(value = "主键ID")
	private Integer recordId;
	/**
	 * 领取记录ID
	 */
	@ApiModelProperty(value = "领取记录ID")
	private Integer receiveId;
	/**
	 * 商家ID
	 */
	@ApiModelProperty(value = "商家ID")
	private Integer merchantId;
	/**
	 * 商家名称
	 */
	@ApiModelProperty(value = "商家名称")
	private String merchantName;
	/**
	 * 渠道商ID
	 */
	@ApiModelProperty(value = "渠道商ID")
	private Integer channelId;
	/**
	 * 渠道商名称
	 */
	@ApiModelProperty(value = "渠道商名称")
	private String channelName;
	/**
	 * 运营商ID
	 */
	@ApiModelProperty(value = "运营商ID")
	private Integer operationId;
	/**
	 * 运营商名称
	 */
	@ApiModelProperty(value = "运营商名称")
	private String operationName;
	/**
	 * 券编号
	 */
	@ApiModelProperty(value = "券编号")
	private String couponSerialNumber;
	/**
	 * 优惠券名称
	 */
	@ApiModelProperty(value = "优惠券名称")
	private String couponName;
	/**
	 * 券类型，红包或优惠券
	 */
	@ApiModelProperty(value = "券类型，红包或优惠券")
	private String couponType;
	/**
	 * 有效期限
	 */
	@ApiModelProperty(value = "有效期限")
	private String couponLimitTime;
	/**
	 * 券使用规则
	 */
	@ApiModelProperty(value = "券使用规则")
	private String couponRule;
	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	private Integer memberId;
	/**
	 * 会员标识
	 */
	@ApiModelProperty(value = "会员标识")
	private String memberCode;
	/**
	 * 会员姓名
	 */
	@ApiModelProperty(value = "会员姓名")
	private String memberName;
	/**
	 * 会员手机号
	 */
	@ApiModelProperty(value = "会员手机号")
	private String memberPhone;
	/**
	 * 用户logo
	 */
	@ApiModelProperty(value = "用户logo")
	private String memberLogo;
	/**
	 * 创建时间即使用时间
	 */
	@ApiModelProperty(value = "创建时间即使用时间")
	private LocalDateTime createTime;
	/**
	 * 创建人姓名
	 */
	@ApiModelProperty(value = "创建人姓名")
	private String createBy;
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
	/**
	 * 订单号(本平台)
	 */
	@ApiModelProperty(value = "订单号(本平台)")
	private String orderNumber;
	/**
	 * 应付金额
	 */
	@ApiModelProperty(value = "应付金额")
	private Double payable;
	/**
	 * 折扣金额
	 */
	@ApiModelProperty(value = "折扣金额")
	private Double discount;
	/**
	 * 实付金额
	 */
	@ApiModelProperty(value = "实付金额")
	private Double money;
	/**
	 * 异步通知时间
	 */
	@ApiModelProperty(value = "异步通知时间")
	private String notifyTime;
}
