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
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 消费订单
 *
 * @author pigx code generator
 * @date 2020-09-03 10:17:02
 */
@Data
@TableName("kboparkx_consumer_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "消费订单")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumerOrder extends Model<ConsumerOrder> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 订单号(本平台)
	 */
	@ApiModelProperty(value = "订单号(本平台)")
	private String orderNumber;

	/**
	 * 订单类型
	 */
	@ApiModelProperty(value = "订单类型)")
	private String orderType;

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
	 * 优惠力度，使用规则
	 */
	@ApiModelProperty(value = "使用规则")
	private String couponRule;
	/**
	 * 支付状态  WAIT 待支付   SUCCESS  支付成功  FAIL 支付失败
	 */
	@ApiModelProperty(value = "支付状态  WAIT 待支付   SUCCESS  支付成功  FAIL 支付失败")
	private String notifyStatus;
	/**
	 * 退款状态
	 */
	@ApiModelProperty(value = "退款状态")
	private String refundStatus;
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

	@TableField(exist = false)
	@ApiModelProperty("会员昵称")
	private String memberNickname;
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
	@TableLogic
	@ApiModelProperty(value = "")
	private String delFlag;

	/**
	 * 订单产品类别
	 */
	private Integer treeId;

	/**
	 * 游玩时间
	 */
	private String travelTime;
	/**
	 * 商品规格
	 */
	private String specType;
	/**
	 * 购买数量
	 */
	private Integer buyNum;

	/**
	 * 配送地址ID
	 */
	private Integer takeAddressId;
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

	@ApiModelProperty(value = "商品单价")
	private Double productPrice;


	@TableField(exist = false)
	private String merchantLogo;
	@TableField(exist = false)
	private String merchantCity;
	@TableField(exist = false)
	private String merchantAddress;
	@TableField(exist = false)
	private Integer num;
}
