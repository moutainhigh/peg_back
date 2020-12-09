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
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.kbopark.operation.dto.MerchantReviewParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商家审核记录
 *
 * @author laomst
 * @date 2020-08-28 23:33:55
 */
@Data
@TableName("kboparkx_merchant_review_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商家审核记录")
public class MerchantReviewLog extends Model<MerchantReviewLog> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 商家id
     */
    @ApiModelProperty(value="商家id")
    private Integer merchantId;
    /**
     * 提交人姓名
     */
    @ApiModelProperty(value="提交人姓名")
    private String submitUserName;
    /**
     * 提交人id
     */
    @ApiModelProperty(value="提交人id")
    private Integer submitUserId;

	@ApiModelProperty(value="提交时间")
	private LocalDateTime submitTime;
    /**
     * 渠道商审核人id
     */
    @ApiModelProperty(value="渠道商审核人id")
    private Integer distributorReviewerId;
    /**
     * 渠道商审核人姓名
     */
    @ApiModelProperty(value="渠道商审核人姓名")
    private String distributorReviewerName;
    /**
     * 渠道商是否审核通过
     */
    @ApiModelProperty(value="渠道商是否审核通过")
    private Boolean distributorPass;
    /**
     * 渠道商审核备注
     */
    @ApiModelProperty(value="渠道商审核备注")
    private String distributorRemark;
    /**
     * 渠道商审核时间
     */
    @ApiModelProperty(value="渠道商审核时间")
    private LocalDateTime distributorReviewTime;
    /**
     * 运营平台审核人id
     */
    @ApiModelProperty(value="运营平台审核人id")
    private Integer operatorReviewerId;
    /**
     * 运营平台审核人姓名
     */
    @ApiModelProperty(value="运营平台审核人姓名")
    private String operatorReviewerName;
    /**
     * 运营平台审核备注
     */
    @ApiModelProperty(value="运营平台审核备注")
    private String operatorRemark;
    /**
     * 运营平台是否审核通过
     */
    @ApiModelProperty(value="运营平台是否审核通过")
    private Boolean operatorPass;
    /**
     * 运营平台审核时间
     */
    @ApiModelProperty(value="运营平台审核时间")
    private LocalDateTime operatorReviewTime;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private LocalDateTime createTime;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private LocalDateTime updateTime;
    /**
     * 
     */
    @ApiModelProperty(value="")
	@TableLogic
    private Integer delFlag;

    @ApiModelProperty("是否已经完成")
    private Boolean finish;

	public MerchantReviewLog() {}

	public MerchantReviewLog(Integer merchantId, Integer submitUserId, String submitUserName) {
		this.submitTime = LocalDateTime.now();
		this.submitUserId = submitUserId;
		this.submitUserName = submitUserName;
		this.merchantId = merchantId;
	}

	public void changeReviewStatus(MerchantReviewParam param, Integer userType, Integer userId, String userRealname) {
		if (ObjectUtil.equal(userType, UserTypeEnum.Channel.code)) {
			setFinish(!param.getPass());
			setDistributorReviewerId(userId);
			setDistributorReviewerName(userRealname);
			setDistributorReviewTime(LocalDateTime.now());
			setDistributorPass(param.getPass());
			setDistributorRemark(param.getRemark());
		}
		// 如果是运营平台审核
		else if (ObjectUtil.equal(userType, UserTypeEnum.Operation.code)) {
			// 运营商审核完成之后整个流程就完成了
			setFinish(true);
			setOperatorReviewerId(userId);
			setOperatorReviewerName(userRealname);
			setOperatorReviewTime(LocalDateTime.now());
			setOperatorPass(param.getPass());
			setOperatorRemark(param.getRemark());
		}
	}


}
