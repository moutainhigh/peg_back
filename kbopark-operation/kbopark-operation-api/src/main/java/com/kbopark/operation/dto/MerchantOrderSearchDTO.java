package com.kbopark.operation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/20 11:47
 **/
@Data
public class MerchantOrderSearchDTO {

	private Integer merchantId;

	private String notifyStatus;
	@ApiModelProperty("支付时间区间开始")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd"
	)
	private Date startPayedTimeKey;
	@ApiModelProperty("支付时间区间结束")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd"
	)
	private Date endPayedTimeKey;

	@ApiModelProperty("订单号")
	private String orderNumber;

	@ApiModelProperty("推广码")
	private String promoteCode;
}
