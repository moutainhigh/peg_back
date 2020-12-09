package com.kbopark.operation.thirdplatform.service;

import cn.qdzhhl.kbopark.common.core.util.R;
import com.kbopark.operation.apidto.MemberOrderDTO;
import com.kbopark.operation.entity.MemberAddress;
import com.kbopark.operation.thirdplatform.dto.*;
import com.kbopark.operation.thirdplatform.entity.OrderInfo;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 13:58
 **/
public interface OrderApiService {

	/**
	 * 1.处理生成待推送订单信息
	 *
	 * @param memberOrderDTO	下单信息
	 * @param orderNumber		订单号
	 * @param memberAddress		收货地址
	 * @return
	 */
	OrderInfo handleOrderInfo(MemberOrderDTO memberOrderDTO, String orderNumber, MemberAddress memberAddress);

	/**
	 * 2.校验下单信息
	 *
	 * @param memberOrderDTO
	 * @return
	 */
	R checkOrderInfo(MemberOrderDTO memberOrderDTO);

	/**
	 * 3.保存青小岛订单
	 *
	 * @param orderInfo
	 * @return
	 */
	R saveOrder(OrderInfo orderInfo);

	/***
	 * 4.获取青小岛订单详情
	 * @param orderDetailDTO
	 * @return
	 */
	R getOrderDetail(OrderDetailDTO orderDetailDTO);


	/**
	 * 5.获取青小岛订单列表
	 *
	 * @param orderListDTO
	 * @return
	 */
	R getOrderList(OrderListDTO orderListDTO);


	/**
	 * 6.订单支付，分销渠道在收取游客订单款以后，立即调用分销系统的支付接口，
	 * 分销系统按照结算价格 从分销渠道的预存款中扣除，并且立即调整订单的状态和后续操作。
	 *
	 * @param orderPayDTO
	 * @return
	 */
	R notifySuccess(OrderPayDTO orderPayDTO);


	/**
	 * 7.订单取消接口
	 *
	 * @param orderCancelDTO
	 * @return
	 */
	R cancelOrder(OrderCancelDTO orderCancelDTO);


	/**
	 * 8.订单部分取消
	 *
	 * @param orderCancelPartDTO
	 * @return
	 */
	R cancelOrderPart(OrderCancelPartDTO orderCancelPartDTO);


	/**
	 * 9.订单退改
	 *
	 * @param orderChangeDTO
	 * @return
	 */
	R changeOrder(OrderChangeDTO orderChangeDTO);


	/**
	 * 10.批量获取订单状态
	 *
	 * @param orderStatusDTO
	 * @return
	 */
	R getOrderListStatus(OrderStatusDTO orderStatusDTO);


}
