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

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kbopark.kbpay.dto.BizConsumerOrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 支付订单
 *
 * @author laomst
 * @date 2020-09-03 16:55:15
 */
@Data
@TableName("kboparkx_pay_trade_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "支付订单")
public class PayTradeOrder extends Model<PayTradeOrder> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 支付通道关联订单id，这个id是传给第三方支付平台的id
	 */
	@ApiModelProperty(value = "支付通道关联订单id，这个id是传给第三方支付平台的id")
	private String payChannelOrderId;

	/**
	 * 所属运营商id
	 */
	@ApiModelProperty(value = "所属运营商id")
	private Integer operatorId;
	/**
	 * 所属渠道商
	 */
	@ApiModelProperty(value = "所属渠道商")
	private Integer distributorId;

	/**
	 * 商户id
	 */
	@ApiModelProperty(value = "商户id")
	private Integer merchantId;
	/**
	 * 业务流水号
	 */
	@ApiModelProperty(value = "业务流水号")
	private String tradeBizNumber;
	/**
	 * 支付时第三方平台的服务url
	 */
	@ApiModelProperty(value = "支付时第三方平台的服务url")
	private String payServiceUrl;
	/**
	 * 商品id，现在这个字段和上面的业务流水号是一样的，现在这个字段还没有开始使用
	 */
	@ApiModelProperty(value = "商品id，现在这个字段和上面的业务流水号是一样的，现在这个字段还没有开始使用")
	private String bizGoodOrderId;
	/**
	 * 商户支付通道id
	 */
	@ApiModelProperty(value = "商户支付通道id")
	private Integer merchantPayChannelId;
	/**
	 * 发起支付时收款通道的类型
	 */
	@ApiModelProperty(value = "发起支付时收款通道的类型")
	private String payChannelValue;
	/**
	 * 发起支付的时候收款通道的配置参数的快照
	 */
	@ApiModelProperty(value = "发起支付的时候收款通道的配置参数的快照")
	private String payChannelParamsSnapshot;
	/**
	 * 支付金额
	 */
	@ApiModelProperty(value = "支付金额")
	private BigDecimal money;
	/**
	 * 商品描述，有name和money两个字段
	 */
	@ApiModelProperty(value = "商品描述，有name和money两个字段")
	private String goods;
	/**
	 * 支付状态， 0-未支付 1-支付中 2-支付成功
	 */
	@ApiModelProperty(value = "支付状态， 0-未支付 1-支付中 2-支付成功")
	private Integer payStatus;
	/**
	 * 发起支付时设置的通知url
	 */
	@ApiModelProperty(value = "发起支付时设置的通知url")
	private String notifyUrl;
	/**
	 * 发起支付的时候设置的返回页面url，有可能是接口传的，也有可能是系统中配置的
	 */
	@ApiModelProperty(value = "发起支付的时候设置的返回页面url，有可能是接口传的，也有可能是系统中配置的")
	private String returnUrl;
	/**
	 * 通知次数
	 */
	@ApiModelProperty(value = "通知次数")
	private Integer notifyCount;
	/**
	 * 最后一次收到通知的时间
	 */
	@ApiModelProperty(value = "最后一次收到通知的时间")
	private LocalDateTime lastNotifyTime;
	/**
	 * 支付成功的时间
	 */
	@ApiModelProperty(value = "支付成功的时间")
	private LocalDateTime paySuccessTime;
	/**
	 * 租户id，注意这个表的租户是手动处理的
	 */
	@ApiModelProperty(value = "租户id，注意这个表的租户是手动处理的", hidden = true)
	private String tenantId;
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
	 * 支付方式
	 */
	@ApiModelProperty(value = "支付方式")
	private String payMethod;
	/**
	 * 订单描述
	 */
	@ApiModelProperty(value = "订单描述")
	private String orderDescription;

	public PayTradeOrder() {}

	public PayTradeOrder(BizConsumerOrderDTO bizOrder,
						 MerchantPayChannel merchantPayChannel,
						 SystemPayChannel systemPayChannel,
						 String returnUrl) {
		update(bizOrder, merchantPayChannel, systemPayChannel, returnUrl);
	}

	public void update(BizConsumerOrderDTO bizOrder,
					   MerchantPayChannel merchantPayChannel,
					   SystemPayChannel systemPayChannel,
					   String returnUrl) {
		tradeBizNumber = bizOrder.getOrderNumber();
		operatorId = bizOrder.getOperationId();
		distributorId = bizOrder.getChannelId();
		merchantId = bizOrder.getMerchantId();
		bizGoodOrderId = tradeBizNumber;
		merchantPayChannelId = merchantPayChannel.getId();
		payChannelValue = merchantPayChannel.getChannelValue();
		payChannelParamsSnapshot = merchantPayChannel.getParams();
		payStatus = 0;
		notifyUrl = systemPayChannel.getNotifyUrl();
		this.returnUrl = StrUtil.isNotBlank(returnUrl) ? returnUrl : systemPayChannel.getReturnUrl();
		notifyCount = 0;
		money = BigDecimal.valueOf(bizOrder.getMoney());
		payServiceUrl = systemPayChannel.getServiceUrl();
		orderDescription = bizOrder.getProductDes();
		payChannelOrderId = bizOrder.getOrderNumber(); // 传给第三方系统的订单id，默认使用业务订单号
	}


	@JsonIgnore
    public Map<String, String> getParamsMap() {
    	return JSON.parseObject(payChannelParamsSnapshot, new TypeReference<Map<String, String>>(){});
    }

}
