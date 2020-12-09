package com.kbopark.operation.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CouponReviewParam implements Serializable {
	private static final long serialVersionUID = 1L;

	private String remark;

	private Integer couponId;

	private Boolean pass;
}
