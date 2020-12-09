package com.kbopark.operation.api.service.impl;

import cn.qdzhhl.kbopark.common.core.util.R;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.kbopark.operation.api.service.ConsumerApiService;
import com.kbopark.operation.dto.CouponReceiveDTO;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.ConsumerStrategyPrize;
import com.kbopark.operation.entity.CouponInfo;
import com.kbopark.operation.entity.CouponReceive;
import com.kbopark.operation.service.*;
import com.kbopark.operation.util.OperationConstants;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/19 10:11
 **/
@Service
@AllArgsConstructor
public class ConsumerApiServiceImpl implements ConsumerApiService {

	private final ConsumerOrderService consumerOrderService;

	private final CouponInfoService couponInfoService;

	private final CouponReceiveService couponReceiveService;

	private final MerchantConsumerStrategyService consumerStrategyService;


	@Override
	public List<CouponInfo> getWelfareCouponByOrder(ConsumerOrder consumerOrder) {
		List<ConsumerStrategyPrize> prizeList = consumerStrategyService.notifyGiveCoupon(consumerOrder);
		List<Integer> ids = prizeList.stream().map(ConsumerStrategyPrize::getCouponId).distinct().collect(Collectors.toList());
		List<CouponInfo> couponWelfareList = couponInfoService.listByIds(ids);
		System.out.println(">>>>>商家赠送福利优惠券："+ JSONObject.toJSONString(couponWelfareList));
		List<CouponReceive> byMemberPhone = couponReceiveService.findByMemberPhone(consumerOrder.getMemberPhone());
		Map<String, String> collect = byMemberPhone.stream().collect(Collectors.toMap(CouponReceive::getCouponSerialNumber, CouponReceive::getCouponName));
		System.out.println(">>>>>当前用户领取记录："+ JSONObject.toJSONString(collect));
		List<CouponInfo> result = couponWelfareList.stream().filter(item -> StringUtils.isBlank(collect.get(item.getCouponSerialNumber()))).collect(Collectors.toList());
		return result;
	}


	@Override
	public R handlerReceive(CouponReceiveDTO couponReceiveDTO) {
		if(StringUtils.isBlank(couponReceiveDTO.getOrderNumber())){
			return R.failed("订单号不能为空");
		}
		String orderNumber = couponReceiveDTO.getOrderNumber().substring(4);
		ConsumerOrder byOrderNumber = consumerOrderService.findByOrderNumber(orderNumber);
		if(byOrderNumber == null){
			return R.failed(OperationConstants.NOTIFY_ORDER_NO_DATA);
		}
		List<CouponInfo> result = getWelfareCouponByOrder(byOrderNumber);
		List<JSONObject> receiveResult = new ArrayList<>();
		//一键领取全部优惠券
		if(couponReceiveDTO.getReceiveAll() != null && couponReceiveDTO.getReceiveAll()){
			if(CollectionUtils.isNotEmpty(result)){
				for (int i = 0; i < result.size(); i++) {
					CouponInfo couponInfo = result.get(i);
					CouponReceiveDTO receiveDTO = new CouponReceiveDTO();
					receiveDTO.setCouponSerialNumber(couponInfo.getCouponSerialNumber());
					receiveDTO.setPhone(byOrderNumber.getMemberPhone());
					receiveDTO.setNickName(byOrderNumber.getMemberName());
					receiveDTO.setImage(byOrderNumber.getMemberAvatar());
					receiveDTO.setOrderNumber(byOrderNumber.getOrderNumber());
					receiveDTO.setMerchantId(byOrderNumber.getMerchantId());
					R res = couponReceiveService.receiveAndCreateRecord(receiveDTO);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put(couponInfo.getCouponSerialNumber(), res.getCode());
					jsonObject.put("msg", res.getMsg());
					receiveResult.add(jsonObject);
				}
			}
		}else{
			//如果是领取某一个使用单个券编号couponSerialNumber
			CouponReceiveDTO receiveDTO = new CouponReceiveDTO();
			receiveDTO.setCouponSerialNumber(couponReceiveDTO.getCouponSerialNumber());
			receiveDTO.setPhone(byOrderNumber.getMemberPhone());
			receiveDTO.setNickName(byOrderNumber.getMemberName());
			receiveDTO.setImage(byOrderNumber.getMemberAvatar());
			receiveDTO.setOrderNumber(byOrderNumber.getOrderNumber());
			receiveDTO.setMerchantId(byOrderNumber.getMerchantId());
			R res = couponReceiveService.receiveAndCreateRecord(receiveDTO);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(couponReceiveDTO.getCouponSerialNumber(), res.getCode());
			jsonObject.put("msg", res.getMsg());
			receiveResult.add(jsonObject);
		}
		return R.ok(receiveResult);
	}
}
