package com.kbopark.operation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantPayOrderDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("商家简称")
	private String merchantSimpleName;

	@ApiModelProperty("商家名称")
	private String merchantName;

	@ApiModelProperty("商家logo")
	private String merchantLogo;

	@ApiModelProperty("会员头像")
	private String memberAvatar;

	@ApiModelProperty("会员昵称")
	private String memberNickname;

	private Integer id;

	@ApiModelProperty(value = "订单号")
	private String orderNumber;

	/**
	 * 应付金额
	 */
	@ApiModelProperty(value = "应付金额")
	private Double payable;
	/**
	 * 折扣金额
	 */
	@ApiModelProperty(value = "折扣金额")
	private Double discount;
	/**
	 * 实付金额
	 */
	@ApiModelProperty(value = "实付金额")
	private Double money;

	/**
	 * 已支付金额
	 */
	@ApiModelProperty(value = "已支付金额")
	private Double payed;

	/**
	 * 优惠券名称
	 */
	@ApiModelProperty(value = "优惠券减免")
	private String couponName;

	@ApiModelProperty("发起时间")
	private String createTime;

	/**
	 * 支付状态  WAIT 待支付   SUCCESS  支付成功  FAIL 支付失败
	 */
	@ApiModelProperty(value = "支付状态  WAIT 待支付   SUCCESS  支付成功  FAIL 支付失败")
	private String notifyStatus;
	/**
	 * 异步通知时间
	 */
	@ApiModelProperty(value = "支付时间")
	private String notifyTime;


	public String getNotifyTime() {
		if (!"SUCCESS".equals(notifyStatus)) {
			return "";
		} else {
			return notifyTime;
		}
	}
}
