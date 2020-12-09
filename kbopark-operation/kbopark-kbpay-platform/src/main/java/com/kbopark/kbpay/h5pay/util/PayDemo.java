package com.kbopark.kbpay.h5pay.util;

import lombok.Data;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/22 11:04
 **/
@Component
@Data
@PropertySource("classpath:/config/h5payParams.properties")
public class PayDemo {

	@Value("${url}")
	private String APIurl;
	@Value("${mid}")
	private String mid;
	@Value("${tid}")
	private String tid;
	@Value("${instMid}")
	private String instMid;
	@Value("${msgSrc}")
	private String msgSrc;
	@Value("${key}")
	private String md5Key;
	@Value("${msgType_refund}")
	private String msgType_refund;
	@Value("${msgType_secureCancel}")
	private String msgType_secureCancel;
	@Value("${msgType_secureComplete}")
	private String msgType_secureComplete;
	@Value("${msgType_close}")
	private String msgType_close;
	@Value("${msgType_query}")
	private String msgType_query;

	final private static String msgType = "trade.h5Pay";
	final private static String msgSrcId = "3194";
	final private static String apiUrl_makeOrder = "https://qr-test2.chinaums.com/netpay-portal/webpay/pay.do";
	final private static String notifyUrl = "http://metro.dthuishenghuo.cn:30090/api/kbpay/pay-notify/ums-notify";
	final private static String returnUrl = "http://192.168.0.144:8080/voucher/payStuate";
	final private static String apiUrl_queryOrder = "https://qr-test2.chinaums.com/netpay-route-server/api/";


	public String sendRequest(){

		// 组织请求报文，具体参数请参照接口文档，以下仅作模拟
		JSONObject json = new JSONObject();
		json.put("mid", mid);
		json.put("tid", tid);
		json.put("msgType", msgType);
		json.put("msgSrc", msgSrc);
		json.put("instMid", instMid);

		json.put("merOrderId", H5PayUtil.genMerOrderId(msgSrcId));
		json.put("totalAmount", 1);
		json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		json.put("notifyUrl", notifyUrl);
		json.put("returnUrl", returnUrl);

		json.put("secureTransaction", false);
		json.put("signType", "SHA256");
		String url = H5PayUtil.makeOrderRequest(json, md5Key, apiUrl_makeOrder);

		return url;
	}

	public Map<String, String> getPayParam(){

		JSONObject json = new JSONObject();
		json.put("mid", mid);
		json.put("tid", tid);
		json.put("msgType", msgType);
		json.put("msgSrc", msgSrc);
		json.put("instMid", instMid);

		json.put("merOrderId", msgSrcId + H5PayUtil.getSerialNumber(null));
		json.put("totalAmount", 1000);
		json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		json.put("notifyUrl", notifyUrl);
		json.put("returnUrl", returnUrl);

		json.put("secureTransaction", false);
		json.put("signType", "SHA256");
		Map<String, String> params = H5PayUtil.jsonToMap(json);
		params.put("sign", H5PayUtil.makeSign(md5Key, params));
		return params;
	}

	public Map<String, String> getQueryParam(String orderNumber){

		JSONObject json = new JSONObject();
		json.put("mid", mid);
		json.put("tid", tid);
		json.put("msgType", msgType_query);
		json.put("msgSrc", msgSrc);
		json.put("instMid", instMid);

		json.put("merOrderId", msgSrcId + orderNumber);
		json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		json.put("signType", "SHA256");
		Map<String, String> params = H5PayUtil.jsonToMap(json);
		params.put("sign", H5PayUtil.makeSign(md5Key, params));
		return params;
	}

	public Map<String, String> getRefundParam(String orderNumber){

		JSONObject json = new JSONObject();
		json.put("mid", mid);
		json.put("tid", tid);
		json.put("msgType", msgType_refund);
		json.put("msgSrc", msgSrc);
		json.put("instMid", instMid);

		json.put("merOrderId", msgSrcId + orderNumber);
		json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		json.put("refundAmount","10");
		json.put("signType", "SHA256");
		Map<String, String> params = H5PayUtil.jsonToMap(json);
		params.put("sign", H5PayUtil.makeSign(md5Key, params));
		return params;
	}


	public Map<String, String> getRefundQueryParam(String orderNumber){

		JSONObject json = new JSONObject();
		json.put("mid", mid);
		json.put("tid", tid);
		json.put("msgType", "refundQuery");
		json.put("msgSrc", msgSrc);
		json.put("instMid", instMid);

		json.put("merOrderId", msgSrcId + orderNumber);
		json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		json.put("signType", "SHA256");
		Map<String, String> params = H5PayUtil.jsonToMap(json);
		params.put("sign", H5PayUtil.makeSign(md5Key, params));
		return params;
	}


	public Map<String, String> getCloseParam(String orderNumber){

		JSONObject json = new JSONObject();
		json.put("mid", mid);
		json.put("tid", tid);
		json.put("msgType", msgType_close);
		json.put("msgSrc", msgSrc);
		json.put("instMid", instMid);

		json.put("merOrderId", msgSrcId + orderNumber);
		json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		json.put("signType", "SHA256");
		Map<String, String> params = H5PayUtil.jsonToMap(json);
		params.put("sign", H5PayUtil.makeSign(md5Key, params));
		return params;
	}


}
