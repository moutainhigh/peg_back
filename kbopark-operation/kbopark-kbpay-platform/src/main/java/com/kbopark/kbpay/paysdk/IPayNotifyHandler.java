package com.kbopark.kbpay.paysdk;

import com.alibaba.fastjson.JSON;
import com.kbopark.kbpay.entity.PayNotifyRecord;
import com.kbopark.kbpay.entity.PayTradeOrder;

import java.time.LocalDateTime;

/**
 * 支付回调处理器
 *
 * @param <T> 传入的通知报文的数据类型
 * @param <R> 返回报文的数据类型
 * @author laomst
 */
public interface IPayNotifyHandler<T,R> {


	IPayNotifyHandler<T,R> handle(T notifyBody);

	/**
	 * 获取返回体
	 *
	 * @return
	 */
	R getReturnBody();

	/**
	 * 获取是否应该通知业务系统
	 * @return
	 */
	Boolean shouldNotifyBiz();

	/**
	 * 获取更新过后的订单
	 * @return
	 */
	PayTradeOrder getTradeOrder();

}
