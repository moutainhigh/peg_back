package com.kbopark.kbpay.paysdk;


import com.kbopark.kbpay.entity.PayTradeOrder;

public class NoSuchChannelPayService implements IKbPayService<PayResult> {
	@Override
	public PayResult jsPay(PayTradeOrder order) {
		throw new UnsupportedOperationException("系统不支持" + order.getPayChannelValue() + "类型的支付通道");
	}

	@Override
	public PayResult createH5PayUrl(PayTradeOrder order) {
		throw new UnsupportedOperationException("系统不支持" + order.getPayChannelValue() + "类型的支付通道");
	}

	@Override
	public String generatePayChannelOrderId(PayTradeOrder payTradeOrder) {
		return null;
	}
}
