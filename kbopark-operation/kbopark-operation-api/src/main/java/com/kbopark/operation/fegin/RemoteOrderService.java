package com.kbopark.operation.fegin;

import cn.qdzhhl.kbopark.common.core.constant.ServiceNameConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import com.kbopark.operation.entity.ConsumerOrder;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/3 17:34
 **/
@FeignClient(contextId = "remoteOrderService", value = ServiceNameConstants.OPERATION_SERVICE)
public interface RemoteOrderService {


	/**
	 * 通过订单号查询订单信息
	 *
	 * @param orderNumber  订单号
	 * @return R
	 */
	@ApiOperation(value = "通过订单号查询", notes = "通过订单号查询")
	@GetMapping("/consumerorder/info/{orderNumber}")
	R<ConsumerOrder> getByOrderNumber(@PathVariable("orderNumber") String orderNumber);


	/**
	 * 支付成功，异步通知更新状态
	 *
	 * @param orderNumber  订单号
	 * @return R
	 */
	@ApiOperation(value = "异步通知更新状态", notes = "异步通知更新状态")
	@GetMapping("/consumerorder/notify/{orderNumber}")
	R notifySuccess(@PathVariable("orderNumber") String orderNumber);

}
