package com.kbopark.operation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantProfitParam implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer merchantId;

	private Double profitPercent;

	@ApiModelProperty("渠道商分润比例")
	private Double operatorProfitPercent;

	@ApiModelProperty("商家福利")
	private Double welfarePercent;
}
