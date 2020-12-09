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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.common.core.util.R;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kbopark.operation.dto.MerchantReviewParam;
import com.kbopark.operation.enums.MerchantReviewStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kbopark.operation.enums.MerchantReviewStatusEnum.*;

/**
 * 商家基本信息表
 *
 * @author laomst
 * @date 2020-08-25 17:59:54
 */
@Data
@TableName("kboparkx_merchant")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商家基本信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Merchant extends Model<Merchant> {
	private static final long serialVersionUID = 1L;

	// 审核中的商家状态
	public static final List<Integer> CHECKING_STATUS = CollectionUtil.newArrayList(
			ADD_DISTRIBUTOR_UN_CHECKED.code,
			ADD_OPERATOR_UN_CHECKED.code,
			EDIT_DISTRIBUTOR_UN_CHECKED.code,
			EDIT_OPERATOR_UN_CHECKED.code);

	// 审核失败的商家状态
	public static final List<Integer> CHECK_FAIL_STATUS = CollectionUtil.newArrayList(
			ADD_CHECK_FAIL.code,
			EDIT_CHECK_FAIL.code);

	public static final List<Integer> CHECK_SUCCESS_STATUS =  CollectionUtil.newArrayList(ADD_CHECK_FAIL.CHECK_SUCCESS.code);

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 所属运营商id
	 */
	@ApiModelProperty(value = "所属运营商id")
	private Integer operatorId;
	/**
	 * 所属渠道商
	 */
	@ApiModelProperty(value = "所属渠道商")
	private Integer distributorId;
	/**
	 * 所属推广员id
	 */
	@ApiModelProperty(value = "所属推广员id")
	private Integer promoterId;
	/**
	 * 商家名称
	 */
	@ApiModelProperty(value = "商家名称")
	private String name;

	@ApiModelProperty("商户简称")
	private String simpleName;

	/**
	 * 商家联系人
	 */
	@ApiModelProperty(value = "商家联系人")
	private String linkMan;
	/**
	 * 商家联系电话
	 */
	@ApiModelProperty(value = "商家联系电话")
	private String linkPhone;

	@ApiModelProperty("推广识别码")
	private String promoteCode;
	/**
	 * 省
	 */
	@ApiModelProperty(value = "省")
	private String provinceName;
	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	private String cityName;
	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
	private String areaName;
	/**
	 * 详细地址
	 */
	@ApiModelProperty(value = "详细地址")
	private String address;

	@ApiModelProperty("经度")
	private Double lng;

	@ApiModelProperty("纬度")
	private Double lat;

	/**
	 * 坐标
	 */
	@ApiModelProperty(value = "坐标描述信息")
	private String coordinatesDetail;

	/**
	 * 营业执照注册号
	 */
	@ApiModelProperty(value = "营业执照注册号")
	private String businessLicenseNumber;
	/**
	 * logo
	 */
	@ApiModelProperty(value = "logo")
	private String logo;
	/**
	 * 证书
	 */
	@ApiModelProperty(value = "证书附件")
	private String credential;
	/**
	 * 审核状态
	 */
	@ApiModelProperty(value = "审核状态")
	private Integer reviewStatus;
	/**
	 * 状态， 1启用 2禁用
	 */
	@ApiModelProperty(value = "状态， 1启用 2禁用")
	private Integer status;

	@ApiModelProperty("营业状态")
	private Integer businessStatus;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private LocalDateTime createTime;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
//	@DateTimeFormat(
//			pattern = "yyyy-MM-dd HH:mm"
//	)
//	@JsonFormat(
//			pattern = "yyyy-MM-dd HH:mm"
//	)
	@JsonIgnore
	private LocalDateTime updateTime;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	@TableLogic
	private String delFlag;
	/**
	 *
	 */
	@ApiModelProperty(value = "", hidden = true)
	private Integer tenantId;

	@ApiModelProperty("商家简介")
	private String remark;

	@ApiModelProperty("商品简介")
	private String goodsRemark;

	@ApiModelProperty("审核备注")
	private String reviewRemark;

	@ApiModelProperty("商家分类")
	private Integer categoryKey;

	@ApiModelProperty("是否同意加盟")
	private Boolean isJoin;

	@ApiModelProperty("编辑状态暂存")
	private String todoSnapshoot;

	@ApiModelProperty("进行禁用操作的平台的级别")
	private Integer downDeptLevel;

	@ApiModelProperty("分润比例")
	private Double profitPercent;


	@ApiModelProperty("渠道商分润比例")
	private Double operatorProfitPercent;

	@ApiModelProperty("商家福利比例")
	private Double welfarePercent;
//
//	@ApiModelProperty("可用余额")
//	private BigDecimal usableBalance;
//
//	@ApiModelProperty("冻结余额")
//	private BigDecimal freezeBalance;
//
//	@ApiModelProperty("待入账余额")
//	private BigDecimal toAccountBalance;

	@ApiModelProperty("优惠券、红包列表展示图")
	private String listLogo;

	@ApiModelProperty("详情页轮播图，用于红包和优惠券详情轮播")
	private String banners;

	@ApiModelProperty("是否自动入驻")
	private Boolean isSelfEnter;

	@ApiModelProperty("是否已经查阅过")
	private Boolean isRead;

	@ApiModelProperty("审核时间(驳回时间和审核通过时间都是这个字段)")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private LocalDateTime reviewTime;

	@ApiModelProperty("提交审核的时间")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private LocalDateTime submitReviewTime;

	@ApiModelProperty("审核人员名字")
	private String reviewUsername;

	@ApiModelProperty("审核人员名字")
	private String submitReviewUsername;

	public Boolean getIsChecking() {
		return CHECKING_STATUS.contains(reviewStatus);
	}

	@ApiModelProperty("是否可以修改")
	@TableField(exist = false)
	private Boolean canChange;

	public String getReviewProgress() {
		if (CHECKING_STATUS.contains(reviewStatus)) {
			// 审核中
			return "checking";
		} else if (CHECK_FAIL_STATUS.contains(reviewStatus)) {
			// 审核失败
			return "check-fail";
		} else {
			// 审核成功
			return "check-success";
		}
	}

	/**
	 * @return
	 */
	public R<Boolean> canEnterSelf() {
		List<String> messageList = new ArrayList<>();
		if (null != id || reviewStatus > 50) {
			messageList.add("商家入驻只能是新增，不能对商家信息进行修改");
		}
		if (null == operatorId) {
			messageList.add("缺少运营商信息");
		}
		if (StrUtil.isBlank(name)) {
			messageList.add("缺少商户名称");
		}
		if (StrUtil.isBlank(businessLicenseNumber)) {
			messageList.add("缺少营业执照注册号");
		}
		if (CollectionUtil.isEmpty(messageList)) {
			return R.ok(true);
		} else {
			return R.ok(false, String.join("、", messageList));
		}
	}

	// 商家审核
	public void review(MerchantReviewParam param, Integer userType, String userName) {
		// 审核前置处理
		reviewUsername = userName;
		reviewTime = LocalDateTime.now();
		isRead = false;
		// 真正的处理操作
		if (reviewStatus > 50) {
			editReview(param, userType);
		} else {
			addReview(param, userType);
		}
	}

	public Map<String, String> getReviewStatusInfo() {
		Map<String, String> resMap = new HashMap<>(4);
		if (CHECK_FAIL_STATUS.contains(reviewStatus)) {
			resMap.put("label", "已驳回");
			resMap.put("class", "review-fail");
		} else if (CHECK_SUCCESS_STATUS.contains(reviewStatus)) {
			resMap.put("label", "已通过");
			resMap.put("class", "review-success");
		} else if (CHECKING_STATUS.contains(reviewStatus)) {
			resMap.put("label", "待审核");
			resMap.put("class", "review-going");
		} else {
			resMap.put("label", "未提交");
			resMap.put("class", "review-not-start");
		}
		return resMap;
	}

	/**
	 * 商家审核完成
	 *
	 * @param userType 审核人的类型
	 */
	private void editReview(MerchantReviewParam param, Integer userType) {
		// 如果有修正信息就修正
		if (null != param.getAmendData()) {
			todoSnapshoot = JSONUtil.toJsonStr(param.getAmendData());
		}
		reviewRemark = param.getRemark();
		// 如果是渠道商审核
		if (ObjectUtil.equal(userType, UserTypeEnum.Channel.code)) {
			if (!MerchantReviewStatusEnum.EDIT_DISTRIBUTOR_UN_CHECKED.code.equals(reviewStatus)) {
				throw new UnsupportedOperationException("商家当前的状态不应该是渠道商进行审核，请核实后再进行操作！");
			}
			if (param.getPass()) {
				// 下一步是运营审核
				reviewStatus = MerchantReviewStatusEnum.EDIT_OPERATOR_UN_CHECKED.code;
			} else {
				// 审核失败
				reviewStatus = MerchantReviewStatusEnum.EDIT_CHECK_FAIL.code;
			}
		}
		// 如果是运营商审核
		else if (ObjectUtil.equal(userType, UserTypeEnum.Operation.code)) {
			if (!MerchantReviewStatusEnum.EDIT_OPERATOR_UN_CHECKED.code.equals(reviewStatus)) {
				throw new UnsupportedOperationException("商家当前的状态不应该是运营平台进行审核，请核实后再进行操作！");
			}
			if (param.getPass()) {
				// 如果是运营商审核通过，那么需要进行反序列化处理，把信息拿出来
				Integer id = this.id;
				BeanUtil.copyProperties(JSONUtil.toBean(todoSnapshoot, this.getClass()), this);
				this.id = id;
				reviewStatus = MerchantReviewStatusEnum.CHECK_SUCCESS.code;
			} else {
				reviewStatus = MerchantReviewStatusEnum.EDIT_CHECK_FAIL.code;
			}
		} else {
			throw new UnsupportedOperationException("您当前没有审核商家信息的权限");
		}

	}

	private void addReview(MerchantReviewParam param, Integer userType) {
		// 如果有修正信息，那么就对修正信息进行修正
		if (param.getAmendData() != null) {
			Integer id = this.id;
			BeanUtil.copyProperties(param.getAmendData(), this);
			this.id = id;
		}
		reviewRemark = param.getRemark();
		// 新增审核三个钱相关的直接设置成默认值
		// 审核
		if (ObjectUtil.equal(userType, UserTypeEnum.Channel.code)) {
			if (!MerchantReviewStatusEnum.ADD_DISTRIBUTOR_UN_CHECKED.code.equals(reviewStatus)) {
				throw new UnsupportedOperationException("商家当前的状态不应该是渠道商进行审核，请核实后再进行操作！");
			}
			// 如果审核通过
			if (param.getPass()) {
				// 如果有修改数据，那么就要进行修改
				// 下一步是运营平台进行审核
				setReviewStatus(MerchantReviewStatusEnum.ADD_OPERATOR_UN_CHECKED.code);
			} else {
				// 新建审核失败
				setReviewStatus(MerchantReviewStatusEnum.ADD_CHECK_FAIL.code);
			}
		}
		// 如果是运营平台审核
		else if (ObjectUtil.equal(userType, UserTypeEnum.Operation.code)) {
			if (!MerchantReviewStatusEnum.ADD_OPERATOR_UN_CHECKED.code.equals(reviewStatus)) {
				throw new UnsupportedOperationException("商家当前的状态不应该是运营平台进行审核，请核实后再进行操作！");
			}
			// 如果审核通过
			if (param.getPass()) {
				setReviewStatus(MerchantReviewStatusEnum.CHECK_SUCCESS.code);
			} else {
				// 新建审核失败
				setReviewStatus(MerchantReviewStatusEnum.ADD_CHECK_FAIL.code);
			}
		}
	}

}
