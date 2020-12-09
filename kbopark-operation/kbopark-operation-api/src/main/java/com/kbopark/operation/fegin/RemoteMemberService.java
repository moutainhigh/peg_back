package com.kbopark.operation.fegin;

import cn.qdzhhl.kbopark.admin.api.dto.UserInfo;
import cn.qdzhhl.kbopark.common.core.constant.SecurityConstants;
import cn.qdzhhl.kbopark.common.core.constant.ServiceNameConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/3 17:34
 **/
@FeignClient(contextId = "remoteMemberService", value = ServiceNameConstants.OPERATION_SERVICE)
public interface RemoteMemberService {


	/**
	 * 通过手机号查询会员信息
	 *
	 * @param phone	手机号
	 * @param from	请求来源
	 * @return
	 */
	@ApiOperation(value = "通过手机号查询", notes = "通过手机号查询")
	@GetMapping("/memberinfo/findByPhone/{phone}")
	R<UserInfo> findByPhone(@PathVariable("phone") String phone, @RequestHeader(SecurityConstants.FROM) String from);

}
