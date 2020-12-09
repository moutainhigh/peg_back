package com.kbopark.operation.unionpay.dto;

import java.io.Serializable;

/**
 * SuperUtil params dto
 */
public class SuperDto implements Serializable{

    //交易码
    private String transCode;

    //版本号
    private String verNo;

    //渠道号，商户资金自主管理系统唯一标识
    private String channelId;

    //测试集团号，生产环境根据申请的为准
    private String groupId;

    //企业用户号
    private String merNo;

    //分账金额
    private String payAmt;

    //分账类型
    private String payType;

    //附言
    private String ps;

    //商户订单号
    private String merOrderNo;

    //卡号
    private String cardNo;

    //交易日期
    private String transDate;

    //查询项
    private String queryItem;

    //查询值
    private String queryValue;

    //签名
    private String signature;

    //请求日期
    private String reqDate;

    //请求交易流水号
    private String reqJournalNo;

    /*----返回报文所需其他属性-----*/
    private String allottedAmt;
    private String canPayAmt;
    private String cardType;
    private String merFee;
    private String oriRefNo;
    private String oriTransDt;
    private String oriTxnAmt;
    private String paidAmt;
    private String pan;
    private String respCode;
    private String respMsg;
    private String settleStatus;
    private String srcReqDate;
    private String srcReqId;
    private String srcReqTime;
    private String txnAmt;
    private String transDt;
    private String status;
    private String merName;
    private String topMer;
    private String merLevel;
    private String orderSet;
    /*----返回报文所需其他属性-----*/

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getVerNo() {
        return verNo;
    }

    public void setVerNo(String verNo) {
        this.verNo = verNo;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(String payAmt) {
        this.payAmt = payAmt;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getMerOrderNo() {
        return merOrderNo;
    }

    public void setMerOrderNo(String merOrderNo) {
        this.merOrderNo = merOrderNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getQueryItem() {
        return queryItem;
    }

    public void setQueryItem(String queryItem) {
        this.queryItem = queryItem;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getReqJournalNo() {
        return reqJournalNo;
    }

    public void setReqJournalNo(String reqJournalNo) {
        this.reqJournalNo = reqJournalNo;
    }

    public String getAllottedAmt() {
        return allottedAmt;
    }

    public void setAllottedAmt(String allottedAmt) {
        this.allottedAmt = allottedAmt;
    }

    public String getCanPayAmt() {
        return canPayAmt;
    }

    public void setCanPayAmt(String canPayAmt) {
        this.canPayAmt = canPayAmt;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getMerFee() {
        return merFee;
    }

    public void setMerFee(String merFee) {
        this.merFee = merFee;
    }

    public String getOriRefNo() {
        return oriRefNo;
    }

    public void setOriRefNo(String oriRefNo) {
        this.oriRefNo = oriRefNo;
    }

    public String getOriTransDt() {
        return oriTransDt;
    }

    public void setOriTransDt(String oriTransDt) {
        this.oriTransDt = oriTransDt;
    }

    public String getOriTxnAmt() {
        return oriTxnAmt;
    }

    public void setOriTxnAmt(String oriTxnAmt) {
        this.oriTxnAmt = oriTxnAmt;
    }

    public String getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(String settleStatus) {
        this.settleStatus = settleStatus;
    }

    public String getSrcReqDate() {
        return srcReqDate;
    }

    public void setSrcReqDate(String srcReqDate) {
        this.srcReqDate = srcReqDate;
    }

    public String getSrcReqId() {
        return srcReqId;
    }

    public void setSrcReqId(String srcReqId) {
        this.srcReqId = srcReqId;
    }

    public String getSrcReqTime() {
        return srcReqTime;
    }

    public void setSrcReqTime(String srcReqTime) {
        this.srcReqTime = srcReqTime;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getTransDt() {
        return transDt;
    }

    public void setTransDt(String transDt) {
        this.transDt = transDt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public String getTopMer() {
        return topMer;
    }

    public void setTopMer(String topMer) {
        this.topMer = topMer;
    }

    public String getMerLevel() {
        return merLevel;
    }

    public void setMerLevel(String merLevel) {
        this.merLevel = merLevel;
    }

    public String getOrderSet() {
        return orderSet;
    }

    public void setOrderSet(String orderSet) {
        this.orderSet = orderSet;
    }

    @Override
    public String toString() {
        return "SuperDto{" +
                "transCode='" + transCode + '\'' +
                ", verNo='" + verNo + '\'' +
                ", channelId='" + channelId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", merNo='" + merNo + '\'' +
                ", payAmt='" + payAmt + '\'' +
                ", payType='" + payType + '\'' +
                ", ps='" + ps + '\'' +
                ", merOrderNo='" + merOrderNo + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", transDate='" + transDate + '\'' +
                ", queryItem='" + queryItem + '\'' +
                ", queryValue='" + queryValue + '\'' +
                ", signature='" + signature + '\'' +
                ", reqDate='" + reqDate + '\'' +
                ", reqJournalNo='" + reqJournalNo + '\'' +
                ", allottedAmt='" + allottedAmt + '\'' +
                ", canPayAmt='" + canPayAmt + '\'' +
                ", cardType='" + cardType + '\'' +
                ", merFee='" + merFee + '\'' +
                ", oriRefNo='" + oriRefNo + '\'' +
                ", oriTransDt='" + oriTransDt + '\'' +
                ", oriTxnAmt='" + oriTxnAmt + '\'' +
                ", paidAmt='" + paidAmt + '\'' +
                ", pan='" + pan + '\'' +
                ", respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                ", settleStatus='" + settleStatus + '\'' +
                ", srcReqDate='" + srcReqDate + '\'' +
                ", srcReqId='" + srcReqId + '\'' +
                ", srcReqTime='" + srcReqTime + '\'' +
                ", txnAmt='" + txnAmt + '\'' +
                ", transDt='" + transDt + '\'' +
                ", status='" + status + '\'' +
                ", merName='" + merName + '\'' +
                ", topMer='" + topMer + '\'' +
                ", merLevel='" + merLevel + '\'' +
                ", orderSet='" + orderSet + '\'' +
                '}';
    }
}
