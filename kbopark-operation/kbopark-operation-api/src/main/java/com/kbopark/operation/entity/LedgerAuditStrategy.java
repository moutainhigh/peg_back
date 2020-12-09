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
 * 财务审核策略设置
 *
 * @author pigx code generator
 * @date 2020-10-15 16:15:31
 */
@Data
@TableName("kboparkx_ledger_audit_strategy")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "财务审核策略设置")
public class LedgerAuditStrategy extends Model<LedgerAuditStrategy> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 机构ID
	 */
	@ApiModelProperty(value = "机构ID")
	private Integer deptId;
	/**
	 * 业务审核策略类型  auto  自动   arti  人工
	 */
	@ApiModelProperty(value = "业务审核策略类型  auto  自动   arti  人工")
	private String businessType;
	/**
	 * 财务审核策略类型  auto  自动   arti  人工
	 */
	@ApiModelProperty(value = "财务审核策略类型  auto  自动   arti  人工")
	private String financialType;
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
