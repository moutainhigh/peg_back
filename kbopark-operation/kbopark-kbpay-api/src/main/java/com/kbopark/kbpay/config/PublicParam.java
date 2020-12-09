package com.kbopark.kbpay.config;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/12 10:44
 **/
@Data
public class PublicParam {

	/**
	 *	消息来源
	 */
	private String msgSrc;
	/**
	 * 消息类型
	 */
	private String msgType;
	/**
	 *	商户号
	 */
	private String mid;
	/**
	 * 终端号
	 */
	private String tid;
	/**
	 * 业务类型,和查询业务保持一致
	 */
	private String instMid;

	/**
	 * 报文请求时间，格式yyyy-MM-dd HH:mm:ss
	 */
	private String requestTimestamp;

	/**
	 * 商户订单号
	 */
	private String merOrderId;

	/**
	 * 签名类型
	 */
	private String signType;

	/**
	 * 数据签名
	 */
	private String sign;

}
