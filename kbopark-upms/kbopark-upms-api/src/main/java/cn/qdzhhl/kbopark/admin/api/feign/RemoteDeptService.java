package cn.qdzhhl.kbopark.admin.api.feign;

import cn.qdzhhl.kbopark.admin.api.entity.SysDept;
import cn.qdzhhl.kbopark.common.core.constant.SecurityConstants;
import cn.qdzhhl.kbopark.common.core.constant.ServiceNameConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/8/28 15:34
 **/
@FeignClient(contextId = "remoteDeptService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteDeptService {


	/**
	 * 通过ID查询
	 *
	 * @param id
	 * @param from
	 * @return
	 */
	@GetMapping("/dept/remote/{id}")
	R<SysDept> remoteQueryId(@PathVariable("id") Integer id, @RequestHeader(SecurityConstants.FROM) String from);


	/**
	 * 获取机构列表
	 *
	 * @param from	来源
	 * @return
	 */
	@GetMapping("/dept/getDeptList")
	R<List<SysDept>> getDeptList(@RequestHeader(SecurityConstants.FROM) String from);
}
