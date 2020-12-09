package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 14:03
 **/
@Data
public class OrderListDTO extends RequestPublic {

	/**
	 * 关键字:订单号、游客姓名、手机号码、
	 * 订单备注、产品名称
	 */
	private String keyWrod;

	/**
	 * 用户：分销渠道下单时传递的用户
	 * 身份编码
	 */
	private String userId;

	/**
	 * 下单开始日期，格式：2011-01-01
	 */
	private Date startDate;

	/**
	 * 下单结束日期，格式：2011-01-01
	 */
	private Date endDate;

	/**
	 * 游玩日期，格式：2011-01-01
	 */
	private Date travelDate;

	/**
	 * 支付方式，0 表示线下支付
	 * 1 表示在线支付
	 */
	private Integer isPay;

	/***
	 * 订单状态:
	 * 0 新订单
	 * 1 已确认
	 * 2 已成功
	 * 3 已取消
	 * 4 已完成。
	 */
	private String orderState;

	/**
	 * 每页条数,默认 50，最大 100
	 */
	private String pageNum;

	/**
	 * 页码,默认 1
	 */
	private String pageNo;

}
