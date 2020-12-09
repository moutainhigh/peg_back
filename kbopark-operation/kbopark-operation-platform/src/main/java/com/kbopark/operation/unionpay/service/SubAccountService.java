package com.kbopark.operation.unionpay.service;

import cn.qdzhhl.kbopark.common.core.util.R;
import com.kbopark.operation.unionpay.dto.*;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/23 8:34
 **/
public interface SubAccountService {


	/**
	 * 按流水划付接口 202001
	 *
	 * @param orderPaymentDTO
	 * @return
	 */
	R executeOrderPayment(OrderPaymentDTO orderPaymentDTO);


	/**
	 * 按流水分账接口 202003
	 *
	 * @param orderSubAccountDTO
	 * @return
	 */
	R executeOrderSubAccount(OrderSubAccountDTO orderSubAccountDTO);


	/**
	 * 按金额划付接口 202002
	 *
	 * @param moneyPaymentDTO
	 * @return
	 */
	R executeMoneyPayment(MoneyPaymentDTO moneyPaymentDTO);


	/**
	 * 按金额分账接口 202004
	 *
	 * @param moneySubAccountDTO
	 * @return
	 */
	R executeMoneySubAccount(MoneySubAccountDTO moneySubAccountDTO);


	/**
	 * 商户信息查询接口 202006
	 *
	 * @param accountInfoDTO
	 * @return
	 */
	R executeAccountInfo(AccountInfoDTO accountInfoDTO);

	/**
	 * 交易明细查询接口 202007
	 *
	 * @param tradeDetailDTO
	 * @return
	 */
	R executeTradeDetail(TradeDetailDTO tradeDetailDTO);


	/**
	 * 操作记录查询接口 202008
	 *
	 * @param handleRecordDTO
	 * @return
	 */
	R executeHandleRecord(HandleRecordDTO handleRecordDTO);

}
