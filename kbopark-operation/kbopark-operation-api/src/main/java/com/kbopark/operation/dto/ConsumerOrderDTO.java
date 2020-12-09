package com.kbopark.operation.dto;

import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.CouponReceive;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/1 11:12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumerOrderDTO extends ConsumerOrder {
	private static final long serialVersionUID = 1L;

	/**页面搜索条件,其他位置可忽略**/
	private String searchName;
	private List<String> searchReceiveTime;


	/**
	 * 商家ID
	 */
	@ApiModelProperty(value = "商家ID")
	private Integer merchantId;

	/**
	 * 优惠券编号
	 */
	@ApiModelProperty(value = "优惠券编号")
	private String couponSerialNumber;
}
