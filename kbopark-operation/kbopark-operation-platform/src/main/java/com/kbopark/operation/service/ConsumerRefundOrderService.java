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
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.apidto.ConsumerRefundOrderDTO;
import com.kbopark.operation.entity.ConsumerRefundOrder;

/**
 * 用户退款申请
 *
 * @author pigx code generator
 * @date 2020-10-28 14:36:17
 */
public interface ConsumerRefundOrderService extends IService<ConsumerRefundOrder> {

	/**
	 * 根据订单号查询退款订单
	 *
	 * @param orderNumber
	 * @return
	 */
	ConsumerRefundOrder findByOrderNumber(String orderNumber);

	/**
	 * 处理退款申请
	 * @param consumerRefundOrderDTO
	 * @return
	 */
	R handleRefundRecord(ConsumerRefundOrderDTO consumerRefundOrderDTO);

	/***
	 * 审核更新退款订单
	 * @param consumerRefundOrder
	 * @return
	 */
	R updateProcessState(ConsumerRefundOrder consumerRefundOrder);

}
