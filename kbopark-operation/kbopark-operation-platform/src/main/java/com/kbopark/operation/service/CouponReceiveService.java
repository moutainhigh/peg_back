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

import cn.hutool.core.util.ObjectUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.data.mybatis.KbBaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.apidto.MonthNumberStatistics;
import com.kbopark.operation.dto.*;
import com.kbopark.operation.entity.CouponReceive;
import com.kbopark.operation.entity.MemberInfo;
import com.kbopark.operation.entity.MerchantMember;
import com.kbopark.operation.subway.util.CouponProvide;
import com.kbopark.operation.subway.util.CouponResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 优惠券领取记录
 *
 * @author pigx code generator
 * @date 2020-09-01 09:35:43
 */
public interface CouponReceiveService extends KbBaseService<CouponReceive> {

	/**
	 * 分页查询
	 *
	 * @param page
	 * @param couponReceiveDTO
	 * @return
	 */
	R getPage(Page page, CouponReceiveDTO couponReceiveDTO);


	/**
	 * 优惠券和红包领取记录
	 *
	 * @param couponReceiveDTO
	 * @return
	 */
	R<CouponReceive> receiveAndCreateRecord(CouponReceiveDTO couponReceiveDTO);

	/**
	 * 领取接口对外提供更好读的参数
	 * @param param
	 * @param memberInfo
	 * @return
	 */
	default R<CouponReceive> memberReceive(ReceiveCouponParam param, MemberInfo memberInfo) {
		Objects.requireNonNull(param, "请设置领取参数");
		Objects.requireNonNull(memberInfo, "请设置用户信息");
		CouponReceiveDTO dto = new CouponReceiveDTO();
		dto.setMerchantId(param.getMerchantId());
		dto.setCouponSerialNumber(param.getCouponSerialNumber());
		dto.setPhone(memberInfo.getPhone());
		return receiveAndCreateRecord(dto);
	}

	/**
	 * 批量领取卡券
	 * @param param
	 * @param memberInfo
	 * @return
	 */
	R<List<CouponReceive>> memberBatchReceive(BatchReceiveCouponParam param, MemberInfo memberInfo);


	/**
	 * 乘车券发放
	 *
	 * @param couponReceiveDTO
	 * @return
	 */
	R receiveTicket(CouponReceiveDTO couponReceiveDTO);


	/**
	 * 乘车券领取记录
	 *
	 * @param phone 手机号
	 * @param type  券类别
	 * @return
	 */
	R getTicketRecord(String phone, String type);


	/**
	 * 根据会员手机号和券标识查询是否存在领取记录
	 *
	 * @param memberPhone 手机号
	 * @param couponCode  优惠券编号
	 * @param lockFlag    优惠券编号
	 * @return
	 */
	List<CouponReceive> findByMemberPhoneAndCouponCode(String memberPhone, String couponCode, String lockFlag);


	/***
	 * 根据用户手机号查询当前用户已经领取的优惠券和红包以及乘车券
	 * @param memberPhone
	 * @return
	 */
	List<CouponReceive> findByMemberPhone(String memberPhone);

	/**
	 * 查询用户卡券分页列表
	 *
	 * @param page
	 * @param param
	 * @return
	 */
	IPage<MemberCouponDTO> selectMemberCouponPage(Page<MemberCouponDTO> page, CouponSearchParam param);

	/**
	 * 查询用户卡券列表
	 * @param param
	 * @return
	 */
	List<MemberCouponDTO> selectMemberCouponList(CouponSearchParam param);


	/**
	 * 请求地铁APP发乘车券
	 *
	 * @param couponProvide
	 * @return
	 */
	CouponResponse sendSubwayPost(CouponProvide couponProvide);

	/**
	 * 查询某个商家近n个月的卡券领取数量
	 * @param merchantId
	 * @param monthNumber
	 * @return
	 */
	List<MonthNumberStatistics> lastMonthCountGroup(Integer merchantId, Integer monthNumber);

	/**
	 * 立即使用卡券
	 * @param serialNumber
	 * @return
	 */
	R<Boolean>  immediateUse(String serialNumber, String memberPhone);


}
