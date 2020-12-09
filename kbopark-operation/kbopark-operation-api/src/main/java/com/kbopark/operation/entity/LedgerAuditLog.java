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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分账订单审核日志
 *
 * @author pigx code generator
 * @date 2020-10-16 13:56:49
 */
@Data
@TableName("kboparkx_ledger_audit_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分账订单审核日志")
public class LedgerAuditLog extends Model<LedgerAuditLog> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String ledgerBatchNo;
	/**
	 * 审核状态
	 */
	@ApiModelProperty(value = "")
	private String auditStatus;
	/**
	 * 审核状态描述
	 */
	@ApiModelProperty(value = "审核状态描述")
	private String auditMsg;
	/**
	 * 审核备注
	 */
	@ApiModelProperty(value = "审核备注")
	private String auditRemark;
	/**
	 * 审核时间
	 */
	@ApiModelProperty(value = "审核时间")
	private LocalDateTime auditTime;
	/**
	 * 审核人
	 */
	@ApiModelProperty(value = "审核人")
	private String auditBy;
	/**
	 * 审核人所属机构ID
	 */
	@ApiModelProperty(value = "审核人所属机构ID")
	private Integer deptId;
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
	 * 更新操作人姓名
	 */
	@ApiModelProperty(value = "更新操作人姓名")
	private String updateBy;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	@TableLogic
	private String delFlag;
}
