package com.kbopark.kbpay.paysdk.ums.service;

import cn.hutool.http.HttpUtil;
import com.kbopark.kbpay.entity.PayTradeOrder;
import com.kbopark.kbpay.h5pay.util.H5PayUtil;
import com.kbopark.kbpay.paysdk.IKbPayService;
import com.kbopark.kbpay.paysdk.PayResult;
import com.kbopark.kbpay.paysdk.ums.dto.UmsOrderParams;
import com.kbopark.kbpay.paysdk.ums.util.SignUtil;
import net.sf.json.JSONObject;

import java.util.Map;

public final class KbUmsPayService implements IKbPayService<PayResult> {
	@SuppressWarnings("all")
	private KbUmsPayService() {
		// 防止通过反射的方式破坏单例
		if (InstanceHolder.instance != null) {
			throw new UnsupportedOperationException("不允许通过反射创建实例!");
		}
	}

	public static KbUmsPayService getInstance() {
		return InstanceHolder.instance;
	}

	@Override
	public PayResult jsPay(PayTradeOrder order) {
		UmsOrderParams params = new UmsOrderParams(order);
		Map<String, String> paramMap = SignUtil.getParamsMapIncludeSign(params, order.getParamsMap().get("md5SecretKey"), true);
		return new PayResult(order.getPayChannelValue(), order.getPayServiceUrl() + "?" + HttpUtil.toParams(paramMap));
	}

	@Override
	public PayResult createH5PayUrl(PayTradeOrder order) {
		JSONObject jsonObject = H5PayUtil.makeOrderInfo(order);
		String url = H5PayUtil.makeOrderRequest(jsonObject, order.getParamsMap().get("md5SecretKey"), order.getPayServiceUrl());
		return new PayResult(order.getPayChannelValue(),url);
	}

	@Override
	public String generatePayChannelOrderId(PayTradeOrder payTradeOrder) {
		return payTradeOrder.getParamsMap().get("systemId") + payTradeOrder.getTradeBizNumber();
	}

	public static class InstanceHolder {
		private static final KbUmsPayService instance = new KbUmsPayService();
	}
}
