package com.kbopark.operation.fegin;

import cn.qdzhhl.kbopark.common.core.constant.ServiceNameConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import com.kbopark.operation.entity.Merchant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/3 17:34
 **/
@FeignClient(contextId = "remoteMerchantService", value = ServiceNameConstants.OPERATION_SERVICE)
public interface RemoteMerchantService {

	String API_PREFIX = "/feign/merchant/";

	String GET_BY_ID = API_PREFIX + "{id}";

	/**
	 * 获取商家信息
	 * @param id
	 * @return
	 */
	@GetMapping(GET_BY_ID)
	R<Merchant> getById(@PathVariable("id") Integer id);


}
