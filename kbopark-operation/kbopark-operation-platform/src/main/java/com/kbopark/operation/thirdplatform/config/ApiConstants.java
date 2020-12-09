package com.kbopark.operation.thirdplatform.config;

import lombok.experimental.UtilityClass;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/26 14:30
 **/
@UtilityClass
public class ApiConstants {

	/**
	 * 分销商帐号
	 */
	public String CUST_ID = "4460512";

	/**
	 * 分销商验证码
	 */
	public String API_KEY = "5940CB690F9B9873B39984021FCDBF91";

	/**
	 * 分销域名
	 */
	public String REALM_NAME = "zxhy.ziwoyou.net";

	/**
	 * 产品列表
	 */
	public String PRODUCT_LIST_URL = "http://" + REALM_NAME + "/api/list.jsp";

	/**
	 * 产品详情
	 */
	public String PRODUCT_DETAIL_URL = "http://" + REALM_NAME + "/api/detail.jsp";

	/**
	 * 批量获取产品状态
	 */
	public String PRODUCT_STATUS_LIST_URL = "http://" + REALM_NAME + "/api/getProductState.jsp";

	/**
	 * 获取产品价格日历
	 */
	public String PRODUCT_SPEC_URL = "http://" + REALM_NAME + "/api/price.jsp";


	/**
	 *订单保存
	 */
	public String ORDER_SAVE_URL = "http://" + REALM_NAME + "/api/order.jsp";

	/**
	 *订单详情
	 */
	public String ORDER_DETAIL_URL = "http://" + REALM_NAME + "/api/orderDetail.jsp";

	/**
	 * 订单列表
	 */
	public String ORDER_LIST_URL = "http://" + REALM_NAME + "/api/orderList.jsp";

	/**
	 * 订单支付成功
	 */
	public String ORDER_PAY_SUCCESS_URL = "http://" + REALM_NAME + "/api/pay.jsp";

	/**
	 * 订单取消
	 */
	public String ORDER_CANCEL_URL = "http://" + REALM_NAME + "/api/cancelOrder.jsp";


	/**
	 * 订单部分取消接口
	 */
	public String ORDER_CANCEL_PART_URL = "http://" + REALM_NAME + "/api/refundOrder.jsp";


	/**
	 * 订单退改申请接口
	 */
	public String ORDER_CHANGE_URL = "http://" + REALM_NAME + "/api/changeApplyOrder.jsp";


	/**
	 * 批量获取订单状态
	 */
	public String ORDER_STATUS_URL = "http://" + REALM_NAME + "/api/getOrderState.jsp";


}
