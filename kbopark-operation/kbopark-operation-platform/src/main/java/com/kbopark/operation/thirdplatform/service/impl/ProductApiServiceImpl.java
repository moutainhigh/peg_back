package com.kbopark.operation.thirdplatform.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.XML;
import cn.qdzhhl.kbopark.common.core.util.R;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.thirdplatform.config.ApiConstants;
import com.kbopark.operation.thirdplatform.dto.ProductDetailDTO;
import com.kbopark.operation.thirdplatform.dto.ProductListDTO;
import com.kbopark.operation.thirdplatform.dto.ProductSpecDTO;
import com.kbopark.operation.thirdplatform.dto.ProductStatusDTO;
import com.kbopark.operation.thirdplatform.entity.*;
import com.kbopark.operation.thirdplatform.enums.ResponseStatusEnum;
import com.kbopark.operation.thirdplatform.service.ProductApiService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/26 14:13
 **/
@Service
@Slf4j
public class ProductApiServiceImpl implements ProductApiService {


	@Override
	public R getProductList(ProductListDTO productListDTO) {
		try {
			productListDTO.setCustId(ApiConstants.CUST_ID);
			productListDTO.setApikey(ApiConstants.API_KEY);
			int pageNo = productListDTO.getPageNo() <= 0 ? 1 : productListDTO.getPageNo();
			productListDTO.setPageNo(pageNo);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(productListDTO), HashMap.class);
			log.info(">>>请求产品列表参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.PRODUCT_LIST_URL, hashMap);
			log.info(">>>请求产品列表响应：" + result);
			XStream xStream = new XStream(new Xpp3Driver(new NoNameCoder()));
			xStream.alias("result", ProductListResult.class);
			xStream.alias("product", ProductInfo.class);
			XStream.setupDefaultSecurity(xStream);
			xStream.allowTypes(new Class[]{ProductListResult.class,ProductInfo.class});
			ProductListResult productResult = (ProductListResult) xStream.fromXML(result);
			if(ResponseStatusEnum.SUCCESS.getCode().equals(productResult.getStatus())){
				Page<ProductInfo> page = new Page<>();
				page.setRecords(productResult.getProducts());
				page.setCurrent(productListDTO.getPageNo() == null ? 1 : productListDTO.getPageNo());
				page.setSize(productListDTO.getPageNum() == null ? 50 : productListDTO.getPageNum());
				page.setTotal(productResult.getTotalNum());
				return R.ok(page);
			}else{
				return R.failed(productResult.getMsg());
			}
		}catch (Exception e){
			e.printStackTrace();
			return R.failed("未查询到产品信息");
		}

	}

	@Override
	public R getProductDetail(ProductDetailDTO productDetailDTO) {
		try {
			productDetailDTO.setCustId(ApiConstants.CUST_ID);
			productDetailDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(productDetailDTO), HashMap.class);
			log.info(">>>请求产品详情参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.PRODUCT_DETAIL_URL, hashMap);
			log.info(">>>请求产品详情响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		}catch (Exception e){
			e.printStackTrace();
			return R.failed("未查询到产品详情信息");
		}
	}


	@Override
	public R getProductStatus(ProductStatusDTO productStatusDTO) {
		try {
			productStatusDTO.setCustId(ApiConstants.CUST_ID);
			productStatusDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(productStatusDTO), HashMap.class);
			log.info(">>>请求批量获取产品状态参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.PRODUCT_STATUS_LIST_URL, hashMap);
			log.info(">>>请求批量获取产品状态响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		}catch (Exception e){
			e.printStackTrace();
			return R.failed("未查询到产品状态信息");
		}
	}


	@Override
	public R getProductSpec(ProductSpecDTO productSpecDTO) {
		try {
			productSpecDTO.setCustId(ApiConstants.CUST_ID);
			productSpecDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(productSpecDTO), HashMap.class);
			log.info(">>>请求产品价格日历参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.PRODUCT_SPEC_URL, hashMap);
			log.info(">>>请求产品价格日历响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		}catch (Exception e){
			e.printStackTrace();
			return R.failed("未查询到产品价格信息");
		}
	}
}
