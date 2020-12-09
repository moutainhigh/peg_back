package com.kbopark.operation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class MerchantCouponVO implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("卡券id")
	private Integer couponId;

	@ApiModelProperty("卡券序列号")
	private String couponSerialNumber;

	@ApiModelProperty("卡券名称")
	private String couponName;

	@ApiModelProperty("卡券金额")
	private Double couponCostMoney;

	@ApiModelProperty("总数量")
	private Double couponTotalNumber;

	@ApiModelProperty("已经领取的数量")
	private Double couponReceivedNumber;

	@DateTimeFormat(
			pattern = "MM-dd"
	)
	@JsonFormat(
			pattern = "MM-dd"
	)
	@ApiModelProperty("有效期结束时间")
	private Date couponEndDate;

	@DateTimeFormat(
			pattern = "MM-dd"
	)
	@JsonFormat(
			pattern = "MM-dd"
	)
	@ApiModelProperty("有效期开始时间")
	private Date couponStartDate;

	@ApiModelProperty("提示文字")
	private String couponTip;

	@ApiModelProperty("用户是否可用")
	private Boolean couponCanUse;

	@ApiModelProperty("类型")
	private String type;

}
