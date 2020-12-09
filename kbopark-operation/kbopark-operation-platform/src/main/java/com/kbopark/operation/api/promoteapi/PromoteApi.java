package com.kbopark.operation.api.promoteapi;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.apidto.ReviewLog;
import com.kbopark.operation.dto.MerchantOrderSearchDTO;
import com.kbopark.operation.dto.MerchantPayOrderDTO;
import com.kbopark.operation.dto.MerchantSearchParam;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.enums.MerchantReviewStatusEnum;
import com.kbopark.operation.service.ConsumerOrderService;
import com.kbopark.operation.service.MerchantReviewLogService;
import com.kbopark.operation.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kbopark.operation.entity.Merchant.*;
import static com.kbopark.operation.enums.MerchantReviewStatusEnum.CHECK_SUCCESS;

@RestController
@AllArgsConstructor
@RequestMapping("/promote-api")
@Api(value = "promote-api", tags = "【推广员端相关接口】")
public class PromoteApi {

	private final MerchantService merchantService;

	private final MerchantReviewLogService reviewLogService;

	private final ConsumerOrderService consumerOrderService;


	@GetMapping("merchant-number")
	@ApiOperation("商家数量")
	public R<Map<String, Integer>> merchantNumber() {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		} else if (StrUtil.isBlank(user.getPromoteCode())) {
			return R.failed("您不是推广员");
		}
		Integer all = merchantService.count(w -> w.lambda()
				.eq(Merchant::getPromoteCode, user.getPromoteCode()));
		Integer reviewing = merchantService.count(w -> w.lambda()
				.eq(Merchant::getPromoteCode, user.getPromoteCode())
				.in(Merchant::getReviewStatus, CHECKING_STATUS));
		Integer success = merchantService.count(w -> w.lambda()
				.eq(Merchant::getPromoteCode, user.getPromoteCode())
				.eq(Merchant::getIsRead, Boolean.FALSE)
				.eq(Merchant::getReviewStatus, CHECK_SUCCESS.code));
		Integer fail = merchantService.count(w -> w.lambda()
				.eq(Merchant::getPromoteCode, user.getPromoteCode())
				.eq(Merchant::getIsRead, Boolean.FALSE)
				.in(Merchant::getReviewStatus, CHECK_FAIL_STATUS));
		Map<String, Integer> resMap = new HashMap<>(4);
		resMap.put("all", all);
		resMap.put("reviewing", reviewing);
		resMap.put("success", success);
		resMap.put("fail", fail);
		return R.ok(resMap);
	}

	@GetMapping("merchant-page")
	@ApiOperation("商家分页列表")
	public R<IPage<Merchant>> merchantPage(@RequestParam(value = "current", defaultValue = "1") Integer current,
										   @RequestParam(value = "size", defaultValue = "10") Integer size,
										   @ApiParam("审核状态，all 全部 reviewing 审核中 success 审核通过 fail 审核失败")
										   @RequestParam(value = "reviewStatus", defaultValue = "all") String reviewStatus,
										   @RequestParam(value = "keyword", required = false) String keyword) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		} else if (StrUtil.isBlank(user.getPromoteCode())) {
			return R.failed("您不是推广员");
		}
		MerchantSearchParam param = new MerchantSearchParam();
		param.setMerchantName(keyword);
		param.setPromoteCode(user.getPromoteCode());
		List<Integer> reviewStatusList;
		switch (reviewStatus) {
			case "reviewing":
				reviewStatusList = CHECKING_STATUS;
				break;
			case "success":
				reviewStatusList = CHECK_SUCCESS_STATUS;
				break;
			case "fail":
				reviewStatusList = CHECK_FAIL_STATUS;
				break;
			default:
				reviewStatusList = null;
		}
		if (null != reviewStatusList) {
			param.setReviewStatusKey(CollectionUtil.join(reviewStatusList, ","));
		}
//		LambdaQueryWrapper<Merchant> queryWrapper = Wrappers.lambdaQuery();
//		queryWrapper.like(StrUtil.isNotBlank(keyword), Merchant::getName, keyword)
//				.eq(Merchant::getPromoteCode, user.getPromoteCode())
//				.orderByDesc(Merchant::getId);
//		switch (reviewStatus) {
//			case "reviewing":
//				queryWrapper.in(Merchant::getReviewStatus, CHECKING_STATUS);
//				break;
//			case "success":
//				queryWrapper.eq(Merchant::getReviewStatus, CHECK_SUCCESS.code);
//				break;
//			case "fail":
//				queryWrapper.in(Merchant::getReviewStatus, CHECK_FAIL_STATUS);
//				break;
//			default:
//		}
//		return R.ok(merchantService.page(new Page<>(current, size), queryWrapper));
		return R.ok(merchantService.selectMerchantPage(new Page<>(current, size), param));
	}

	@ApiOperation("获取商家的详细信息, 供编辑使用")
	@GetMapping("/merchant-info/{id}")
	public R<Merchant> getMerchantInfo(@PathVariable Integer id) {
		Merchant entity = merchantService.getById(id);
		entity.setCanChange(true);
		if (ObjectUtil.equal(entity.getReviewStatus(), MerchantReviewStatusEnum.EDIT_DISTRIBUTOR_UN_CHECKED.code)
				|| ObjectUtil.equal(entity.getReviewStatus(), MerchantReviewStatusEnum.EDIT_OPERATOR_UN_CHECKED.code)) {
			entity = JSON.parseObject(entity.getTodoSnapshoot(), Merchant.class);
			entity.setCanChange(false);
		}
		return R.ok(entity);
	}

	@ApiOperation("获取商家的详细信息， 只供查看使用")
	@GetMapping("/merchant-view-info/{id}")
	public R<Merchant> getMerchantViewInfo(@PathVariable Integer id) {
		Merchant entity = merchantService.getById(id);
		if (null == entity) {
			return R.failed("查询商家信息失败");
		}
		// 处理查看的逻辑
		entity.setIsRead(true);
		merchantService.updateById(entity);
		entity.setCanChange(!CHECKING_STATUS.contains(entity.getReviewStatus()));
		return R.ok(entity);
	}

	@ApiOperation(value = "支付订单分页列表")
	@GetMapping("/payorder-page")
	public R<IPage<MerchantPayOrderDTO>> getConsumerOrderPage(@ApiParam("当前页")
															  @RequestParam(value = "current", defaultValue = "1") Integer current,
															  @ApiParam("每页条数")
															  @RequestParam(value = "size", defaultValue = "10") Integer size,
															  MerchantOrderSearchDTO param) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		} else if (StrUtil.isBlank(user.getPromoteCode())) {
			return R.failed("您不是推广员");
		}
		param.setPromoteCode(user.getPromoteCode());
		return R.ok(consumerOrderService.selectMerchantOrderPage(new Page<>(current, size), param));
	}

	@ApiOperation(value = "商家入驻")
	@SysLog("新增商家基本信息表")
	@PostMapping("merchant-enter")
	@PreAuthorize("@pms.hasPermission('operation_merchant_add')")
	public R merchantEnter(@RequestBody Merchant merchant) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		} else if (StrUtil.isBlank(user.getPromoteCode())) {
			return R.failed("您不是推广员");
		}
		return R.ok(merchantService.saveMerchant(merchant, user));
	}

	@ApiOperation(value = "修改商家信息", notes = "修改商家基本信息表")
	@SysLog("修改商家基本信息表")
	@PutMapping("merchant-update")
	public R<Boolean> update(@RequestBody Merchant merchant) {
		return R.ok(merchantService.updateMerchant(merchant, SecurityUtils.getUser()));
	}

	@ApiOperation("商家审核进展")
	@GetMapping("/merchant-reviewlog-list")
	public R<List<ReviewLog>> reviewLogList(@RequestParam(value = "id", required = true) Integer merchantId) {
		return R.ok(reviewLogService.getReviewProgress(merchantId));
	}

}
