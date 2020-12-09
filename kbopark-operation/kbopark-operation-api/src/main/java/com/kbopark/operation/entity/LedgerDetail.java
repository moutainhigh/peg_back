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
 * 分账明细
 *
 * @author pigx code generator
 * @date 2020-09-28 15:26:39
 */
@Data
@TableName("kboparkx_ledger_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分账明细")
public class LedgerDetail extends Model<LedgerDetail> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 商家ID
	 */
	@ApiModelProperty(value = "商家ID")
	private Integer merchantId;
	/**
	 * 运营商ID
	 */
	@ApiModelProperty(value = "运营商ID")
	private Integer operationId;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String ledgerBatchNo;
	/**
	 * 请求资管系统流水号
	 */
	@ApiModelProperty(value = "请求资管系统流水号")
	private String srcReqId;
	/**
	 * 交易码
	 */
	@ApiModelProperty(value = "交易码")
	private String transCode;
	/**
	 * 交易名称
	 */
	@ApiModelProperty(value = "交易名称")
	private String transName;
	/**
	 * 交易日期  yyyyMMdd
	 */
	@ApiModelProperty(value = "交易日期  yyyyMMdd")
	private String transDate;
	/**
	 * 交易时间   HHmmss
	 */
	@ApiModelProperty(value = "交易时间   HHmmss")
	private String transTime;
	/**
	 * 交易状态  FAIL  交易失败   SUCCESS  交易成功
	 */
	@ApiModelProperty(value = "交易状态  FAIL  交易失败   SUCCESS  交易成功")
	private String transStatus;
	/**
	 * 商家名称，分账主账户名称
	 */
	@ApiModelProperty(value = "商家名称，分账主账户名称")
	private String merchantName;
	/**
	 * 分账账户名称
	 */
	@ApiModelProperty(value = "分账账户名称")
	private String subAccountName;
	/**
	 * 分账主账户号，商家账户号
	 */
	@ApiModelProperty(value = "分账主账户号，商家账户号")
	private String merchantMerNo;
	/**
	 * 分账加密卡号
	 */
	@ApiModelProperty(value = "分账加密卡号")
	private String subCardNo;
	/**
	 * 分账金额
	 */
	@ApiModelProperty(value = "分账金额")
	private Double payAmt;
	/**
	 * 分账附言
	 */
	@ApiModelProperty(value = "分账附言")
	private String payPs;
	/**
	 * 银商返回码
	 */
	@ApiModelProperty(value = "银商返回码")
	private String respCode;
	/**
	 * 银商返回描述信息
	 */
	@ApiModelProperty(value = "银商返回描述信息")
	private String respMsg;
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
