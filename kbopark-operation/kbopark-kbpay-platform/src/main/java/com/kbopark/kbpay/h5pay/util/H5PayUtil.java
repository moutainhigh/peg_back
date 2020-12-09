package com.kbopark.kbpay.h5pay.util;

import cn.hutool.core.date.DateUtil;
import com.kbopark.kbpay.entity.PayTradeOrder;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/22 10:53
 **/
public class H5PayUtil {

	public static JSONObject makeOrderInfo(PayTradeOrder order){
		Map<String, String> propsMap = order.getParamsMap();

		JSONObject json = new JSONObject();
		json.put("mid", propsMap.get("mid"));
		json.put("tid", propsMap.get("tid"));
		json.put("msgType", propsMap.get("msgType"));
		json.put("msgSrc", propsMap.get("msgSrc"));
		json.put("instMid", propsMap.get("instMid"));

		json.put("merOrderId", order.getPayChannelOrderId());
		json.put("totalAmount", order.getMoney().multiply(BigDecimal.valueOf(100)).intValue());
		json.put("requestTimestamp", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));

		json.put("notifyUrl", order.getNotifyUrl());
		json.put("returnUrl", order.getReturnUrl());
		json.put("orderDesc", order.getOrderDescription());

		json.put("secureTransaction", false);
		json.put("sceneType", "AND_WAP");
		json.put("signType", "SHA256");
		return json;
	}


	public static String makeOrderRequest(JSONObject json, String md5Key, String apiUrl) {
		Map<String, String> params = jsonToMap(json);
		params.put("sign", makeSign(md5Key, params));
		return apiUrl + "?" + buildUrlParametersStr(params);
	}


	public static String makeSign(String md5Key, Map<String, String> params) {
		String preStr = buildSignString(params); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String text = preStr + md5Key;
		return DigestUtils.sha256Hex(getContentBytes(text)).toUpperCase();
	}

	public static boolean checkSign(String md5Key, Map<String, String> params) {
		String sign = params.get("sign");

		if (StringUtils.isBlank(sign)) {
			return false;
		}

		String signV = makeSign(md5Key, params);

		return StringUtils.equalsIgnoreCase(sign, signV);
	}

	// 获取HttpServletRequest里面的参数，并decode
	public static Map<String, String> getRequestParams(HttpServletRequest request) {
		Map<String, String[]> params = request.getParameterMap();
		Map<String, String> params2 = new HashMap<>();
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			if (values.length > 0) {
				params2.put(key, values[0]);
			}
		}
		return params2;
	}

	public static String genMerOrderId(String msgId) {
		String date = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
		String rand = RandomStringUtils.randomNumeric(7);
		return msgId + date + rand;
	}

	private static String buildUrlParametersStr(Map<String, String> paramMap) {
		Map.Entry entry;
		StringBuffer buffer = new StringBuffer();

		for (Iterator iterator = paramMap.entrySet().iterator(); iterator.hasNext(); ) {
			entry = (Map.Entry) iterator.next();

			buffer.append(entry.getKey().toString()).append("=");
			try {
				if (entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())) {
					buffer.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
				}
			} catch (UnsupportedEncodingException e) {
			}

			buffer.append(iterator.hasNext() ? "&" : "");
		}

		return buffer.toString();
	}

	// 使json-lib来进行json到map的转换，fastjson有排序问题，不能用
	public static Map<String, String> jsonToMap(JSONObject json) {
		Map<String, String> map = new HashMap<>();
		for (Object key : json.keySet()) {
			String value = json.optString((String) key);
			map.put((String) key, value);
		}
		return map;
	}

	// 构建签名字符串
	private static String buildSignString(Map<String, String> params) {

		if (params == null || params.size() == 0) {
			return "";
		}

		List<String> keys = new ArrayList<>(params.size());

		for (String key : params.keySet()) {
			if ("sign".equals(key)) {
				continue;
			}
			if (StringUtils.isEmpty(params.get(key))) {
				continue;
			}
			keys.add(key);
		}

		Collections.sort(keys);

		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				buf.append(key + "=" + value);
			} else {
				buf.append(key + "=" + value + "&");
			}
		}

		return buf.toString();
	}

	// 根据编码类型获得签名内容byte[]
	private static byte[] getContentBytes(String content) {
		try {
			return content.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("签名过程中出现错误");
		}
	}

	/**
	 * @title:获取指定位数的随机数，可指定为纯数字或数字字母组合
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/8/7 10:53
	 * @param:
	 * @return:
	 */
	public static String getRandomCharAndNumber(Integer length, boolean isNumber) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			boolean b = random.nextBoolean();
			// 字符串
			if (b) {
				if (isNumber) {
					str += String.valueOf(random.nextInt(10));
				} else {
					// 取得大写字母
					str += (char) (65 + random.nextInt(26));
				}
			} else {
				// 数字
				str += String.valueOf(random.nextInt(10));
			}
		}
		return str;
	}


	/**
	 * @title:获取流水号
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/8/7 10:53
	 * @param:
	 * @return:
	 */
	public static String getSerialNumber(String suffix) {
		String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String t = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
		String s = getRandomCharAndNumber(4, true);
		String e = getRandomCharAndNumber(4, true);
		if (StringUtils.isBlank(suffix)) {
			return d + s + t + e;
		} else {
			return suffix + d + s + t + e;
		}
	}

}
