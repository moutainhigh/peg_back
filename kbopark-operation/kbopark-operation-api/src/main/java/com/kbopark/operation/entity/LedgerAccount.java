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

import java.time.LocalDateTime;

/**
 * 分账账户管理
 *
 * @author pigx code generator
 * @date 2020-09-29 10:24:33
 */
@Data
@TableName("kboparkx_ledger_account")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分账账户管理")
public class LedgerAccount extends Model<LedgerAccount> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 关联ID,商家、运营商、平台
	 */
	@ApiModelProperty(value = "关联ID,商家、运营商、平台")
	private Integer relationId;
	/**
	 * 名称（业务系统）
	 */
	@ApiModelProperty(value = "名称（业务系统）")
	private String belongName;
	/**
	 * 账户类别（业务系统）商家、运营商、平台
	 */
	@ApiModelProperty(value = "账户类别（业务系统）商家、运营商、平台")
	private String belongType;
	/**
	 * 集团号
	 */
	@ApiModelProperty(value = "集团号")
	private String groupId;
	/**
	 * 企业用户号（银商分配的分账专用商户号）
	 */
	@ApiModelProperty(value = "企业用户号（银商分配的分账专用商户号）")
	private String merNo;
	/**
	 * 银商账户名称
	 */
	@ApiModelProperty(value = "银商账户名称")
	private String merName;
	/**
	 * 分账专用卡号（未Hash）
	 */
	@ApiModelProperty(value = "分账专用卡号（未Hash）")
	private String cardNo;
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
	 * 更新人姓名
	 */
	@ApiModelProperty(value = "更新人姓名")
	private String updateBy;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	@TableLogic
	private String delFlag;
}
