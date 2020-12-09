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

import cn.hutool.core.util.ObjectUtil;
import cn.qdzhhl.kbopark.admin.api.entity.SysDept;
import cn.qdzhhl.kbopark.admin.api.enums.DeptLevelEnum;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.admin.api.feign.RemoteDeptService;
import cn.qdzhhl.kbopark.common.core.constant.SecurityConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.dto.MerchantReviewParam;
import com.kbopark.operation.dto.MerchantSearchParam;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.entity.MerchantReviewLog;
import com.kbopark.operation.enums.MerchantBusinessStatusEnum;
import com.kbopark.operation.enums.MerchantReviewStatusEnum;
import com.kbopark.operation.enums.MerchantStatusEnum;
import com.kbopark.operation.mapper.MerchantMapper;
import com.kbopark.operation.service.MerchantBalanceService;
import com.kbopark.operation.service.MerchantReviewLogService;
import com.kbopark.operation.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 商家基本信息表
 *
 * @author laomst
 * @date 2020-08-25 17:59:54
 */
@Service
@AllArgsConstructor
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

	private final MerchantReviewLogService reviewLogService;

	private final RemoteDeptService remoteDeptService;

	private final MerchantBalanceService merchantBalanceService;

	@Override
	public IPage<Merchant> selectMerchantPage(IPage<Merchant> page, MerchantSearchParam param) {
		return page.setRecords(baseMapper.selectMerchantPage(page, param));
	}

	@Override
	@Transactional
	public Boolean merchantEnterSelf(Merchant entity) {
		if (null == entity) {
			throw new RuntimeException("获取商户信息失败");
		}
		R<Boolean> booleanR = entity.canEnterSelf();
		if (!booleanR.getData()) {
			throw new RuntimeException(booleanR.getMsg());
		}
		SysDept dept = remoteDeptService.remoteQueryId(entity.getOperatorId(), SecurityConstants.FROM_IN).getData();
		if (null == dept) {
			throw new RuntimeException("获取运营平台信息失败");
		}
		checkBusinessLicenseNumber(entity.getId(), entity.getBusinessLicenseNumber());
		// 设置自主入驻标志位
		entity.setIsSelfEnter(true);
		// 自主入驻的审核直接到运营平台
		entity.setReviewStatus(MerchantReviewStatusEnum.ADD_OPERATOR_UN_CHECKED.code);
		entity.setStatus(MerchantStatusEnum.UP.code);
		entity.setBusinessStatus(MerchantBusinessStatusEnum.UP.code);
		entity.setSubmitReviewTime(LocalDateTime.now());
		entity.setSubmitReviewUsername(entity.getLinkMan());
		// 如果是新增则需要设置一些初始状态
		saveOrUpdate(entity);
		if (!merchantBalanceService.init4Merchant(entity.getId())) {
			throw new RuntimeException("初始化商家账户信息失败");
		}
		// 保存审核提交记录
		return reviewLogService.save(new MerchantReviewLog(entity.getId(), -1, entity.getLinkMan()));
	}

	@Override
	@Transactional
	public Boolean saveMerchant(Merchant entity, KboparkUser user) {
		if (null == user) {
			throw new UnsupportedOperationException("获取用户信息失败");
		}
		SysDept dept = remoteDeptService.remoteQueryId(user.getDeptId(), SecurityConstants.FROM_IN).getData();
		if (null == dept) {
			throw new RuntimeException("获取运营平台信息失败");
		}
		if (!ObjectUtil.equal(dept.getDeptLevel(), DeptLevelEnum.THREE.code)) {
			throw new UnsupportedOperationException("只有渠道商中的相关人员才能够添加商家");
		}
		// 判断营业执照注册号是否可用
		checkBusinessLicenseNumber(entity.getId(), entity.getBusinessLicenseNumber());
		entity.setReviewStatus(MerchantReviewStatusEnum.ADD_DISTRIBUTOR_UN_CHECKED.code);
		entity.setStatus(MerchantStatusEnum.UP.code);
		entity.setBusinessStatus(MerchantBusinessStatusEnum.UP.code);
		entity.setSubmitReviewTime(LocalDateTime.now());
		entity.setSubmitReviewUsername(user.getRealName());
		// 如果是新增则需要设置一些初始状态
		if (entity.getId() == null) {
			if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Promoter.code)) {
				entity.setPromoterId(user.getId());
				entity.setPromoteCode(user.getPromoteCode());
			}
			entity.setDistributorId(user.getDeptId());
			entity.setOperatorId(dept.getParentId());
		}
		saveOrUpdate(entity);
		if (!merchantBalanceService.init4Merchant(entity.getId())) {
			throw new RuntimeException("初始化商家账户信息失败");
		}
		// 处理商家信息表
		// 保存审核提交记录
		return reviewLogService.save(new MerchantReviewLog(entity.getId(), user.getId(), user.getRealName()));
	}

	@Override
	public Boolean updateMerchant(Merchant entity, KboparkUser user) {
		if (entity.getIsChecking()) {
			throw new UnsupportedOperationException("当前商家正在审核，不能进行编辑");
		}
		// 如果是新增未审核通过的，就走新增的流程
		if (entity.getReviewStatus() < 50) {
			return saveMerchant(entity, user);
		}

		// 否则就是修改的流程，这里需要处理快照
		if (null == user) {
			throw new UnsupportedOperationException("获取用户信息失败");
		}
		SysDept dept = remoteDeptService.remoteQueryId(user.getDeptId(), SecurityConstants.FROM_IN).getData();
		if (null == dept) {
			throw new RuntimeException("获取用户平台信息失败");
		}
		Merchant oldEntity = getById(entity.getId());
		if (null == oldEntity) {
			throw new RuntimeException("系统中没有当前实体");
		}
		// 如果是运营平台进行修改，那么直接修改通过就可以了
		if (UserTypeEnum.Operation.code.equals(user.getUserType())) {
			entity.setId(oldEntity.getId());
			return updateById(entity);
		} else {
			// 编辑之后要等待渠道商审核
			entity.setReviewStatus(MerchantReviewStatusEnum.EDIT_DISTRIBUTOR_UN_CHECKED.code);
			oldEntity.setReviewStatus(entity.getReviewStatus());
			entity.setTodoSnapshoot(null);
			oldEntity.setTodoSnapshoot(JSON.toJSONString(entity));
			entity.setSubmitReviewTime(LocalDateTime.now());
			entity.setSubmitReviewUsername(user.getRealName());
			updateById(oldEntity);
			// 之前的所有记录设置为已经完成
			reviewLogService.update(w-> {
				w.lambda().eq(MerchantReviewLog::getMerchantId, entity.getId())
						.set(MerchantReviewLog::getFinish, Boolean.TRUE);
			});
			// 保存审核提交记录
			return reviewLogService.save(new MerchantReviewLog(entity.getId(), user.getId(), user.getRealName()));
		}
	}

	@Override
	public Boolean upOrDown(Integer id, KboparkUser user) {
		if (null == user) {
			throw new UnsupportedOperationException("获取用户信息失败");
		}
		SysDept dept = remoteDeptService.remoteQueryId(user.getDeptId(), SecurityConstants.FROM_IN).getData();
		if (null == dept) {
			throw new RuntimeException("获取用户平台信息失败");
		}
		Merchant entity = getById(id);
		// 如果以前是启用状态， 那么现在的操作就是禁用
		if (ObjectUtil.equal(entity.getStatus(), MerchantStatusEnum.UP.code)) {
			entity.setDownDeptLevel(dept.getDeptLevel());
			entity.setStatus(MerchantStatusEnum.DOWN.code);
		} else {
			// TODO 判断禁用的级别，如果级别不够就不能进行操作
			entity.setStatus(MerchantStatusEnum.UP.code);
		}
		return updateById(entity);
	}

	@Override
	@Transactional
	public Boolean review(MerchantReviewParam param, KboparkUser user) {
		if (null == user) {
			throw new UnsupportedOperationException("获取用户信息失败");
		}
		Merchant entity = getById(param.getMerchantId());
		if (null == entity) {
			throw new RuntimeException("系统中没有当前实体");
		}
		// 处理审核记录
		MerchantReviewLog log = reviewLogService.getLastOfMerchant(entity.getId());
		if (null != log) {
			log.changeReviewStatus(param, user.getUserType(), user.getId(), user.getRealName());
			reviewLogService.updateById(log);
		}
		// 审核
		entity.review(param, user.getUserType(), user.getRealName());
		return updateById(entity);
	}
	/**
	 * 查询某个商户号是否可用
	 * @param id
	 * @param businessLicenseNumber
	 * @return
	 */
	private void checkBusinessLicenseNumber(Integer id, String businessLicenseNumber) {
		// 判断营业执照注册号是否可用
		Merchant one = getOne(w -> {
			w.lambda().eq(Merchant::getBusinessLicenseNumber, businessLicenseNumber);
		});
		// 如果营业执照注册号已经被别人使用了，那么它就不能继续使用了
		if (one != null && !ObjectUtil.equal(id, one.getId())) {
			throw new UnsupportedOperationException("营业执照注册号: " + businessLicenseNumber + " 已经入驻，不能重复入驻");
		}
	}
}
