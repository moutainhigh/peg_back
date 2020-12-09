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

package com.kbopark.kbpay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消费订单
 *
 * @author pigx code generator
 * @date 2020-09-03 10:17:02
 */
@Data
@ApiModel(value = "消费订单")
public class BizConsumerOrderDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 订单号(本平台)
	 */
	@ApiModelProperty(value = "订单号(本平台)")
	private String orderNumber;
	/**
	 * 第三方单号
	 */
	@ApiModelProperty(value = "第三方单号")
	private String thirdNumber;
	/**
	 * 商品编号
	 */
	@ApiModelProperty(value = "商品编号")
	private String productCode;
	/**
	 * 商品描述
	 */
	@ApiModelProperty(value = "商品描述")
	private String productDes;
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
	 * 已支付金额
	 */
	@ApiModelProperty(value = "已支付金额")
	private Double payed;
	/**
	 * 是否使用优惠券  0  未使用  1  已使用
	 */
	@ApiModelProperty(value = "是否使用优惠券  0  未使用  1  已使用")
	private Integer couponUsed;
	/**
	 * 优惠券名称
	 */
	@ApiModelProperty(value = "优惠券名称")
	private String couponName;
	/**
	 * 优惠券编号
	 */
	@ApiModelProperty(value = "优惠券编号")
	private String couponSerialNumber;
	/**
	 * 优惠券类型
	 */
	@ApiModelProperty(value = "优惠券类型")
	private String couponType;
	/**
	 * 支付状态  WAIT 待支付   SUCCESS  支付成功  FAIL 支付失败
	 */
	@ApiModelProperty(value = "支付状态  WAIT 待支付   SUCCESS  支付成功  FAIL 支付失败")
	private String notifyStatus;
	/**
	 * 异步通知时间
	 */
	@ApiModelProperty(value = "异步通知时间")
	private String notifyTime;
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
	 * 会员头像地址
	 */
	@ApiModelProperty(value = "会员头像地址")
	private String memberAvatar;
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
	 * 删除标记
	 */
	@ApiModelProperty(value = "")
	private String delFlag;
}
