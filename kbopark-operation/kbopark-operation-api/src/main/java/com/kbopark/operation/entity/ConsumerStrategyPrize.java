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
import com.kbopark.operation.vo.MerchantCouponVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商家消费策略奖品表
 *
 * @author laomst
 * @date 2020-09-08 15:31:49
 */
@Data
@TableName("kboparkx_consumer_strategy_prize")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商家消费策略奖品表")
public class ConsumerStrategyPrize extends Model<ConsumerStrategyPrize> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 消费策略id
	 */
	@ApiModelProperty(value = "消费策略id")
	private Integer strategyId;
	/**
	 * 奖品类型 coupon 优惠券 redpack 红包
	 */
	@ApiModelProperty(value = "奖品类型 coupon 优惠券 redpack 红包")
	private String prizeType;
	/**
	 * 红包或者卡券的id
	 */
	@ApiModelProperty(value = "红包或者卡券的id")
	private Integer couponId;
	/**
	 * 赠送数量
	 */
	@ApiModelProperty(value = "赠送数量")
	private Integer couponNum;

//	@TableField(exist = false)
//	private MerchantCouponVO prizeInfo;
}
