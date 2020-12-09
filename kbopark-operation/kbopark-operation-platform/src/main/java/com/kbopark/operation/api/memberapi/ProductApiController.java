package com.kbopark.operation.api.memberapi;

import cn.hutool.core.util.StrUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.entity.CouponInfo;
import com.kbopark.operation.thirdplatform.dto.ProductDetailDTO;
import com.kbopark.operation.thirdplatform.dto.ProductListDTO;
import com.kbopark.operation.thirdplatform.dto.ProductSpecDTO;
import com.kbopark.operation.thirdplatform.dto.ProductStatusDTO;
import com.kbopark.operation.thirdplatform.service.ProductApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author:sunwenzhi
 * @Description:青小岛产品相关接口
 * @Date: 2020/10/26 15:37
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/productApi")
@Api(value = "productApi", tags = "【_青小岛-产品相关接口】")
public class ProductApiController {

	private final ProductApiService productApiService;

	/**
	 * 分页查询产品列表
	 *
	 * @param productListDTO
	 * @return
	 */
	@ApiOperation(value = "分页查询产品列表", notes = "分页查询产品列表")
	@GetMapping("/page")
	public R getProductPage(@Valid ProductListDTO productListDTO) {
		return productApiService.getProductList(productListDTO);
	}


	/**
	 * 查询产品详情
	 * @param productDetailDTO
	 * @return
	 */
	@ApiOperation(value = "查询产品详情", notes = "查询产品详情")
	@GetMapping("/detail")
	public R getProductDetail(@Valid ProductDetailDTO productDetailDTO) {
		return productApiService.getProductDetail(productDetailDTO);
	}


	/**
	 * 查询产品价格日历
	 * @param productSpecDTO
	 * @return
	 */
	@ApiOperation(value = "查询产品价格日历", notes = "查询产品价格日历")
	@GetMapping("/spec")
	public R getProductSpec(@Valid ProductSpecDTO productSpecDTO) {
		return productApiService.getProductSpec(productSpecDTO);
	}


	/**
	 * 批量查询产品状态
	 * @param productStatusDTO
	 * @return
	 */
	@ApiOperation(value = "批量查询产品状态", notes = "批量查询产品状态")
	@GetMapping("/status")
	public R getProductStatus(@Valid ProductStatusDTO productStatusDTO) {
		return productApiService.getProductStatus(productStatusDTO);
	}

}
