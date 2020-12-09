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
import cn.qdzhhl.kbopark.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.apidto.ReviewLog;
import com.kbopark.operation.entity.MerchantReviewLog;
import com.kbopark.operation.mapper.MerchantReviewLogMapper;
import com.kbopark.operation.service.MerchantReviewLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商家审核记录
 *
 * @author laomst
 * @date 2020-08-28 23:33:55
 */
@Service
public class MerchantReviewLogServiceImpl extends ServiceImpl<MerchantReviewLogMapper, MerchantReviewLog> implements MerchantReviewLogService {

	@Override
	public MerchantReviewLog getLastOfMerchant(Integer merchantId) {
		return getOne(w -> {
			w.lambda().eq(MerchantReviewLog::getMerchantId, merchantId)
					.eq(MerchantReviewLog::getFinish, false)
					.last("limit 1");
		});
	}

	@Override
	public List<ReviewLog> getReviewProgress(Integer merchantId) {
		MerchantReviewLog log = getOne( w -> w.lambda().eq(MerchantReviewLog::getMerchantId, merchantId)
				.orderByDesc(MerchantReviewLog::getId)
				.last("limit 1"));
		ReviewLog start = new ReviewLog();
		ReviewLog end = new ReviewLog();
		if (null == log) {
			start.setTime(LocalDateTime.now());
			start.setStatusName("未提交审核");
			start.setShowRemark(false);
			return CollectionUtil.newArrayList(start);
		}
		start.setTime(log.getSubmitTime());
		start.setShowRemark(false);
		start.setOperatorName(log.getSubmitUserName());
		start.setStatusName("提交审核");
		if (log.getFinish()) {
			String reviewerName;
			Boolean isPass;
			LocalDateTime reviewTime;
			String remark;
			// 如果运营商没有审核，那么审核结果就是渠道商的审核结果，备注就是渠道商的审核备注
			if (null == log.getOperatorReviewerId()) {
				isPass = log.getDistributorPass();
				reviewerName = log.getDistributorReviewerName();
				reviewTime = log.getDistributorReviewTime();
				remark = log.getDistributorRemark();
			} else {
				isPass = log.getOperatorPass();
				reviewerName = log.getOperatorReviewerName();
				reviewTime = log.getOperatorReviewTime();
				remark = log.getOperatorRemark();
			}
			end.setStatusName(isPass ? "已通过" : "已驳回");
			end.setOperatorName(reviewerName);
			end.setIsFail(!isPass);
			end.setRemark(remark);
			end.setTime(reviewTime);
			end.setShowRemark(true);
		} else {
			end.setStatusName("审核中");
			end.setIsFail(false);
			end.setShowRemark(true);
			end.setRemark("正在审核中，请耐心等待...");
		}
		return CollectionUtil.newArrayList(end, start);
	}
}
