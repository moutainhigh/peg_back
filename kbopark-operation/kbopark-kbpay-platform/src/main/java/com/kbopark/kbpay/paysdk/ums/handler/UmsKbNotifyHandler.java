package com.kbopark.kbpay.paysdk.ums.handler;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.kbopark.kbpay.entity.PayTradeOrder;
import com.kbopark.kbpay.h5pay.util.H5PayUtil;
import com.kbopark.kbpay.paysdk.AbstractPayNotifyHandler;
import com.kbopark.kbpay.paysdk.IPayNotifyHandler;
import com.kbopark.kbpay.paysdk.PayChannelValueEnum;
import com.kbopark.kbpay.paysdk.PayTradeOrderPayStatusEnum;
import com.kbopark.kbpay.paysdk.ums.util.SignUtil;

import java.time.LocalDateTime;
import java.util.Map;

public class UmsKbNotifyHandler extends AbstractPayNotifyHandler<Map<String, String>, String> {

	private String returnBody;
	private PayTradeOrderPayStatusEnum payTradeOrderPayStatusEnum;
	private boolean shouldNotifyBiz;

	public UmsKbNotifyHandler(PayTradeOrder tradeOrder, Map<String, String> notifyBody) {
		super(tradeOrder, notifyBody);
	}

	@Override
	public IPayNotifyHandler<Map<String, String>, String> handle(Map<String, String> notifyBody) {
		boolean hasPayed = false;
		// 如果已经支付了,那么返回值就是SUCCESS
		if (PayTradeOrderPayStatusEnum.codeOf(tradeOrder.getPayStatus()) == PayTradeOrderPayStatusEnum.PAID) {
			hasPayed = true;
			this.returnBody = "SUCCESS";
		} else if (!verifySignature()) {
			this.returnBody = "签名验证失败";
			payTradeOrderPayStatusEnum = PayTradeOrderPayStatusEnum.UN_PAY;
		} else if ("TRADE_SUCCESS".equals(notifyBody.get("status"))) {
			this.returnBody = "SUCCESS";
			payTradeOrderPayStatusEnum = PayTradeOrderPayStatusEnum.PAID;
		} else {
			this.returnBody = "FAILED";
			payTradeOrderPayStatusEnum = PayTradeOrderPayStatusEnum.UN_PAY;
		}
		// 更新订单状态
		if (payTradeOrderPayStatusEnum == PayTradeOrderPayStatusEnum.PAID && !hasPayed) {
			shouldNotifyBiz = true;
			tradeOrder.setPayStatus(payTradeOrderPayStatusEnum.code);
			tradeOrder.setPayMethod(notifyBody.get("targetSys"));
			tradeOrder.setPaySuccessTime(LocalDateTime.now());
		}
		tradeOrder.setLastNotifyTime(LocalDateTime.now());
		tradeOrder.setNotifyCount(tradeOrder.getNotifyCount() + 1);
		return this;
	}

	@Override
	public String getReturnBody() {
		return returnBody;
	}

	@Override
	public Boolean shouldNotifyBiz() {
		return shouldNotifyBiz;
	}

	@Override
	public PayTradeOrder getTradeOrder() {
		return tradeOrder;
	}


	/**
	 * 验证签名
	 *
	 * @return
	 */
	private boolean verifySignature() {
		if(PayChannelValueEnum.CHINA_H5.getCode().equals(tradeOrder.getPayChannelValue())){
			return H5PayUtil.checkSign(tradeOrder.getParamsMap().get("md5SecretKey"), notifyBody);
		}else{
			String rawSign = notifyBody.get("sign");
			String sign = SignUtil.getSign(notifyBody,
					tradeOrder.getParamsMap().get("md5SecretKey"),
					ObjectUtil.equal(notifyBody.get("signType"), "sha256"));
			return ObjectUtil.equal(rawSign.toUpperCase(), sign.toUpperCase());
		}
	}


}
