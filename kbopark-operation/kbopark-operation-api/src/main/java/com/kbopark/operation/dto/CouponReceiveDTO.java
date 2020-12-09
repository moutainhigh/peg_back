package com.kbopark.operation.dto;

import com.kbopark.operation.entity.CouponReceive;
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
public class CouponReceiveDTO extends CouponReceive {
	private static final long serialVersionUID = 1L;

	/**页面搜索条件,其他位置可忽略**/
	private String searchName;
	private List<String> searchReceiveTime;


	/**
	 * 商家ID
	 */
	@ApiModelProperty(value = "商家ID")
	@NotNull(message = "请设置商家ID")
	private Integer merchantId;
	/**
	 * 手机号
	 */
	@NotNull(message = "请设置手机号")
	@Size(min = 1,message = "请设置手机号")
	private String phone;
	/**
	 * 用户昵称
	 */
	private String nickName;
	/**
	 * 头像地址
	 */
	private String image;
	/**
	 * 券编号
	 */
	@NotNull(message = "请设置券编号")
	@Size(min = 1,message = "请设置券编号")
	private String couponSerialNumber;
	/**
	 * 是否是一键领取全部券
	 */
	private Boolean receiveAll;
	/**订单编号，领取优惠券或者乘车券时使用**/
	private String orderNumber;
}
