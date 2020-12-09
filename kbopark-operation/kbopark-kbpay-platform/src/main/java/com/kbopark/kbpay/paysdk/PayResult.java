package com.kbopark.kbpay.paysdk;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PayResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("支付通道类型")
	private String payChannelValue;

	@ApiModelProperty("支付链接")
	private String payUrl;

	public PayResult(String payChannelValue, String payUrl) {
		this.payChannelValue = payChannelValue;
		this.payUrl = payUrl;
	}

}
