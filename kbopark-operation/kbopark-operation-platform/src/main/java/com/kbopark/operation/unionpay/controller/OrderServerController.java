package com.kbopark.operation.unionpay.controller;

import cn.qdzhhl.kbopark.common.core.util.R;
import com.kbopark.operation.unionpay.dto.*;
import com.kbopark.operation.unionpay.service.SubAccountService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:sunwenzhi
 * @Description:分账接口
 * @Date: 2020/9/21 16:44
 **/
@RestController
@RequestMapping("/orderserver")
@AllArgsConstructor
public class OrderServerController {

	private final SubAccountService subAccountService;

	/**
	 * 按流水划付接口 202001
	 *
	 * @param orderPaymentDTO
	 * @return
	 */
	@PostMapping("/orderPayment")
	@ApiOperation(value = "按流水划付接口", notes = "按流水划付接口202001")
	public R sendOrderPayment(@RequestBody OrderPaymentDTO orderPaymentDTO) {
		return subAccountService.executeOrderPayment(orderPaymentDTO);
	}


	/**
	 * 按流水分账接口 202003
	 *
	 * @param orderSubAccountDTO
	 * @return
	 */
	@PostMapping("/orderSubAccount")
	@ApiOperation(value = "按流水分账接口", notes = "按流水分账接口202003")
	public R sendOrderSubAccount(@RequestBody OrderSubAccountDTO orderSubAccountDTO) {
		return subAccountService.executeOrderSubAccount(orderSubAccountDTO);
	}


	/**
	 * 按金额划付接口 202002
	 *
	 * @param moneyPaymentDTO
	 * @return
	 */
	@PostMapping("/moneyPayment")
	@ApiOperation(value = "按金额划付接口", notes = "按金额划付接口 202002")
	public R sendMoneyPayment(@RequestBody MoneyPaymentDTO moneyPaymentDTO) {
		return subAccountService.executeMoneyPayment(moneyPaymentDTO);
	}


	/**
	 * 按金额分账接口 202004
	 *
	 * @param moneySubAccountDTO
	 * @return
	 */
	@PostMapping("/moneySubAccount")
	@ApiOperation(value = "金额分账接口", notes = "按金额分账接口 202004")
	public R sendMoneySubAccount(@RequestBody MoneySubAccountDTO moneySubAccountDTO) {
		return subAccountService.executeMoneySubAccount(moneySubAccountDTO);
	}


	/**
	 * 商户信息查询接口 202006
	 *
	 * @param accountInfoDTO
	 * @return
	 */
	@PostMapping("/getAccountInfo")
	@ApiOperation(value = "商户信息查询接口", notes = "商户信息查询接口 202006")
	public R getAccountInfo(@RequestBody AccountInfoDTO accountInfoDTO) {
		return subAccountService.executeAccountInfo(accountInfoDTO);
	}


	/**
	 * 交易明细查询接口 202007
	 *
	 * @param tradeDetailDTO
	 * @return
	 */
	@PostMapping("/getTradeDetail")
	@ApiOperation(value = "交易明细查询接口", notes = "交易明细查询接口 202007")
	public R getTradeDetail(@RequestBody TradeDetailDTO tradeDetailDTO) {
		return subAccountService.executeTradeDetail(tradeDetailDTO);
	}



	/**
	 * 操作记录查询接口 202008
	 *
	 * @param handleRecordDTO
	 * @return
	 */
	@PostMapping("/getHandleRecord")
	@ApiOperation(value = "操作记录查询接口", notes = "操作记录查询接口 202008")
	public R getHandleRecord(@RequestBody HandleRecordDTO handleRecordDTO) {
		return subAccountService.executeHandleRecord(handleRecordDTO);
	}

}
