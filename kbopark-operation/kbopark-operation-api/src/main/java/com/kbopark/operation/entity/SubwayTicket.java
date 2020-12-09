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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 乘车券管理
 *
 * @author pigx code generator
 * @date 2020-09-08 14:52:55
 */
@Data
@TableName("kboparkx_subway_ticket")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "乘车券管理")
public class SubwayTicket extends Model<SubwayTicket> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 乘车券名称
	 */
	@ApiModelProperty(value = "乘车券名称")
	private String name;
	/**
	 * 地铁券标识
	 */
	@ApiModelProperty(value = "地铁券标识")
	private String subwayCode;
	/**
	 * 系统编号，本平台编号
	 */
	@ApiModelProperty(value = "系统编号，本平台编号")
	private String serialNumber;
	/**
	 * 乘车券价值
	 */
	@ApiModelProperty(value = "乘车券价值")
	private Double value;
	/**
	 * 有效期类型 1 按领取时间 2 按有效期
	 */
	@ApiModelProperty(value = "有效期类型 1 按领取时间 2 按有效期")
	private Integer effectType;
	/**
	 * 有效期天数
	 */
	@ApiModelProperty(value = "有效期天数")
	private Integer effectDay;
	/**
	 * 有效期开始时间
	 */
	@ApiModelProperty(value = "有效期开始时间")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd"
	)
	private Date effectStartTime;
	/**
	 * 有效期结束时间
	 */
	@ApiModelProperty(value = "有效期结束时间")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd"
	)
	private Date effectEndTime;
	/**
	 * 使用规则
	 */
	@ApiModelProperty(value = "使用规则")
	private String ruleInfo;
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

	/**
	 * 使用状态 0 禁用 1 启用
	 */
	@ApiModelProperty(value = "使用状态")
	private Integer usedStatus;


	/**
	 * 总发放数量
	 */
	@ApiModelProperty(value = "总发放数量")
	private Integer totalNumber;


	/**
	 * 已领取数量
	 */
	@ApiModelProperty(value = "已领取数量")
	private Integer usedNumber;

}
