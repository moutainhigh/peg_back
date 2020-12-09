package com.kbopark.kbpay.h5pay.controller;

import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.kbopark.kbpay.h5pay.util.PayDemo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/22 11:14
 **/
@RestController
@RequestMapping("/h5pay")
@AllArgsConstructor
public class UnionPayController {

	private final PayDemo payDemo;

	@ApiOperation(value = "h5支付", notes = "h5支付")
	@SysLog("h5支付")
	@PostMapping("/createOrder")
	@Inner(value = false)
	public R createOrder() {
		String s = payDemo.sendRequest();
		return R.ok(s);
	}

	@PostMapping("/getPayParam")
	@Inner(value = false)
	public R getPayParam() {
		return R.ok(payDemo.getPayParam());
	}

	@PostMapping("/getQueryParam")
	@Inner(value = false)
	public R getQueryParam(@RequestBody String order) {
		return R.ok(payDemo.getQueryParam(order));
	}

	@PostMapping("/getRefundParam")
	@Inner(value = false)
	public R getRefundParam(@RequestBody String order) {
		return R.ok(payDemo.getRefundParam(order));
	}

	@PostMapping("/getRefundQueryParam")
	@Inner(value = false)
	public R getRefundQueryParam(@RequestBody String order) {
		return R.ok(payDemo.getRefundQueryParam(order));
	}

	@PostMapping("/getCloseParam")
	@Inner(value = false)
	public R getCloseParam(@RequestBody String order) {
		return R.ok(payDemo.getCloseParam(order));
	}



}
