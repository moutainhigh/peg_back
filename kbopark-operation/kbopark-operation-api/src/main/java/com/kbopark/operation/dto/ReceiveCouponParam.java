package com.kbopark.operation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ReceiveCouponParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("商家id")
	@NotNull(message="请设置商家ID")
	private Integer MerchantId;

	@ApiModelProperty("卡券编号")
	@NotNull(message = "请设置券编号")
	private String couponSerialNumber;
}
