package com.kbopark.operation.dto;

import com.kbopark.operation.entity.Merchant;
import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantReviewParam implements Serializable {
	private static final long serialVersionUID = 1L;

	// 修改信息
	private Merchant amendData;

	private String remark;

	private Integer merchantId;

	private Boolean pass;
}
