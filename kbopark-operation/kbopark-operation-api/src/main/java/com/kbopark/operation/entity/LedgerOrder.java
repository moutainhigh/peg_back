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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 分账订单记录
 *
 * @author pigx code generator
 * @date 2020-09-14 13:50:42
 */
@Data
@TableName("kboparkx_ledger_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分账订单记录")
public class LedgerOrder extends Model<LedgerOrder> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty("主键id")
	@TableId(
		value = "id",
		type = IdType.ASSIGN_ID
	)
	private Long id;
	/**
	 * 分账编号
	 */
	@ApiModelProperty(value = "分账编号")
	private String ledgerSerialNumber;
	/**
	 * 分账类别
	 */
	@ApiModelProperty(value = "分账类别")
	private Integer ledgerCategory;
	/**
	 * 分账批次号
	 */
	@ApiModelProperty(value = "分账批次号")
	private String ledgerBatchNo;
	/**
	 * 第三方分账标识
	 */
	@ApiModelProperty(value = "第三方分账标识")
	private String ledgerThirdNo;
	/**
	 * 分账状态  已结算/未结算
	 */
	@ApiModelProperty(value = "分账状态  已结算/未结算")
	private String ledgerStatus;
	/**
	 * 运营商ID
	 */
	@ApiModelProperty(value = "运营商ID")
	private Integer operationId;
	/**
	 * 运营商编号（备用）
	 */
	@ApiModelProperty(value = "运营商编号（备用）")
	private String operationName;
	/**
	 * 运营商分账比例
	 */
	@ApiModelProperty(value = "运营商分账比例")
	private Double operationPercent;
	/**
	 * 运营商分账金额
	 */
	@ApiModelProperty(value = "运营商分账金额")
	private Double operationLedgerAccount;
	/**
	 * 平台ID
	 */
	@ApiModelProperty(value = "平台ID")
	private Integer platformId;
	/**
	 * 平台编号
	 */
	@ApiModelProperty(value = "平台编号")
	private String platformName;
	/**
	 * 平台分账比例
	 */
	@ApiModelProperty(value = "平台分账比例")
	private Double platformPercent;
	/**
	 * 平台分账金额
	 */
	@ApiModelProperty(value = "平台分账金额")
	private Double platformLegerAccount;
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
	 * 商家分账比例
	 */
	@ApiModelProperty(value = "商家分账比例")
	private Double merchantPercent;
	/**
	 * 商家分账金额
	 */
	@ApiModelProperty(value = "商家分账金额")
	private Double merchantLegerAccount;
	/**
	 * 消费订单号
	 */
	@ApiModelProperty(value = "消费订单号")
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
	 * 实付金额（用户实际付款金额）
	 */
	@ApiModelProperty(value = "实付金额（用户实际付款金额）")
	private Double money;
	/**
	 * 分账日期   yyyy-MM-dd
	 */
	@ApiModelProperty(value = "分账日期 yyyy-MM-dd")
	private LocalDate ledgerTime;

	/**
	 * 核对账单生成状态  WAIT 待生成  CREATED 已生成
	 */
	@ApiModelProperty(value = "分账订单审核状态")
	private String ledgerCreateStatus;



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
