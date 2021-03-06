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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author pigx code generator
 * @date 2020-08-27 11:30:36
 */
@Data
@TableName("kboparkx_subway_line")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class SubwayLine extends Model<SubwayLine> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 地铁线路名称
     */
    @NotNull(message = "名称不能为空")
	@Size(min = 1,message = "名称不能为空")
    @ApiModelProperty(value="地铁线路名称")
    private String name;
    /**
     * 地铁线路编码
     */
    @ApiModelProperty(value="地铁线路编码")
    private String code;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sortNumber;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;
    /**
     * 
     */
    @ApiModelProperty(value="")
	@TableLogic
    private String delFlag;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String lockFlag;
    /**
     * 所属租户
     */
    @ApiModelProperty(value="所属租户",hidden=true)
    private Integer tenantId;
    }
