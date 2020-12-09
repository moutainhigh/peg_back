package com.kbopark.operation.thirdplatform.service;

import cn.qdzhhl.kbopark.common.core.util.R;
import com.kbopark.operation.thirdplatform.dto.ProductDetailDTO;
import com.kbopark.operation.thirdplatform.dto.ProductListDTO;
import com.kbopark.operation.thirdplatform.dto.ProductSpecDTO;
import com.kbopark.operation.thirdplatform.dto.ProductStatusDTO;
import com.kbopark.operation.thirdplatform.entity.ProductInfo;

import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/26 14:13
 **/
public interface ProductApiService {

	/**
	 * 获取产品列表
	 * @param productListDTO
	 * @return
	 */
	R getProductList(ProductListDTO productListDTO);


	/**
	 * 获取产品详情
	 * @param productDetailDTO
	 * @return
	 */
	R getProductDetail(ProductDetailDTO productDetailDTO);


	/**
	 * 批量获取产品状态
	 * @param productStatusDTO
	 * @return
	 */
	R getProductStatus(ProductStatusDTO productStatusDTO);


	/**
	 * 获取商品价格日历，包括规格信息
	 * @param productSpecDTO
	 * @return
	 */
	R getProductSpec(ProductSpecDTO productSpecDTO);

}
