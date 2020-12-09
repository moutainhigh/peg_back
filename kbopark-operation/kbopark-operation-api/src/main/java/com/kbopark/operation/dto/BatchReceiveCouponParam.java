package com.kbopark.operation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class BatchReceiveCouponParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("商家id")
	@NotNull(message="请设置商家ID")
	private Integer MerchantId;

	@ApiModelProperty("卡券编号列表")
	@NotNull(message = "请设置券编号")
	private List<String> couponSerialNumberList;
}
