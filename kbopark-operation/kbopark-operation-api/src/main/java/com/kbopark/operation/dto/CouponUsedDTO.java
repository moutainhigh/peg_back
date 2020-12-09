package com.kbopark.operation.dto;

import com.kbopark.operation.entity.CouponUsedLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/1 11:12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CouponUsedDTO extends CouponUsedLog {
	private static final long serialVersionUID = 1L;


	/**
	 * 商家ID(必传)
	 */
	@ApiModelProperty(value = "商家ID")
	@NotNull(message = "请设置商家ID")
	private Integer merchantId;

	/**
	 * 券类型，红包或优惠券(必传)
	 */
	@ApiModelProperty(value = "券类型，红包或优惠券")
	@NotNull(message = "请设置类型")
	private String couponType;

}
