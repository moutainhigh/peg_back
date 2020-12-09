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
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.dto.ConsumerStrategySearchParam;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.ConsumerStrategyPrize;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.entity.MerchantConsumerStrategy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

/**
 * 商家消费策论
 *
 * @author laomst
 * @date 2020-09-02 09:43:14
 */
public interface MerchantConsumerStrategyService extends KbBaseService<MerchantConsumerStrategy> {

	/**
	 * 自定义分页查询
	 * @param page
	 * @param param
	 * @return
	 */
	IPage<MerchantConsumerStrategy> selectStrategyPage(IPage<MerchantConsumerStrategy> page, ConsumerStrategySearchParam param);

	/**
	 * 启用或者禁用
	 * @param id
	 * @param user
	 * @return
	 */
	Boolean upOrDown(Integer id, KboparkUser user);

	/**
	 * 修改
	 * @param entity
	 * @return
	 */
	@Transactional
	boolean updateStrategy(MerchantConsumerStrategy entity);

	/**
	 * 保存
	 * @param merchantConsumerStrategy
	 * @return
	 */
	boolean saveStrategy(MerchantConsumerStrategy merchantConsumerStrategy);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	Boolean removeConsumerStrategy(Integer id);


	/**
	 * 商家消费策略赠送优惠券或者红包
	 * @param byOrderNumber
	 * @return
	 */
	List<ConsumerStrategyPrize> notifyGiveCoupon(ConsumerOrder byOrderNumber);

	/****************************************** 模板工具方法 ***********************************************/
	default Boolean update(Consumer<UpdateWrapper<MerchantConsumerStrategy>> wrapperBuilder) {
		UpdateWrapper<MerchantConsumerStrategy> query = Wrappers.update();
		if (null != wrapperBuilder) {
			wrapperBuilder.accept(query);
		}
		return update(query);
	}


}
