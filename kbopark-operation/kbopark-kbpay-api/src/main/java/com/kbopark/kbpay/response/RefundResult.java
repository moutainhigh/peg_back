package com.kbopark.kbpay.response;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/12 11:03
 **/
@Data
public class RefundResult {

	public String errCode;
	public String errMsg;
	public String msgId;
	public String msgType;
	public String msgSrc;
	public String srcReserve;
	public String responseTimestamp;
	public String mid;
	public String tid;
	public String merOrderId;
	public String merName;
	public String seqId;
	public String status;
	public String targetMid;
	public String targetOrderId;
	public String targetStatus;
	public String targetSys;
	public Integer totalAmount;
	public Integer refundAmount;
	public String refundFunds;
	public String refundFundsDesc;
	public Integer refundInvoiceAmount;
	public String refundOrderId;
	public String refundTargetOrderId;
	public String sign;
	public Integer yxlmAmount;

}
