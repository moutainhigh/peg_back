package com.kbopark.kbpay.paysdk;


import com.kbopark.kbpay.entity.PayTradeOrder;

public interface IKbPayService<R extends PayResult> {

	/**
	 * 获取扫码支付的支付链接，此链接只能在微信或支付宝客户端打开，不能通过第三方APP使用
	 * @param order
	 * @return
	 */
	R jsPay(PayTradeOrder order);


	/**
	 * 创建H5支付的支付链接，此链接可以在第三方应用中调起支付宝客户端进行支付
	 * （银联支付通道支持问题，目前仅使用支付宝支付）
	 * @param order
	 * @return
	 */
	R createH5PayUrl(PayTradeOrder order);

	/**
	 * 生成传送给支付平台的支付订单号, 如果返回的结果是blank的，那么就使用默认值
	 * @param payTradeOrder
	 * @return
	 */
	String generatePayChannelOrderId(PayTradeOrder payTradeOrder);
}
