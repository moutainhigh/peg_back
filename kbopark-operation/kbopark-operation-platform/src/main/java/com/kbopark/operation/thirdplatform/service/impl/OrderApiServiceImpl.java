package com.kbopark.operation.thirdplatform.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.XML;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.kbopark.operation.apidto.MemberOrderDTO;
import com.kbopark.operation.entity.*;
import com.kbopark.operation.enums.CondTypeEnum;
import com.kbopark.operation.enums.PackageTypeEnum;
import com.kbopark.operation.enums.SingleTypeEnum;
import com.kbopark.operation.enums.TreeIdEnum;
import com.kbopark.operation.thirdplatform.config.ApiConstants;
import com.kbopark.operation.thirdplatform.dto.*;
import com.kbopark.operation.thirdplatform.entity.*;
import com.kbopark.operation.thirdplatform.service.OrderApiService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 13:59
 **/
@Service
@Slf4j
public class OrderApiServiceImpl implements OrderApiService {

	private String link_man = "link_man";
	private String link_phone = "link_phone";
	private String link_credit_no = "link_credit_no";
	private String link_email = "link_email";
	private String link_address = "link_address";


	@Override
	public OrderInfo handleOrderInfo(MemberOrderDTO memberOrderDTO, String orderNumber,MemberAddress memberAddress) {

		//获取到当前登录的会员信息
		KboparkUser user = SecurityUtils.getUser();

		OrderInfo orderInfo = new OrderInfo();
		//游玩日期
		orderInfo.setTravel_date(memberOrderDTO.getTravelTime());
		//产品编号
		orderInfo.setInfo_id(memberOrderDTO.getProductNo());
		//分销商账号
		orderInfo.setCust_id(ApiConstants.CUST_ID);
		//订单号
		orderInfo.setOrder_source_id(orderNumber);
		//预定数量
		orderInfo.setNum(memberOrderDTO.getBuyNum());

		// link_address、linkCode、link_credit_type、link_credit_no 是否填写,参见产品详情接 口返回的”custField”参数的内容而定。
		// link_man,link_phone,link_credit_no,link_email,link_address
		// link_man:姓名 link_phone:手机号
		// link_credit_no:证件号 link_email:邮箱地址
		// link_address:邮寄地址 (要求快递门票 时此项为必填项)
		List<String> fields = Lists.newArrayList();
		if(StringUtils.isNotBlank(memberOrderDTO.getCustField())){
			fields = Arrays.asList(memberOrderDTO.getCustField().split(","));
		}

		//如果地址不为空
		if (memberAddress != null && memberAddress.getId() != null) {

			//此时商品必须填写地址
			if(fields.contains(link_address)){
				orderInfo.setLink_address(memberAddress.getTakeAddress() + memberAddress.getTakeAddressDetail());
			}

			//收货人名称
			if(fields.contains(link_man)){
				orderInfo.setLink_man(memberAddress.getTakeName());
			}

			//收货人手机号
			if(fields.contains(link_phone)){
				orderInfo.setLink_phone(memberAddress.getTakePhone());
			}
		} else {
			//收货人名称
			if(fields.contains(link_man)){
				orderInfo.setLink_man(user.getRealName());
			}

			//收货人手机号
			if(fields.contains(link_phone)){
				orderInfo.setLink_phone(user.getPhone());
			}
		}

		if(fields.contains(link_credit_no)){
			orderInfo.setLink_credit_no(memberOrderDTO.getLinkCreditNo());
		}
		if(fields.contains(link_email)){
			orderInfo.setLink_email(memberOrderDTO.getLinkEmail());
		}

		//增值项目
		boolean isCond = CondTypeEnum.YES.getCode().equals(memberOrderDTO.getIsConds());
		if(isCond){
			orderInfo.setConds(memberOrderDTO.getConds());
		}


		//<peoples/>节点的填写需要参见产品详情接口返回的”isSingle”参数的内容而定,
		// 若返回 值为”1”,则购买多少张票需要填写多少个客人信息
		if(SingleTypeEnum.YES.getCode().equals(memberOrderDTO.getIsSingle())){
			orderInfo.setPeoples(memberOrderDTO.getPeoples());
		}

		//Param 参数打包产品子产品节点<prods><prod>.....</prod></prods>
		//打包产品专属节点，注意（只有 treeId=1 并且 isPackage=1 时必填）
		boolean isPackage = TreeIdEnum.TICKET_PACKAGE.getCode().equals(memberOrderDTO.getTreeId())
				&& PackageTypeEnum.YES.getCode().equals(memberOrderDTO.getIsPackage());
		if(isPackage){
			orderInfo.setProds(memberOrderDTO.getProds());
		}

		//tree_id=11 和 tree_id=12 的实体商品规格节点<cond>11<cond>需要传商品规格价格ID，
		// 参见产品价格日历接口的“spec_id”。
		boolean isSpec = TreeIdEnum.PHYSICAL_PRODUCT.getCode().equals(memberOrderDTO.getTreeId()) && TreeIdEnum.PRE_SELL.getCode().equals(memberOrderDTO.getTreeId());
		if(isSpec){
			orderInfo.setDetails(memberOrderDTO.getDetails());
		}

		return orderInfo;
	}

	@Override
	public R checkOrderInfo(MemberOrderDTO memberOrderDTO) {

		List<String> fields = new ArrayList<>();

		if(StringUtils.isNotBlank(memberOrderDTO.getCustField())){
			fields = Arrays.asList(memberOrderDTO.getCustField().split(","));
		}

		if(fields.contains(link_credit_no) && StringUtils.isBlank(memberOrderDTO.getLinkCreditNo())){
			return R.failed("请设置证件号");
		}

		if(fields.contains(link_email) && StringUtils.isBlank(memberOrderDTO.getLinkEmail())){
			return R.failed("请设置邮箱");
		}

		if(SingleTypeEnum.YES.getCode().equals(memberOrderDTO.getIsSingle()) && CollectionUtils.isEmpty(memberOrderDTO.getPeoples())){
			return R.failed("请设置其他联系人");
		}

		boolean isPackage = TreeIdEnum.TICKET_PACKAGE.getCode().equals(memberOrderDTO.getTreeId()) && PackageTypeEnum.YES.getCode().equals(memberOrderDTO.getIsPackage());

		boolean packageParam = CollectionUtils.isEmpty(memberOrderDTO.getProds());

		if(isPackage && packageParam){
			return R.failed("打包产品需要设置子产品信息");
		}

		boolean isSpec = TreeIdEnum.PHYSICAL_PRODUCT.getCode().equals(memberOrderDTO.getTreeId()) && TreeIdEnum.PRE_SELL.getCode().equals(memberOrderDTO.getTreeId());

		boolean specParam = CollectionUtils.isEmpty(memberOrderDTO.getDetails());

		if(isSpec && specParam){
			return R.failed("多规格产品需要设置规格信息");
		}

		boolean isCond = CondTypeEnum.YES.getCode().equals(memberOrderDTO.getIsConds());

		boolean condParam = CollectionUtils.isEmpty(memberOrderDTO.getConds());

		if(isCond && condParam){
			return R.failed("请设置增值项目信息");
		}

		return R.ok(true);
	}

	@Override
	public R saveOrder(OrderInfo orderInfo) {
		String title = "订单保存";
		try {
			if(orderInfo == null){
				log.info(">>>"+ title + "订单信息不能为");
				return R.failed("订单信息不能为空");
			}
			XStream xStream = new XStream(new Xpp3Driver(new NoNameCoder()));
			xStream.alias("order", OrderInfo.class);
			xStream.alias("people", People.class);
			xStream.alias("cond", CondValue.class);
			xStream.alias("prod", Prod.class);
			xStream.alias("detail", Detail.class);
			XStream.setupDefaultSecurity(xStream);
			xStream.allowTypes(new Class[]{OrderInfo.class, People.class, CondValue.class, Prod.class, Detail.class});
			String param = xStream.toXML(orderInfo);
			OrderSaveDTO  orderSaveDTO = new OrderSaveDTO();
			orderSaveDTO.setCustId(ApiConstants.CUST_ID);
			orderSaveDTO.setApikey(ApiConstants.API_KEY);
			orderSaveDTO.setParam(param);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(orderSaveDTO), HashMap.class);
			log.info(">>>请求" + title + "参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.ORDER_SAVE_URL, hashMap);
			log.info(">>>请求" + title + "响应：" + result);
			return R.ok(XML.toJSONObject(result));
		} catch (Exception e) {
			e.printStackTrace();
			return R.failed("订单保存失败");
		}
	}

	@Override
	public R getOrderDetail(OrderDetailDTO orderDetailDTO) {
		String title = "获取订单详情";
		try {
			orderDetailDTO.setCustId(ApiConstants.CUST_ID);
			orderDetailDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(orderDetailDTO), HashMap.class);
			log.info(">>>请求" + title + "参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.ORDER_DETAIL_URL, hashMap);
			log.info(">>>请求" + title + "响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		} catch (Exception e) {
			e.printStackTrace();
			return R.failed(title + "无数据");
		}
	}

	@Override
	public R getOrderList(OrderListDTO orderListDTO) {
		String title = "获取订单列表";
		try {
			orderListDTO.setCustId(ApiConstants.CUST_ID);
			orderListDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(orderListDTO), HashMap.class);
			log.info(">>>请求" + title + "参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.ORDER_LIST_URL, hashMap);
			log.info(">>>请求" + title + "响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		} catch (Exception e) {
			e.printStackTrace();
			return R.failed(title + "无数据");
		}
	}

	@Override
	public R notifySuccess(OrderPayDTO orderPayDTO) {
		String title = "订单支付";
		try {
			orderPayDTO.setCustId(ApiConstants.CUST_ID);
			orderPayDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(orderPayDTO), HashMap.class);
			log.info(">>>请求" + title + "参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.ORDER_PAY_SUCCESS_URL, hashMap);
			log.info(">>>请求" + title + "响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		} catch (Exception e) {
			e.printStackTrace();
			return R.failed(title + "无数据");
		}
	}

	@Override
	public R cancelOrder(OrderCancelDTO orderCancelDTO) {
		String title = "订单取消";
		try {
			orderCancelDTO.setCustId(ApiConstants.CUST_ID);
			orderCancelDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(orderCancelDTO), HashMap.class);
			log.info(">>>请求" + title + "参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.ORDER_CANCEL_URL, hashMap);
			log.info(">>>请求" + title + "响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		} catch (Exception e) {
			e.printStackTrace();
			return R.failed(title + "无数据");
		}
	}

	@Override
	public R cancelOrderPart(OrderCancelPartDTO orderCancelPartDTO) {
		String title = "订单部分取消";
		try {
			orderCancelPartDTO.setCustId(ApiConstants.CUST_ID);
			orderCancelPartDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(orderCancelPartDTO), HashMap.class);
			log.info(">>>请求" + title + "参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.ORDER_CANCEL_PART_URL, hashMap);
			log.info(">>>请求" + title + "响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		} catch (Exception e) {
			e.printStackTrace();
			return R.failed(title + "无数据");
		}
	}

	@Override
	public R changeOrder(OrderChangeDTO orderChangeDTO) {
		String title = "订单退改";
		try {
			orderChangeDTO.setCustId(ApiConstants.CUST_ID);
			orderChangeDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(orderChangeDTO), HashMap.class);
			log.info(">>>请求" + title + "参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.ORDER_CHANGE_URL, hashMap);
			log.info(">>>请求" + title + "响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		} catch (Exception e) {
			e.printStackTrace();
			return R.failed(title + "无数据");
		}
	}

	@Override
	public R getOrderListStatus(OrderStatusDTO orderStatusDTO) {
		String title = "批量获取订单状态";
		try {
			orderStatusDTO.setCustId(ApiConstants.CUST_ID);
			orderStatusDTO.setApikey(ApiConstants.API_KEY);
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(orderStatusDTO), HashMap.class);
			log.info(">>>请求" + title + "参数：" + hashMap);
			String result = HttpUtil.get(ApiConstants.ORDER_CHANGE_URL, hashMap);
			log.info(">>>请求" + title + "响应：" + result);
			//目前问题是第三方文档字段不全，所以直接转为json传递
			return R.ok(XML.toJSONObject(result));
		} catch (Exception e) {
			e.printStackTrace();
			return R.failed(title + "无数据");
		}
	}
}
