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
 * 用户退款申请
 *
 * @author pigx code generator
 * @date 2020-10-28 14:36:17
 */
@Data
@TableName("kboparkx_consumer_refund_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户退款申请")
public class ConsumerRefundOrder extends Model<ConsumerRefundOrder> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	private String orderNumber;
	/**
	 * 退款方式类别
	 */
	@ApiModelProperty(value = "退款方式类别")
	private Integer wayType;
	/**
	 * 退款原因类别
	 */
	@ApiModelProperty(value = "退款原因类别")
	private Integer reasonType;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer num;

	/**
	 * 退款金额
	 */
	@ApiModelProperty(value = "退款金额")
	private Double money;
	/**
	 * 用户备注
	 */
	@ApiModelProperty(value = "用户备注")
	private String remark;
	/**
	 * 处理状态
	 */
	@ApiModelProperty(value = "处理状态")
	private String processState;
	/**
	 * 处理时间
	 */
	@ApiModelProperty(value = "处理时间")
	private LocalDateTime processTime;
	/**
	 * 处理结果
	 */
	@ApiModelProperty(value = "处理结果")
	private String processResult;
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
	@ApiModelProperty(value = "")
	@TableLogic
	private String delFlag;
}
