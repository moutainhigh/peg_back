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

import cn.hutool.core.util.ObjectUtil;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.kbopark.operation.dto.CouponReviewParam;
import com.kbopark.operation.enums.CouponAuditStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 优惠券审核记录
 *
 * @author pigx code generator
 * @date 2020-08-30 14:50:54
 */
@Data
@TableName("kboparkx_coupon_review_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "优惠券审核记录")
public class CouponReviewLog extends Model<CouponReviewLog> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 券ID
	 */
	@ApiModelProperty(value = "券ID")
	private Integer couponId;

	/**
	 * 申请人ID
	 */
	@ApiModelProperty(value = "申请人ID")
	private Integer applyBy;
	/**
	 * 申请人姓名
	 */
	@ApiModelProperty(value = "申请人姓名")
	private String applyName;

	/**
	 * 申请提交时间
	 */
	@ApiModelProperty(value = "申请提交时间")
	private LocalDateTime applyTime;

	/**
	 * 运营平台审核人id
	 */
	@ApiModelProperty(value = "运营平台审核人id")
	private Integer operatorReviewerId;

	/**
	 * 运营平台审核人姓名
	 */
	@ApiModelProperty(value = "运营平台审核人姓名")
	private String operatorReviewerName;
	/**
	 * 运营平台审核备注
	 */
	@ApiModelProperty(value = "运营平台审核备注")
	private String operatorRemark;
	/**
	 * 运营平台是否审核通过
	 */
	@ApiModelProperty(value = "运营平台是否审核通过")
	private Boolean operatorPass;
	/**
	 * 运营平台审核时间
	 */
	@ApiModelProperty(value = "运营平台审核时间")
	private LocalDateTime operatorReviewTime;

	/**
	 * 运营平台审核人id
	 */
	@ApiModelProperty(value = "平台审核人id")
	private Integer platformReviewerId;

	/**
	 * 运营平台审核人姓名
	 */
	@ApiModelProperty(value = "平台审核人姓名")
	private String platformReviewerName;
	/**
	 * 运营平台审核备注
	 */
	@ApiModelProperty(value = "平台审核备注")
	private String platformRemark;
	/**
	 * 运营平台是否审核通过
	 */
	@ApiModelProperty(value = "平台是否审核通过")
	private Boolean platformPass;
	/**
	 * 运营平台审核时间
	 */
	@ApiModelProperty(value = "平台审核时间")
	private LocalDateTime platformReviewTime;

	@ApiModelProperty("是否已经完成")
	private Boolean finish;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private String delFlag;

	/**
	 * @param couponId
	 * @param applyName
	 * @param applyBy
	 * @return
	 * @author laomst
	 */
	public static CouponReviewLog newLogOfCoupon(Integer couponId, String applyName, Integer applyBy) {
		CouponReviewLog reviewLog = new CouponReviewLog();
		reviewLog.setCouponId(couponId);
		reviewLog.setApplyBy(applyBy);
		reviewLog.setApplyName(applyName);
		reviewLog.setApplyTime(LocalDateTime.now());
		return reviewLog;
	}


	/**
	 * 审核
	 * @param param
	 * @param userType
	 * @param userId
	 * @param userRealname
	 * @return
	 */
	public CouponAuditStatusEnum review(CouponReviewParam param, Integer userType, Integer userId, String userRealname) {
		if (ObjectUtil.equal(userType, UserTypeEnum.Operation.code)) {
			setFinish(!param.getPass());
			setOperatorReviewerId(userId);
			setOperatorReviewerName(userRealname);
			setOperatorReviewTime(LocalDateTime.now());
			setOperatorPass(param.getPass());
			setOperatorRemark(param.getRemark());
			return param.getPass() ? CouponAuditStatusEnum.WAIT_PLATFORM : CouponAuditStatusEnum.FAIL;
		}
		// 如果是运营平台审核
		else if (ObjectUtil.equal(userType, UserTypeEnum.Administrator.code)) {
			// 运营商审核完成之后整个流程就完成了
			setFinish(true);
			setPlatformReviewerId(userId);
			setPlatformReviewerName(userRealname);
			setPlatformReviewTime(LocalDateTime.now());
			setPlatformPass(param.getPass());
			setPlatformRemark(param.getRemark());
			return param.getPass() ? CouponAuditStatusEnum.SUCCESS : CouponAuditStatusEnum.FAIL;
		} else {
			throw new UnsupportedOperationException("您没有权限进行审核");
		}
	}
}
