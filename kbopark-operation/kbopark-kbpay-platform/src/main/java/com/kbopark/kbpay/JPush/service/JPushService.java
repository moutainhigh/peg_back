package com.kbopark.kbpay.JPush.service;

import com.kbopark.operation.entity.ConsumerOrder;

import java.util.List;

public interface JPushService {
	/**
	 * 支付成功消息推送
	 * @param consumerOrder 支付成功的订单号
	 */
	void pushPaySuccessNotify(ConsumerOrder consumerOrder);

	void pushPaySuccessNotify(String bizNumber);

	public void sendPushByAlias(String content, List<String> aliases);
}
