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

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.kbopark.operation.enums.CouponTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商家消费策论
 *
 * @author laomst
 * @date 2020-09-02 09:43:14
 */
@Data
@TableName("kboparkx_merchant_consumer_strategy")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商家消费策论")
public class MerchantConsumerStrategy extends Model<MerchantConsumerStrategy> implements Comparable<MerchantConsumerStrategy>{
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 商家id
	 */
	@ApiModelProperty(value = "商家id")
	private Integer merchantId;
	/**
	 * 消费金额
	 */
	@ApiModelProperty(value = "消费金额")
	private Double money;
	/**
	 * 权重
	 */
	@ApiModelProperty(value = "权重")
	private Integer weight;

	private String prizeType;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	@TableLogic
	private Integer delFlag;
	/**
	 * 1 启用 2禁用
	 */
	@ApiModelProperty(value = "1 启用 2禁用")
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

	@TableField(exist = false)
	private String merOrderId;

	@TableField(exist = false)
	private List<ConsumerStrategyPrize> couponList;
	@TableField(exist = false)
	private List<ConsumerStrategyPrize> prizeList;
	@TableField(exist = false)
	private List<ConsumerStrategyPrize> redpackList;

	public void completePrize() {
		String[] types = this.prizeType.split(",");
		// 如果没有优惠券，就把优惠券相关的信息置空
		if (!ArrayUtil.contains(types, CouponTypeEnum.COUPON_TYPE.getCode())) {
			couponList = new ArrayList<>();
		}
		// 如果没有红包，就把红包相关的内容置空
		if (!ArrayUtil.contains(types, CouponTypeEnum.RED_PACK_TYPE.getCode())) {
			redpackList = new ArrayList<>();
		}
		redpackList.forEach(item -> item.setPrizeType(CouponTypeEnum.RED_PACK_TYPE.getCode()));
		couponList.forEach(item -> item.setPrizeType(CouponTypeEnum.COUPON_TYPE.getCode()));
	}

	@Override
	public int compareTo(MerchantConsumerStrategy o) {
		return o.money.compareTo(this.money);
	}
}
