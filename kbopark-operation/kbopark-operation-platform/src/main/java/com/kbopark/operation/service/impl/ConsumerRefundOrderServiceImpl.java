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
package com.kbopark.operation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qdzhhl.kbopark.common.core.constant.CommonConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.kbpay.dto.RefundDTO;
import com.kbopark.kbpay.feign.RemotePayService;
import com.kbopark.operation.apidto.ConsumerRefundOrderDTO;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.ConsumerRefundOrder;
import com.kbopark.operation.enums.OrderStatusEnum;
import com.kbopark.operation.enums.ProcessStateEnum;
import com.kbopark.operation.mapper.ConsumerOrderMapper;
import com.kbopark.operation.mapper.ConsumerRefundOrderMapper;
import com.kbopark.operation.service.ConsumerRefundOrderService;
import com.kbopark.operation.util.OrderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户退款申请
 *
 * @author pigx code generator
 * @date 2020-10-28 14:36:17
 */
@Service
@Slf4j
@AllArgsConstructor
public class ConsumerRefundOrderServiceImpl extends ServiceImpl<ConsumerRefundOrderMapper, ConsumerRefundOrder> implements ConsumerRefundOrderService {

	private final ConsumerOrderMapper consumerOrderMapper;

	private final RemotePayService remotePayService;


	@Override
	public ConsumerRefundOrder findByOrderNumber(String orderNumber) {
		QueryWrapper<ConsumerRefundOrder> query = new QueryWrapper<>();
		query.lambda().eq(ConsumerRefundOrder::getOrderNumber, orderNumber);
		return getOne(query, false);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R handleRefundRecord(ConsumerRefundOrderDTO refundOrderDTO) {

		String orderNumber = refundOrderDTO.getOrderNumber();

		ConsumerRefundOrder refundOrder = findByOrderNumber(orderNumber);
		if(refundOrder != null){
			return R.failed("该订单已申请退款，请勿重复提交");
		}
		QueryWrapper<ConsumerOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ConsumerOrder::getOrderNumber, orderNumber);
		List<ConsumerOrder> consumerOrders = consumerOrderMapper.selectList(queryWrapper);
		if(CollectionUtils.isEmpty(consumerOrders)){
			return R.failed("订单号不存在");
		}

		ConsumerOrder consumerOrder = consumerOrders.get(0);

		//如果前端传入退货数量不为空,而且大于购买数量
		if (refundOrderDTO.getNum() > 0 && refundOrderDTO.getNum() > consumerOrder.getBuyNum()) {
			 return R.failed("不能退大于购买数量商品.");
		}

		if(!OrderStatusEnum.SUCCESS.getCode().equals(consumerOrder.getNotifyStatus())){
			return R.failed("该订单未支付，不能申请退款");
		}
		if(refundOrderDTO.getMoney() != null && refundOrderDTO.getMoney() > consumerOrder.getMoney()){
			return R.failed("申请退款金额不能大于订单支付金额");
		}

		//更新订单状态
		consumerOrder.setRefundStatus(OrderStatusEnum.REFUNDING.getCode());
		consumerOrderMapper.updateById(consumerOrder);

		ConsumerRefundOrder apply = BeanUtil.copyProperties(refundOrderDTO, ConsumerRefundOrder.class);
		KboparkUser user = SecurityUtils.getUser();
		apply.setCreateBy(user.getRealName());
		apply.setCreateTime(LocalDateTime.now());
		apply.setProcessState(ProcessStateEnum.APPLY.getCode());

		//如果有传入数量，则拿出消费订单中的单价做退款金额
		apply.setMoney(refundOrderDTO.getMoney() == null ? OrderUtil.mul(consumerOrder.getProductPrice() * refundOrder.getNum(),2) : refundOrderDTO.getMoney());
		apply.setNum(refundOrderDTO.getNum() == null ? consumerOrder.getNum() : refundOrderDTO.getNum());
		save(apply);
		return R.ok(apply);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public R updateProcessState(ConsumerRefundOrder consumerRefundOrder) {
		QueryWrapper<ConsumerOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ConsumerOrder::getOrderNumber, consumerRefundOrder.getOrderNumber());
		List<ConsumerOrder> consumerOrders = consumerOrderMapper.selectList(queryWrapper);
		if(CollectionUtils.isEmpty(consumerOrders)){
			return R.failed("订单号不存在");
		}
		ConsumerOrder consumerOrder = consumerOrders.get(0);
		if(ProcessStateEnum.FAIL.getCode().equals(consumerRefundOrder.getProcessState())){
			consumerOrder.setRefundStatus(OrderStatusEnum.REFUSED.getCode());
		}
		if(ProcessStateEnum.REFUNDED.getCode().equals(consumerRefundOrder.getProcessState())){
			//审核通过后更新状态的同时发起退款
			consumerOrder.setNotifyStatus(OrderStatusEnum.REFUNDED.getCode());
			consumerOrder.setRefundStatus(OrderStatusEnum.REFUNDED.getCode());

			//创建退款参数DTO，并用于调用远程退款服务`
			RefundDTO refundDTO = new RefundDTO();
			refundDTO.setTradeBizNumber(consumerRefundOrder.getOrderNumber());

			//获取订单中的单价  *  用户退款单中数量  并保留两位小数
			Double totalPrice = OrderUtil.mul(consumerOrder.getProductPrice() * consumerRefundOrder.getNum(),2);
			refundDTO.setTotalPrice(totalPrice);

			R r = remotePayService.refundCall(refundDTO);
			log.info(">>>远程调用支付服务--发起退款响应："+ JSONObject.toJSONString(r));
			if(r.getCode() == CommonConstants.FAIL){
				throw new UnsupportedOperationException(r.getMsg());
			}
		}
		consumerOrderMapper.updateById(consumerOrder);

		consumerRefundOrder.setUpdateTime(LocalDateTime.now());
		consumerRefundOrder.setProcessTime(LocalDateTime.now());

		return R.ok(updateById(consumerRefundOrder));
	}
}
