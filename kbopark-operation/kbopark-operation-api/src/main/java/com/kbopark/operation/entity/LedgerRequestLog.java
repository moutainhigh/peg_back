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
 * 分账请求日志
 *
 * @author pigx code generator
 * @date 2020-09-24 17:30:47
 */
@Data
@TableName("kboparkx_ledger_request_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分账请求日志")
public class LedgerRequestLog extends Model<LedgerRequestLog> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
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
	 * 请求系统流水号
	 */
	@ApiModelProperty(value = "请求系统流水号")
	private String srcReqId;
	/**
	 * 请求参数
	 */
	@ApiModelProperty(value = "请求参数")
	private String requestParam;
	/**
	 * 响应数据
	 */
	@ApiModelProperty(value = "响应数据")
	private String responseData;
	/**
	 * 验签结果
	 */
	@ApiModelProperty(value = "验签结果")
	private String validResult;
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
