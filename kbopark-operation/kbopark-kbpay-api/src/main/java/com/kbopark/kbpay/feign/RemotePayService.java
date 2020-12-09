package com.kbopark.kbpay.feign;

import cn.qdzhhl.kbopark.common.core.constant.ServiceNameConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import com.kbopark.kbpay.dto.RefundDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/28 17:26
 **/
@FeignClient(contextId = "remotePayService", value = ServiceNameConstants.KBPAY_SERVICE)
public interface RemotePayService {


	/**
	 * 通过订单号申请退款
	 *
	 * @param refundDTO
	 * @return
	 */
	@ApiOperation(value = "通过订单号申请退款", notes = "通过订单号申请退款")
	@PostMapping("/pay-api/refund")
	R refundCall(@RequestBody RefundDTO refundDTO);

}
