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

import com.baomidou.mybatisplus.annotation.TableField;
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

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 优惠券表
 *
 * @author pigx code generator
 * @date 2020-08-28 16:15:36
 */
@Data
@TableName("kboparkx_coupon_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "优惠券表")
public class CouponInfo extends Model<CouponInfo> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 券编号
	 */
	@ApiModelProperty(value = "券编号")
	private String couponSerialNumber;

	/**
	 * 所属商家ID
	 */
	@ApiModelProperty(value = "所属商家ID")
	private Integer merchantId;
	/**
	 * 商户简称
	 */
	@ApiModelProperty(value = "商户简称")
	private String merchantName;
	/**
	 * 渠道商ID
	 */
	@ApiModelProperty(value = "渠道商ID")
	private Integer channelId;
	/**
	 * 运营商ID
	 */
	@ApiModelProperty(value = "运营商ID")
	private Integer operationId;
	/**
	 * 优惠券名称
	 */
	@ApiModelProperty(value = "优惠券名称")
	private String name;
	/**
	 * 分类类型，红包、优惠券
	 */
	@ApiModelProperty(value = "分类类型")
	private String type;
	/**
	 * 优惠券类型  1  满减券
	 * 红包类型	1 随机  2 普通
	 */
	@ApiModelProperty(value = "优惠券类型  1  满减券")
	private Integer couponType;
	/**
	 * 优惠券设置信息
	 */
	@ApiModelProperty(value = "优惠券设置信息")
	@TableField(exist = false)
	private String couponSetInfo;
	/**
	 * 满金额
	 */
	@ApiModelProperty(value = "满金额")
	private Double fullMoney;
	/**
	 * 减金额
	 */
	@ApiModelProperty(value = "减金额")
	private Double subMoney;
	/**
	 * 红包金额
	 */
	@ApiModelProperty(value = "红包金额")
	private Double costMoney;
	/**
	 * 红包累计产生金额
	 */
	@ApiModelProperty(value = "红包金额")
	private Double addUpMoney;
	/**
	 * 阈值最小金额
	 */
	@ApiModelProperty(value = "阈值最小金额")
	private Double minMoney;
	/**
	 * 阈值最大金额
	 */
	@ApiModelProperty(value = "阈值最大金额")
	private Double maxMoney;
	/**
	 * 参与门店，关联门店ID
	 */
	@ApiModelProperty(value = "参与门店，关联门店ID")
	private String branchMerchantIds;
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
	private Date startTime;
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
	private Date endTime;
	/**
	 * 总数量
	 */
	@ApiModelProperty(value = "总数量")
	private Integer totalNumber;
	/**
	 * 每人限领取数量
	 */
	@ApiModelProperty(value = "每人限领取数量")
	private Integer limitNumber;
	/**
	 * 已领取数量
	 */
	@ApiModelProperty(value = "已领取数量")
	private Integer usedNumber;
	/**
	 * 剩余数量
	 */
	@ApiModelProperty(value = "剩余数量")
	private Integer remainNumber;
	/**
	 * 是否同意授权运营平台进行站点分配
	 */
	@ApiModelProperty(value = "是否同意授权运营平台进行站点分配")
	private Integer hadAuth;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String ruleInfo;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 创建人ID
	 */
	@ApiModelProperty(value = "创建人ID")
	private Integer createUserId;
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
	 * 启用禁用状态，平台或运营商使用
	 */
	@ApiModelProperty(value = "启用禁用状态，平台或运营商使用")
	private String lockFlag;
	/**
	 * 审核状态
	 */
	@ApiModelProperty(value = "审核状态")
	private Integer auditStatus;

	/**
	 * 审核备注信息
	 */
	@ApiModelProperty(value = "审核备注信息")
	private String auditMarkInfo;

	/**
	 * 上架下架状态
	 */
	@ApiModelProperty(value = "上架下架状态")
	private Integer takeStatus;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date auditTime;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private Date onlineTime;

	@ApiModelProperty("权重")
	private Integer weight;

	public String getCouponSetInfo() {
		if ("coupon".equals(type)) {
			return  "满"+ this.fullMoney + "元"+",减"+ this.subMoney + "元";
		} else {
			return "";
		}
	}

}
