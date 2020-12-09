package com.kbopark.operation.dto;

import com.kbopark.operation.entity.ConsumerOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/1 11:12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateOrderDTO extends ConsumerOrder {
	private static final long serialVersionUID = 1L;


	/**
	 * 商家ID
	 */
	@ApiModelProperty(value = "商家ID")
	@NotNull(message = "请设置商家ID")
	private Integer merchantId;

	/**
	 * 会员手机号
	 */
	@ApiModelProperty(value = "会员手机号")
	@NotNull(message = "请设置用户手机号")
	@Size(min = 1,message = "请设置用户手机号")
	private String memberPhone;

	/**
	 * 优惠券编号
	 */
	@ApiModelProperty(value = "优惠券编号")
	private String couponSerialNumber;

	/**
	 * 应付金额
	 */
	@ApiModelProperty(value = "应付金额")
	@NotNull(message = "请设置支付金额")
	private Double payable;
}
