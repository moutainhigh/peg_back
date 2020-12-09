package com.kbopark.operation.api.merchantapi;

import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.kbopark.operation.dto.CreateOrderDTO;
import com.kbopark.operation.dto.FeedBackDTO;
import com.kbopark.operation.service.FeedbackInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/12 9:12
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/feedback")
@Api(value = "feedback", tags = "【用户信息反馈】")
public class FeedBackApi {

	private final FeedbackInfoService feedbackInfoService;


	/**
	 * 用户信息反馈
	 * @param feedBackDTO
	 * @return
	 */
	@ApiOperation(value = "反馈信息提交接口", notes = "反馈信息提交接口")
	@SysLog("反馈信息提交接口")
	@PostMapping("/create")
	public R createFeedBack(@Valid @RequestBody FeedBackDTO feedBackDTO) {
		KboparkUser user = SecurityUtils.getUser();
		feedBackDTO.setName(user.getRealName());
		feedBackDTO.setPhone(user.getPhone());
		return R.ok(feedbackInfoService.save(feedBackDTO));
	}

}
