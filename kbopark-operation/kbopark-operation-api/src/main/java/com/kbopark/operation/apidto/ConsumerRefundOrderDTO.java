package com.kbopark.operation.apidto;

import com.kbopark.operation.entity.ConsumerRefundOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/28 15:02
 **/
@Data
public class ConsumerRefundOrderDTO extends ConsumerRefundOrder {


	@NotNull(message = "请选择订单号")
	private String orderNumber;

	//0 1
	@NotNull(message = "请选择退款方式")
	private Integer wayType;

	@NotNull(message = "请选择退款原因")
	private Integer reasonType;

}
