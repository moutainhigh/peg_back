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
 * 用户反馈信息
 *
 * @author pigx code generator
 * @date 2020-10-12 08:51:25
 */
@Data
@TableName("kboparkx_feedback_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户反馈信息")
public class FeedbackInfo extends Model<FeedbackInfo> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 反馈人姓名
	 */
	@ApiModelProperty(value = "反馈人姓名")
	private String name;
	/**
	 * 反馈类型
	 */
	@ApiModelProperty(value = "反馈类型")
	private String type;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String phone;
	/**
	 * 反馈信息详情
	 */
	@ApiModelProperty(value = "反馈信息详情")
	private String detailInfo;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
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
