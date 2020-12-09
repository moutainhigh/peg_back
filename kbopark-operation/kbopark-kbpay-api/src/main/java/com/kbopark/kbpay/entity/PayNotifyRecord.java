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
 * 回调记录
 *
 * @author laomst
 * @date 2020-09-04 10:24:32
 */
@Data
@TableName("kboparkx_pay_notify_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "回调记录")
public class PayNotifyRecord extends Model<PayNotifyRecord> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 支付流水号
	 */
	@ApiModelProperty(value = "支付流水号")
	private String tradeBizNumber;
	/**
	 * 回调报文
	 */
	@ApiModelProperty(value = "回调报文")
	private String requestBody;
	/**
	 * 响应报文
	 */
	@ApiModelProperty(value = "响应报文")
	private String responseBody;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
}
