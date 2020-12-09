/*
 *    Copyright (c) 2018-2025, kbopark All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: kbopark
 */

package com.kbopark.operation.service;

import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.data.mybatis.KbBaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.apidto.MemberOrderDTO;
import com.kbopark.operation.apidto.MonthNumberStatistics;
import com.kbopark.operation.dto.*;
import com.kbopark.operation.entity.ConsumerOrder;

import java.time.LocalDate;
import java.util.List;

/**
 * 消费订单
 *
 * @author pigx code generator
 * @date 2020-09-03 10:17:02
 */
public interface ConsumerOrderService extends KbBaseService<ConsumerOrder> {


	/**
	 * 根据ID查询订单信息
	 *
	 * @param id
	 * @return
	 */
	R getOrderById(Integer id);

	/**
	 * 分页查询订单信息
	 *
	 * @param page
	 * @param consumerOrderDTO
	 * @return
	 */
	R getOrderPage(Page page, ConsumerOrderDTO consumerOrderDTO);

	/**
	 * 根据订单号查询信息
	 *
	 * @param orderNumber
	 * @return
	 */
	ConsumerOrder findByOrderNumber(String orderNumber);

	/**
	 * 扫商家码直接下单
	 *
	 * @param createOrderDTO
	 * @return
	 */
	R<ConsumerOrder> createOrder(CreateOrderDTO createOrderDTO);


	/**
	 * 选择固定商品直接下单
	 *
	 * @param memberOrderDTO
	 * @param orderNumber
	 * @param thirdOrderNumber
	 * @return
	 */
	R<ConsumerOrder> createOrder(MemberOrderDTO memberOrderDTO, String orderNumber, String thirdOrderNumber);


	/**
	 * 异步通知更新状态
	 *
	 * @param orderNumber	订单号
	 * @return
	 */
	R notifyChangeStatus(String orderNumber);

	/**
	 * 用户取消支付
	 *
	 * @param orderNumber	订单号
	 * @param hasPrefix		是否有银联前缀
	 * @return
	 */
	R cancelPayOrder(String orderNumber, boolean hasPrefix);


	/**
	 * 检测当前用户是否存在未支付订单，存在则设置为关闭状态并还原使用的优惠券
	 * @return
	 */
	R checkPayOrder();

	/**
	 * 查询用户卡券分页列表
	 *
	 * @param page
	 * @param param
	 * @return
	 */
	IPage<MerchantPayOrderDTO> selectMerchantOrderPage(Page<MerchantPayOrderDTO> page, MerchantOrderSearchDTO param);

	/**
	 *
	 * @param merchantId 商家id
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 金额合计
	 */
	Double getMerchantIncomeMoney(Integer merchantId, LocalDate startDate, LocalDate endDate);

	/**
	 * 查询某个商家近n个月的订单数量
	 * @param merchantId
	 * @param monthNumber
	 * @return
	 */
	List<MonthNumberStatistics> lastMonthCountGroup(Integer merchantId, Integer monthNumber);

	/**
	 * 查询某个商家近n个月的收益金额
	 * @param merchantId
	 * @param monthNumber
	 * @return
	 */
	List<MonthNumberStatistics> lastMonthMoneyGroup(Integer merchantId, Integer monthNumber);

}
