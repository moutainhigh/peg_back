package com.kbopark.operation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class CouponSearchParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("商家id")
	private Integer merchantIdKey;

	private Integer memberIdKey;

	@ApiModelProperty("卡券/红包id")
	private Integer couponIdKey;

	@ApiModelProperty(value = "类型", notes = "coupon是优惠券")
	private String typeKey;

	@ApiModelProperty(value = "类型", notes = "对于红包而言， 1是随机红包， 2是普通红包; 对于优惠券而言， 1是满减券")
	private Integer couponTypeKey;

	@ApiModelProperty(value = "状态", allowableValues = "all(所有)，can-use(可用),disabled(所有不可用的),overdue(过期),locked(禁用的、失效的),used(已使用)")
	private String statusKey;

	@ApiModelProperty("使用时间区间开始")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd"
	)
	private Date startUsedTimeKey;
	@ApiModelProperty("使用时间区间结束")
	@DateTimeFormat(
			pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd"
	)
	private Date endUsedTimeKey;

}
