package com.kbopark.kbpay.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PayParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("订单流水号")
	private String tradeBizNumber;

	@ApiModelProperty("支付返回页面url")
	private String returnUrl;

}
