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

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地铁站点活动设置
 *
 * @author pigx code generator
 * @date 2020-09-02 10:10:33
 */
@Data
@TableName("kboparkx_subway_site_activity")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "地铁站点活动设置")
public class SubwaySiteActivity extends Model<SubwaySiteActivity> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 站点编号
	 */
	@ApiModelProperty(value = "站点编号")
	@NotNull(message = "请设置站点编号")
	@Size(min = 1, message = "请设置站点编号")
	private String siteCode;
	/**
	 * 支持展示的商家数量
	 */
	@ApiModelProperty(value = "支持展示的商家数量")
	private Integer merchantNumber;
	/**
	 * 商家距离
	 */
	@ApiModelProperty(value = "商家距离")
	private Double merchantDistance;
	/**
	 * 支持展示的优惠券数量
	 */
	@ApiModelProperty(value = "支持展示的优惠券数量")
	private Integer couponNumber;
	/**
	 * 优惠券距离
	 */
	@ApiModelProperty(value = "优惠券距离")
	private Double couponDistance;
	/**
	 * 支持展示的红包数量
	 */
	@ApiModelProperty(value = "支持展示的红包数量")
	private Integer redpackNumber;
	/**
	 * 红包距离
	 */
	@ApiModelProperty(value = "红包距离")
	private Double redpackDistance;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value="创建时间")
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
	/**
	 * 0 未锁定   1  已锁定
	 */
	@ApiModelProperty(value = "0 未锁定   1  已锁定")
	private String lockFlag;
	/**
	 * 所属租户
	 */
	@ApiModelProperty(value = "所属租户", hidden = true)
	private Integer tenantId;
}
