package com.kbopark.operation.api.service;

import cn.qdzhhl.kbopark.common.core.util.R;
import com.kbopark.operation.dto.CouponReceiveDTO;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.CouponInfo;

import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/19 10:11
 **/
public interface ConsumerApiService {

	/**
	 * 支付成功后根据订单号获取商家赠送的优惠券列表
	 *
	 * @param consumerOrder
	 * @return
	 */
	List<CouponInfo> getWelfareCouponByOrder(ConsumerOrder consumerOrder);


	/**
	 * 支付成功后领取优惠券方法
	 * @param couponReceiveDTO
	 * @return
	 */
	R handlerReceive(CouponReceiveDTO couponReceiveDTO);

}
