package com.kbopark.kbpay.paysdk.ums.dto;

import cn.hutool.core.date.DateUtil;
import com.kbopark.kbpay.entity.PayTradeOrder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
public class UmsOrderParams implements Serializable {
	private static final long serialVersionUID = 1L;

	private String msgSrc; // 消息来源
	private String msgType; // 消息类型
//	private String msgId; // 消息ID，在通知的时候会原样返回
	private String requestTimestamp = DateUtil.formatDateTime(new Date()); //请求时间戳
	private String merOrderId; //商户订单号
	private String mid; //商户号
	private String tid; // 终端号
	private String instMid; // 机构商户号
	private String signType = "SHA256";
	private String orderDesc; // 商品描述
	private Integer totalAmount; // 支付总金额, 单位分
	private String notifyUrl;
	private String returnUrl;

	public UmsOrderParams() {
	}

	public UmsOrderParams(PayTradeOrder order) {
		Map<String, String> propsMap = order.getParamsMap();
		msgSrc = propsMap.get("msgSrc");
		msgType = propsMap.get("msgType");
		merOrderId = order.getPayChannelOrderId();
		mid = propsMap.get("mid");
		tid = propsMap.get("tid");
		instMid = propsMap.get("instMid");
		orderDesc = order.getOrderDescription();
		totalAmount = order.getMoney().multiply(BigDecimal.valueOf(100)).intValue();
		notifyUrl = order.getNotifyUrl();
		returnUrl = order.getReturnUrl();
//		msgId = order.getTradeBizNumber();
	}
}
