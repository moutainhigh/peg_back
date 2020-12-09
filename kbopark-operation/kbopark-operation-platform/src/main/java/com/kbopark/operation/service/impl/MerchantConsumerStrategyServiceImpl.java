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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.dto.ConsumerStrategySearchParam;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.ConsumerStrategyPrize;
import com.kbopark.operation.entity.MerchantConsumerStrategy;
import com.kbopark.operation.enums.MerchantStatusEnum;
import com.kbopark.operation.mapper.MerchantConsumerStrategyMapper;
import com.kbopark.operation.service.ConsumerStrategyPrizeService;
import com.kbopark.operation.service.MerchantConsumerStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 商家消费策论
 *
 * @author laomst
 * @date 2020-09-02 09:43:14
 */
@Service
public class MerchantConsumerStrategyServiceImpl extends ServiceImpl<MerchantConsumerStrategyMapper, MerchantConsumerStrategy> implements MerchantConsumerStrategyService {

	@Autowired
	private ConsumerStrategyPrizeService strategyPrizeService;

	@Override
	public IPage<MerchantConsumerStrategy> selectStrategyPage(IPage<MerchantConsumerStrategy> page, ConsumerStrategySearchParam param) {
		List<MerchantConsumerStrategy> resList = baseMapper.selectStrategyPage(page, param);
		resList.forEach(item -> {
			item.setPrizeList(strategyPrizeService.list(w -> w.lambda().eq(ConsumerStrategyPrize::getStrategyId, item.getId())));
		});
		return page.setRecords(resList);
	}

	@Override
	public Boolean upOrDown(Integer id, KboparkUser user) {
		if (null == user) {
			throw new UnsupportedOperationException("获取用户信息失败");
		}
		MerchantConsumerStrategy entity = getById(id);
		// 如果以前是启用状态， 那么现在的操作就是禁用
		if (ObjectUtil.equal(entity.getStatus(), MerchantStatusEnum.UP.code)) {
			entity.setStatus(MerchantStatusEnum.DOWN.code);
		} else {
			entity.setStatus(MerchantStatusEnum.UP.code);
		}
		return updateById(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateStrategy(MerchantConsumerStrategy entity) {
		entity.completePrize();
		updateById(entity);
		return updatePrize(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveStrategy(MerchantConsumerStrategy entity) {
		entity.completePrize();
		save(entity);
		return updatePrize(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeConsumerStrategy(Integer id) {
		strategyPrizeService.remove(w -> w.lambda().eq(ConsumerStrategyPrize::getStrategyId, id));
		return removeById(id);
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean updatePrize(MerchantConsumerStrategy entity) {
		List<ConsumerStrategyPrize> couponList = entity.getCouponList();
		List<ConsumerStrategyPrize> redpackList = entity.getRedpackList();
		if (CollectionUtil.isEmpty(couponList)) {
			couponList = new ArrayList<>();
		}
		if (CollectionUtil.isNotEmpty(redpackList)) {
			couponList.addAll(redpackList);
		}
		couponList.forEach(item -> {
			item.setId(null);
			item.setStrategyId(entity.getId());
		});
		strategyPrizeService.remove(w -> w.lambda().eq(ConsumerStrategyPrize::getStrategyId, entity.getId()));
		return strategyPrizeService.saveBatch(couponList);
	}

	@Override
	public List<ConsumerStrategyPrize> notifyGiveCoupon(ConsumerOrder byOrderNumber) {
		QueryWrapper<MerchantConsumerStrategy> query = new QueryWrapper<>();
		query.lambda().eq(MerchantConsumerStrategy::getMerchantId, byOrderNumber.getMerchantId());
		List<MerchantConsumerStrategy> list = this.list(query);
		if(CollectionUtil.isNotEmpty(list)){
			Collections.sort(list);
			Double money = byOrderNumber.getMoney();
			MerchantConsumerStrategy consumerStrategy = null;
			for (int i = 0; i < list.size(); i++) {
				if(money >= list.get(i).getMoney()){
					consumerStrategy = list.get(i);
					break;
				}
			}
			if(consumerStrategy != null){
				QueryWrapper<ConsumerStrategyPrize> queryPrize = new QueryWrapper<>();
				queryPrize.lambda().eq(ConsumerStrategyPrize::getStrategyId, consumerStrategy.getId());
				List<ConsumerStrategyPrize> prizeList = strategyPrizeService.list(queryPrize);
				return prizeList;
			}

		}

		return null;
	}
}
