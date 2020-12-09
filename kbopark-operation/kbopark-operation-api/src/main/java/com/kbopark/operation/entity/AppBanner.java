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
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户端首页图片关联
 *
 * @author laomst
 * @date 2020-09-30 11:38:14
 */
@Data
@TableName("kboparkx_app_banner")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户端首页图片关联")
public class AppBanner extends Model<AppBanner> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String name;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String imageUrl;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String linkUrl;
	/**
	 * 类型 1 banner图 2 权益图
	 */
	@ApiModelProperty(value = "类型 1 banner图 2 权益图")
	private Integer type;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String remark;
	/**
	 * 0
	 */
	@ApiModelProperty(value = "0")
	private Integer delFlag;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime createTime;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private LocalDateTime updateTime;

	@ApiModelProperty("是否展示")
	private Boolean isShow;

	@ApiModelProperty("排序")
	private Integer sort;
}
