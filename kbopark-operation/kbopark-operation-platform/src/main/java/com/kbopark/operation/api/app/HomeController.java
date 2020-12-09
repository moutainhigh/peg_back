package com.kbopark.operation.api.app;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbopark.operation.apidto.AppBannerListDTO;
import com.kbopark.operation.entity.QuestionDetail;
import com.kbopark.operation.entity.QuestionType;
import com.kbopark.operation.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/11 9:29
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/home")
@Api(value = "home", tags = "【首页相关接口】")
public class HomeController {

	private final SubwaySiteService subwaySiteService;

	private final AppBannerService bannerManageService;

	private final SystemSettingsService systemSettingsService;

	public final QuestionTypeService questionTypeService;

	public final QuestionDetailService questionDetailService;

	/***
	 * 获取地铁线路树
	 * @return
	 */
	@ApiOperation(value = "获取地铁线路树", notes = "获取地铁线路树")
	@SysLog("获取地铁线路树")
	@GetMapping("/lineTree")
	@Inner(value = false)
	public R getSubwayLineTree() {
		return subwaySiteService.buildSubwayLineTree();
	}

	@ApiOperation(value = "获取首页轮播", notes = "获取首页轮播")
	@SysLog("获取首页轮播")
	@GetMapping("/banner")
	@Inner(value = false)
	public R<AppBannerListDTO> getBanner() {
		return R.ok(bannerManageService.getGroupedBannerList());
	}

	/**
	 * 获取系统参数设置
	 *
	 * @return
	 */
	@ApiOperation(value = "获取系统参数设置", notes = "获取系统参数设置")
	@SysLog("获取系统参数设置")
	@GetMapping("/setting")
	@Inner(value = false)
	public R getSystemSetting() {
		return R.ok(systemSettingsService.getOne(Wrappers.query(), false));
	}

	@GetMapping("/question-help")
	@ApiOperation("常见问题帮助")
	public R<List<QuestionType>> questionHelp(@RequestParam(name = "title", required = false) String title) {
		List<QuestionType> typeList = questionTypeService.list();
		List<QuestionDetail> detailList = questionDetailService.list(w -> w.lambda().like(StrUtil.isNotBlank(title), QuestionDetail::getQuestionTitle, title));
		Map<String, List<QuestionDetail>> groupedDetail = detailList.stream().collect(Collectors.groupingBy(QuestionDetail::getQuestionType));
		typeList.forEach(item -> item.setQuestionDetailList(groupedDetail.getOrDefault(item.getValue(), Collections.emptyList())));
		List<QuestionType> resList = typeList.stream().filter(item -> CollectionUtil.isNotEmpty(item.getQuestionDetailList())).collect(Collectors.toList());
		return R.ok(resList);
	}
}
