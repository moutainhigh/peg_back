package com.kbopark.kbpay.paysdk;


import com.kbopark.kbpay.entity.PayTradeOrder;

public abstract class AbstractPayNotifyHandler<T,R> implements IPayNotifyHandler<T,R> {
	protected final PayTradeOrder tradeOrder;
	protected final T notifyBody;
	protected boolean shouldNotifyBiz;

	public AbstractPayNotifyHandler(PayTradeOrder tradeOrder, T notifyBody) {
		this.tradeOrder = tradeOrder;
		this.notifyBody = notifyBody;
	}

}
