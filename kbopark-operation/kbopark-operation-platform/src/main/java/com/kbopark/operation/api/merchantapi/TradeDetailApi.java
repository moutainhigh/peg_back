package com.kbopark.operation.api.merchantapi;

import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.entity.LedgerDetail;
import com.kbopark.operation.service.LedgerDetailService;
import com.kbopark.operation.unionpay.enums.TransCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/22 11:37
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/tradeDetail")
@Api(value = "tradeDetail", tags = "分账明细查询")
public class TradeDetailApi {

	private final LedgerDetailService ledgerDetailService;

	/***
	 * 商家查询分账明细
	 * @param page
	 * @param ledgerDetail
	 * @return
	 */
	@ApiOperation(value = "分账明细分页查询", notes = "分账明细分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_ledgerdetail_view')")
	public R getLedgerDetailPage(Page page, LedgerDetail ledgerDetail) {
		KboparkUser user = SecurityUtils.getUser();
		//商家查询设置商家ID和交易码
		if(user.getUserType() >= UserTypeEnum.Merchant.getCode()){
			ledgerDetail.setMerchantId(user.getMerchantId());
			ledgerDetail.setTransCode(TransCodeEnum.T202002.getCode());
		}else{
			return R.failed("非商家查询无权限");
		}
		return R.ok(ledgerDetailService.page(page, Wrappers.query(ledgerDetail)));
	}

}
