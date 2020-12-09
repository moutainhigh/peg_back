package com.kbopark.operation.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 状态大于50的属于可用状态
 */
@Getter
@AllArgsConstructor
public enum MerchantReviewStatusEnum {

	UN_START_REVIEW(
			"UN_START_REVIEW",
			-100,
			"未开始审核",
			false,
			true
	),
	ADD_UN_SUBMIT(
			"UN_SUBMIT",
			1,
			"新建，未提交审核",
			false,
			true
	),
	ADD_DISTRIBUTOR_UN_CHECKED(
			"UN_CHECKED",
			2,
			"新建，等待渠道商审核",
			false,
			true
	),
	ADD_OPERATOR_UN_CHECKED(
			"UN_CHECKED",
			3,
			"新建，等待运营平台审核",
			false,
			true
	),
	ADD_CHECK_FAIL(
			"CHECK_FAIL",
			4,
			"新建，已驳回",
			true,
			true
	),
	EDIT_UN_SUBMIT(
			"EDIT_UN_SUBMIT",
			51,
			"修改，未提交审核",
			false,
			false
	),
	EDIT_DISTRIBUTOR_UN_CHECKED(
			"EDIT_UN_CHECKED",
			52,
			"修改，等待渠道商审核",
			false,
			false
	),
	EDIT_OPERATOR_UN_CHECKED(
			"EDIT_UN_CHECKED",
			53,
			"修改，等待运营平台审核",
			false,
			false
	),
	EDIT_CHECK_FAIL(
			"EDIT_CHECK_FAIL",
			54,
			"修改，已驳回",
			true,
			false
	),
	CHECK_SUCCESS(
			"CHECK_SUCCESS",
			100,
			"审核通过",
			false,
			false
	),
	;

	public final String value;
	public final Integer code;
	public final String description;
	public final Boolean isFail; // 是否审核失败
	public final Boolean isAdd; // 是否是在添加，如果不是添加那么就是编辑了

	// 通过code获取一个状态,
	public static MerchantReviewStatusEnum codeOf(Integer code) {
		return Arrays.stream(MerchantReviewStatusEnum.values()).filter(item -> item.code.equals(code)).findFirst().orElse(UN_START_REVIEW);
	}
}
