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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.dto.CouponQueryDTO;
import com.kbopark.operation.dto.CouponReviewParam;
import com.kbopark.operation.entity.*;
import com.kbopark.operation.enums.*;
import com.kbopark.operation.mapper.CouponInfoMapper;
import com.kbopark.operation.mapper.CouponReceiveMapper;
import com.kbopark.operation.mapper.MerchantMapper;
import com.kbopark.operation.service.CouponInfoService;
import com.kbopark.operation.service.CouponReviewLogService;
import com.kbopark.operation.service.SubwaySiteActivityService;
import com.kbopark.operation.service.SubwaySiteService;
import com.kbopark.operation.util.*;
import com.kbopark.operation.vo.CouponInfoVO;
import com.kbopark.operation.vo.MerchantCouponVO;
import com.kbopark.operation.vo.MerchantInfoVO;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 优惠券表
 *
 * @author pigx code generator
 * @date 2020-08-28 16:15:36
 */
@Service
@AllArgsConstructor
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

	private final CouponReviewLogService couponReviewLogService;

	private final MerchantMapper merchantMapper;

	private final CouponReceiveMapper couponReceiveMapper;

	private final CouponInfoMapper couponInfoMapper;

	private final SubwaySiteService subwaySiteService;

	private final SubwaySiteActivityService subwaySiteActivityService;


	@Override
	public R getPage(Page page, CouponInfo couponInfo) {
		if (!CouponTypeEnum.COUPON_TYPE.getCode().equals(couponInfo.getType())) {
			couponInfo.setType(CouponTypeEnum.RED_PACK_TYPE.getCode());
		}
		QueryWrapper<CouponInfo> query = Wrappers.query(couponInfo);
		//附加条件
		checkUserTypeAndSetCondition(query);
		// 按创建时间倒排
		query.lambda().orderByDesc(CouponInfo::getId);
		return R.ok(page(page, query));
	}

	@Override
	public R saveAndCheck(CouponInfo couponInfo) {

		//待审核
		couponInfo.setAuditStatus(CouponAuditStatusEnum.WAIT.getCode());
		//未禁用
		couponInfo.setLockFlag(LockStatusEnum.UNLOCK.getCode());
		//未上架
		couponInfo.setTakeStatus(CouponStatusEnum.OFFLINE.getCode());
		couponInfo.setUsedNumber(0);
		couponInfo.setRemainNumber(couponInfo.getTotalNumber());

		//当前用户（权限控制在商家）
		KboparkUser user = SecurityUtils.getUser();
		//用户信息
		couponInfo.setCreateUserId(user.getId());
		//当前用户名
		couponInfo.setCreateBy(user.getRealName());
		//商家信息
		if (user.getMerchantId() != null) {
			Merchant merchant = merchantMapper.selectById(user.getMerchantId());
			//商家名称
			couponInfo.setMerchantName(merchant.getSimpleName());
			//商家ID
			couponInfo.setMerchantId(user.getMerchantId());
			//渠道商ID
			couponInfo.setChannelId(merchant.getDistributorId());
			//运营商ID
			couponInfo.setOperationId(merchant.getOperatorId());
		}

		//编号
		if (StrUtil.isBlank(couponInfo.getCouponSerialNumber())) {
			String code = RandomUtil.randomStringUpper(32);
			couponInfo.setCouponSerialNumber(code);
		}
//		if ("coupon".equals(couponInfo.getType())) {
//			couponInfo.setCouponSetInfo("满"+ couponInfo.getFullMoney() + "元"+",减"+ couponInfo.getSubMoney() + "元");
//		}
		saveOrUpdate(couponInfo);
		// 保存审核记录并返回
		return R.ok(couponReviewLogService.save(CouponReviewLog.newLogOfCoupon(couponInfo.getId(), user.getRealName(), user.getId())));
	}

	@Override
	public R reviewAndLog(CouponReviewParam param) {
		KboparkUser user = SecurityUtils.getUser();
		CouponInfo couponInfo = getById(param.getCouponId());
		// 审核日志
		CouponReviewLog reviewLog = couponReviewLogService.getLastLogOfCoupon(couponInfo.getId())
				.orElse(CouponReviewLog.newLogOfCoupon(couponInfo.getId(), couponInfo.getCreateBy(), couponInfo.getCreateUserId()));
		CouponAuditStatusEnum reviewStatus = reviewLog.review(param, user.getUserType(), user.getId(), user.getRealName());
		couponInfo.setAuditStatus(reviewStatus.getCode());
		couponInfo.setAuditMarkInfo(param.getRemark());
		couponReviewLogService.saveOrUpdate(reviewLog);
		return R.ok(updateById(couponInfo));
	}

	@Override
	@Transactional
	public R editCouponStatus(CouponInfo couponInfo) {
		couponInfo.setUpdateTime(LocalDateTime.now());
		Integer takeStatus = couponInfo.getTakeStatus();
		CouponInfo byId = getById(couponInfo.getId());
		// 如果是审核失败的卡券，就走创建的流程
		if (CouponAuditStatusEnum.FAIL.getCode().equals(byId.getAuditStatus())) {
			return saveAndCheck(couponInfo);
		}

		boolean isRandom = RedpackMiniTypeEnum.RANDOM.getCode().equals(byId.getCouponType())
				&& CouponTypeEnum.RED_PACK_TYPE.getCode().equals(byId.getType());
		//如果是审核成功的优惠券验证期限和数量
		if (CouponAuditStatusEnum.SUCCESS.getCode().equals(byId.getAuditStatus())) {
			if (!isRandom && (byId.getTotalNumber() > couponInfo.getTotalNumber())) {
				return R.failed("已通过审核的优惠券或红包数量只支持增加，不可减少");
			}
			if (byId.getEndTime().getTime() > couponInfo.getEndTime().getTime()) {
				return R.failed("已通过审核的优惠券或红包有效期只支持延后操作");
			}
		}
		//设置数量
		if (!isRandom) {
			couponInfo.setRemainNumber(couponInfo.getTotalNumber() - byId.getUsedNumber());
		}
		couponInfo.setTakeStatus(takeStatus);
		couponReceiveMapper.update(null,
				Wrappers.<CouponReceive>lambdaUpdate().set(CouponReceive::getCouponEndTime, couponInfo.getEndTime())
						.eq(CouponReceive::getCouponSerialNumber, byId.getCouponSerialNumber()));
		return R.ok(updateById(couponInfo));
	}


	@Override
	public CouponInfo getBySerialNumber(String serialNumber) {
		if (StringUtils.isBlank(serialNumber)) {
			return null;
		}
		List<CouponInfo> list = list(Wrappers.<CouponInfo>lambdaQuery().eq(CouponInfo::getCouponSerialNumber, serialNumber));
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public R<CouponInfo> checkCouponAvailable(String serialNumber) {
		CouponInfo bySerialNumber = getBySerialNumber(serialNumber);
		return checkCouponAvailable(bySerialNumber);
	}

	@Override
	public R<CouponInfo> checkCouponAvailable(CouponInfo couponInfo) {
		if (couponInfo == null) {
			return R.failed(OperationConstants.COUPON_UNAVAILABLE_EMPTY);
		}
		Date startTime = couponInfo.getStartTime();
		// 加一天
		Date endTime = DateUtil.offsetDay(couponInfo.getEndTime(), 1);
		if (startTime.after(new Date())) {
			return R.failed(OperationConstants.COUPON_UNAVAILABLE_START_TIME);
		}
		if (endTime.before(new Date())) {
			return R.failed(OperationConstants.COUPON_UNAVAILABLE_END_TIME);
		}
		if (LockStatusEnum.LOCKED.getCode().equals(couponInfo.getLockFlag())) {
			return R.failed(OperationConstants.COUPON_UNAVAILABLE_BANNED);
		}
		if (CouponStatusEnum.OFFLINE.getCode().equals(couponInfo.getTakeStatus())) {
			return R.failed(OperationConstants.COUPON_UNAVAILABLE_DOWN);
		}
		return R.ok(couponInfo);
	}


	@Override
	public Page<CouponInfoVO> getCouponList(CouponQueryDTO couponQueryDTO, String memberPhone) {
		int current = couponQueryDTO.getCurrent() == null || couponQueryDTO.getCurrent() <= 0 ? 1 : couponQueryDTO.getCurrent();
		int size = couponQueryDTO.getSize() == null ? 20 : couponQueryDTO.getSize();
		Page<CouponInfoVO> page = new Page<>(current, size);
		List<CouponInfoVO> couponList = couponInfoMapper.getCouponPage(page, couponQueryDTO, memberPhone);
		if (CollectionUtils.isNotEmpty(couponList)) {
			KboparkUser user = SecurityUtils.getUser();
			// 处理是否领取的逻辑
			QueryWrapper<CouponReceive> queryWrapper = new QueryWrapper<>();
			queryWrapper.select("distinct(coupon_serial_number)")
					.lambda()
					.eq(CouponReceive::getMemberPhone, user.getPhone());
			List<CouponReceive> couponReceives = couponReceiveMapper.selectList(queryWrapper);
			final Set<String> serialNumberSet = couponReceives.stream().map(CouponReceive::getCouponSerialNumber).collect(Collectors.toSet());
			couponList.forEach(item -> {
				item.setUserReceived(serialNumberSet.contains(item.getCouponSerialNumber()));
			});
		}
		page.setRecords(couponList);
		return page;
		//1.根据经纬度查询站点信息获取code,根据code查询站点活动设置
//		double couponDistance = CouponUtil.DEFAULT_DISTANCE;
//		double couponNumber = CouponUtil.DEFAULT_NUM;
//		double redPackDistance = CouponUtil.DEFAULT_DISTANCE;
//		double redPackNumber = CouponUtil.DEFAULT_NUM;
		//
//		SubwaySite subwaySite = subwaySiteService.findByLngAndLat(lng, lat);
//		if (subwaySite != null && StringUtils.isNotBlank(subwaySite.getSiteCode())) {
//			SubwaySiteActivity activity = subwaySiteActivityService.findBySiteCode(subwaySite.getSiteCode());
//			if (activity != null) {
//				couponDistance = checkDoubleNumber(activity.getCouponDistance()) ? OrderUtil.mul(activity.getCouponDistance(), 1000) : CouponUtil.DEFAULT_DISTANCE;
//				couponNumber = checkIntegerNumber(activity.getCouponNumber()) ? activity.getCouponNumber() : CouponUtil.DEFAULT_NUM;
//				redPackDistance = checkDoubleNumber(activity.getRedpackDistance()) ? OrderUtil.mul(activity.getRedpackDistance(), 1000) : CouponUtil.DEFAULT_DISTANCE;
//				redPackNumber = checkIntegerNumber(activity.getRedpackNumber()) ? activity.getRedpackNumber() : CouponUtil.DEFAULT_NUM;
//			}
//		}
		//2.计算距离
//		for (CouponInfoVO vo : couponList) {
//			double distance = 0;
//			if (vo.getLat() != null && vo.getLng() != null) {
//				distance = GpsUtil.getDistance(lat, lng, vo.getLat(), vo.getLng());
//			}
//			vo.setDistance(distance);
//		}
		//3.过滤5km以内或者活动设置距离以内的数据
//		List<CouponInfoVO> distanceList = CouponUtil.filterCouponByDistance(couponList,couponDistance,redPackDistance);
		//4.过滤活动设置数量以内的数据
//		List<CouponInfoVO> resultList = CouponUtil.filterCouponByNumber(distanceList, couponNumber, redPackNumber);
	}

	@Override
	public List<CouponInfoVO> couponAdvertList(CouponQueryDTO couponQueryDTO, String memberPhone) {
		int current = couponQueryDTO.getCurrent() == null || couponQueryDTO.getCurrent() <= 0 ? 1 : couponQueryDTO.getCurrent();
		int size = couponQueryDTO.getSize() == null ? 20 : couponQueryDTO.getSize();
		Page<CouponInfoVO> page = new Page<>(current, size);
		return couponInfoMapper.getCouponPage(page, couponQueryDTO, memberPhone);
	}

	@Override
	public R getMerchantList(CouponQueryDTO couponQueryDTO) {

		int page = couponQueryDTO.getCurrent() == null ? 1 : couponQueryDTO.getCurrent();
		int size = couponQueryDTO.getSize() == null ? 20 : couponQueryDTO.getSize();
		couponQueryDTO.setCurrent((page - 1) * size);
		couponQueryDTO.setSize(size);
		List<MerchantInfoVO> merchantList = couponInfoMapper.getMerchantList(couponQueryDTO);
		//1.根据经纬度查询站点信息获取code,根据code查询站点活动设置
//		double merchantDistance = CouponUtil.DEFAULT_DISTANCE;
//		double merchantNumber = CouponUtil.DEFAULT_NUM;
//		SubwaySite subwaySite = subwaySiteService.findByLngAndLat(couponQueryDTO.getLng(), couponQueryDTO.getLat());
//		if (subwaySite != null && StringUtils.isNotBlank(subwaySite.getSiteCode())) {
//			SubwaySiteActivity activity = subwaySiteActivityService.findBySiteCode(subwaySite.getSiteCode());
//			if (activity != null) {
//				merchantDistance = checkDoubleNumber(activity.getMerchantDistance()) ? OrderUtil.mul(activity.getMerchantDistance(), 1000) : CouponUtil.DEFAULT_DISTANCE;
//				merchantNumber = checkIntegerNumber(activity.getMerchantNumber()) ? activity.getMerchantNumber() : CouponUtil.DEFAULT_NUM;
//			}
//		}
		//2.计算距离
//		for (MerchantInfoVO vo : merchantList) {
//			double distance = 0;
//			if (vo.getLat() != null && vo.getLng() != null) {
//				distance = GpsUtil.getDistance(couponQueryDTO.getLat(), couponQueryDTO.getLng(), vo.getLat(), vo.getLng());
//			}
//			vo.setDistance(distance);
//		}
		//3.根据活动设置参数过率数据
//		List<MerchantInfoVO> merchantInfoVOS = CouponUtil.filterMerchantByDistance(merchantList, merchantDistance, merchantNumber);
		return R.ok(merchantList);
	}

	@Override
	public Page<MerchantCouponVO> selectMemberMerchantCouponPage(Page<MerchantCouponVO> page, String type, Integer merchantId, String memberPhone) {
		return page.setRecords(baseMapper.selectMemberMerchantCouponPage(page, type, merchantId, memberPhone));
	}

	@Override
	public List<MerchantCouponVO> getCanReceiveList(List<Integer> ids, String memberPhone, Integer merchantId) {
		return baseMapper.getCanReceiveList(ids, memberPhone, merchantId);
	}

	@Override
	public Boolean setWeight(Integer couponId, Integer weight) {
		Objects.requireNonNull(couponId, "请设置id");
		Objects.requireNonNull(weight, "请设置权重值");
		return update(w -> w.lambda()
				.eq(CouponInfo::getId, couponId)
				.set(CouponInfo::getWeight, weight));
	}

	/***
	 * 判断活动数据是否设置
	 * @param num
	 * @return
	 */
	private boolean checkDoubleNumber(Double num) {
		if (num != null && num > 0) {
			return true;
		}
		return false;
	}

	private boolean checkIntegerNumber(Integer num) {
		if (num != null && num > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断用户类型并设置查询条件
	 *
	 * @param queryWrapper
	 */
	private void checkUserTypeAndSetCondition(QueryWrapper<CouponInfo> queryWrapper) {
		//当前用户判断
		KboparkUser user = SecurityUtils.getUser();
		//非总平台用户查看数据增加所属限制
		if (!UserTypeEnum.Administrator.getCode().equals(user.getUserType())) {
			if (UserTypeEnum.Operation.getCode().equals(user.getUserType())) {
				queryWrapper.lambda().eq(CouponInfo::getOperationId, user.getDeptId());
			} else if (UserTypeEnum.Channel.getCode().equals(user.getUserType()) || UserTypeEnum.Promoter.getCode().equals(user.getUserType())) {
				queryWrapper.lambda().eq(CouponInfo::getChannelId, user.getDeptId());
			} else if (UserTypeEnum.Merchant.getCode().equals(user.getUserType()) || UserTypeEnum.MerchantLower.getCode().equals(user.getUserType())) {
				queryWrapper.lambda().eq(CouponInfo::getMerchantId, user.getMerchantId());
			} else {
				queryWrapper.lambda().eq(CouponInfo::getMerchantId, -1);
			}
		}
	}
}
