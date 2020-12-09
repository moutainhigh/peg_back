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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 优惠券领取记录
 *
 * @author pigx code generator
 * @date 2020-09-01 09:35:43
 */
@Data
@TableName("kboparkx_coupon_receive")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "优惠券领取记录")
public class CouponReceive extends Model<CouponReceive> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 券编号
	 */
	@ApiModelProperty(value = "券编号")
	private String couponSerialNumber;
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
	 * 优惠券有限期限
	 */
	@ApiModelProperty(value = "开始时间")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd"
	)
	private Date couponStartTime;
	/**
	 * 优惠券有限期限
	 */
	@ApiModelProperty(value = "结束时间")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd"
	)
	private Date couponEndTime;
	/**
	 * 券使用规则
	 */
	@ApiModelProperty(value = "券使用规则")
	private String couponRule;
	/**
	 * 领券的红包金额
	 */
	@ApiModelProperty(value = "领券的红包金额")
	private Double couponMoney;
	/**
	 * 领券的数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer couponNumber;
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
	 * 会员logo
	 */
	@ApiModelProperty(value = "会员logo")
	private String memberLogo;
	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	private String orderNumber;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
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
	@TableLogic
	@ApiModelProperty(value = "")
	private String delFlag;
	/**
	 * 启用禁用状态  0 禁用   1 启用
	 */
	@ApiModelProperty(value = "启用禁用状态  0 禁用   1 启用")
	private String lockFlag;
	/**
	 * 使用状态
	 */
	@ApiModelProperty(value = "使用状态")
	private Integer usedStatus;
	/**
	 * 使用时间
	 */
	@ApiModelProperty(value = "使用时间")
	private LocalDateTime usedTime;
}
