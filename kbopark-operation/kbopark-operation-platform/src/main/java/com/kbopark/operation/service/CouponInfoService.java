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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.dto.CouponQueryDTO;
import com.kbopark.operation.dto.CouponReviewParam;
import com.kbopark.operation.entity.CouponInfo;
import com.kbopark.operation.vo.CouponInfoVO;
import com.kbopark.operation.vo.MerchantCouponVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 优惠券表
 *
 * @author pigx code generator
 * @date 2020-08-28 16:15:36
 */
public interface CouponInfoService extends KbBaseService<CouponInfo> {

	/**
	 * 分页查询优惠券列表
	 *
	 * @param page
	 * @param couponInfo
	 * @return
	 */
	R getPage(Page page, CouponInfo couponInfo);

	/**
	 * 新增优惠券
	 *
	 * @param couponInfo
	 * @return
	 */
	R saveAndCheck(CouponInfo couponInfo);

	/**
	 * 审核更新并生成审核记录
	 *
	 * @return
	 */
	R reviewAndLog(CouponReviewParam param);

	/**
	 * 修改
	 *
	 * @param couponInfo
	 * @return
	 */
	R editCouponStatus(CouponInfo couponInfo);


	/**
	 * 根据券编号查询优惠券基本信息
	 *
	 * @param serialNumber
	 * @return
	 */
	CouponInfo getBySerialNumber(String serialNumber);

	/***
	 * 验证优惠券是否可用
	 * @param serialNumber    优惠券编号
	 * @return
	 */
	R<CouponInfo> checkCouponAvailable(String serialNumber);

	/**
	 * 验证优惠券是否可用
	 * 是否存在该优惠券
	 * 是否在有效期内
	 * 是否已被禁用
	 * 是否已下架
	 *
	 * @param couponInfo 优惠券信息
	 * @return
	 */
	R<CouponInfo> checkCouponAvailable(CouponInfo couponInfo);


	/**
	 * [用户端]根据用户定位信息查询附近几公里内得优惠券列表
	 *
	 * @param couponQueryDTO
	 * @return
	 */
	Page<CouponInfoVO> getCouponList(CouponQueryDTO couponQueryDTO, String memberPhone);
	/**
	 * [用户端]根据用户定位信息查询附近几公里内得优惠券列表
	 *
	 * @param couponQueryDTO
	 * @return
	 */
	List<CouponInfoVO> couponAdvertList(CouponQueryDTO couponQueryDTO, String memberPhone);


	/**
	 * [用户端]根据用户定位信息查询附近几公里内得商家列表
	 *
	 * @param couponQueryDTO
	 * @return
	 */
	R getMerchantList(CouponQueryDTO couponQueryDTO);

	/**
	 * 用户端商家卡券分页查询
	 *
	 * @param page
	 * @param merchantId
	 * @param memberPhone
	 * @return
	 */
	Page<MerchantCouponVO> selectMemberMerchantCouponPage(Page<MerchantCouponVO> page, String type, Integer merchantId, String memberPhone);


	/**
	 * 获取用户可以领取的卡券列表
	 *
	 * @return
	 */
	List<MerchantCouponVO> getCanReceiveList(List<Integer> ids, String memberPhone, Integer merchantId);

	/**
	 * 设置权重
	 * @param couponId
	 * @param weight
	 * @return
	 */
	Boolean setWeight(Integer couponId, Integer weight);


}
