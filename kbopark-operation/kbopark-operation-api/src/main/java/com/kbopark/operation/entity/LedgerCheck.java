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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 分账核对账单
 *
 * @author pigx code generator
 * @date 2020-10-19 11:49:30
 */
@Data
@TableName("kboparkx_ledger_check")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分账核对账单")
public class LedgerCheck extends Model<LedgerCheck> {
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
	 * 分账核对编号
	 */
	@ApiModelProperty(value = "分账核对编号")
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
	 * 分账状态  SETTLED已结算 / UNSETTLE 待结算 / NOSETTLE驳回不予结算
	 */
	@ApiModelProperty(value = "分账状态  SETTLED已结算 / UNSETTLE 待结算 / NOSETTLE驳回不予结算")
	private String ledgerStatus;
	/**
	 * 分账日期   yyyy-MM-dd
	 */
	@ApiModelProperty(value = "分账日期   yyyy-MM-dd")
	private LocalDate ledgerTime;
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
	 * 运营商分账比例
	 */
	@ApiModelProperty(value = "运营商分账比例")
	private Double operationPercent;
	/**
	 * 运营商分账总金额
	 */
	@ApiModelProperty(value = "运营商分账总金额")
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
	 * 平台分账总金额
	 */
	@ApiModelProperty(value = "平台分账总金额")
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
	 * 商家分账总金额
	 */
	@ApiModelProperty(value = "商家分账总金额")
	private Double merchantLegerAccount;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 分账订单审核状态  WAIT 待审核  SUCCESS 审核通过可分账 AUDITING  审核中   FAILED  审核驳回不可分账
	 */
	@ApiModelProperty(value = "分账订单审核状态  WAIT 待审核  SUCCESS 审核通过可分账 AUDITING  审核中   FAILED  审核驳回不可分账")
	private String ledgerAuditStatus;
	/**
	 * 运营商审核状态   1  业务审核驳回  2  业务审核通过   3  财务审核驳回   4  财务审核通过
	 */
	@ApiModelProperty(value = "运营商审核状态   1  业务审核驳回  2  业务审核通过   3  财务审核驳回   4  财务审核通过")
	private Integer operationAuditStatus;
	/**
	 * 平台审核状态 1  业务审核驳回  2  业务审核通过   3  财务审核驳回   4  财务审核通过
	 */
	@ApiModelProperty(value = "平台审核状态 1  业务审核驳回  2  业务审核通过   3  财务审核驳回   4  财务审核通过")
	private Integer platformAuditStatus;
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
