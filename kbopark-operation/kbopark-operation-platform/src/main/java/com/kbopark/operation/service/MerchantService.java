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

import cn.qdzhhl.kbopark.common.data.mybatis.KbBaseService;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kbopark.operation.dto.MerchantReviewParam;
import com.kbopark.operation.dto.MerchantSearchParam;
import com.kbopark.operation.entity.Merchant;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商家基本信息表
 *
 * @author laomst
 * @date 2020-08-25 17:59:54
 */
public interface MerchantService extends KbBaseService<Merchant> {

	/**
	 * 自定义分页
	 */
	IPage<Merchant> selectMerchantPage(IPage<Merchant> page, MerchantSearchParam param);

	/**
	 * 商户自主入驻
	 * @param entity
	 * @return
	 */
    Boolean merchantEnterSelf(Merchant entity);

    /**
	 * 新增
	 * @param entity
	 * @param user
	 * @return
	 */
	@Transactional
	Boolean saveMerchant(Merchant entity, KboparkUser user);


	/**
	 * 修改
	 * @param entity
	 * @param user
	 * @return
	 */
	@Transactional
	Boolean updateMerchant(Merchant entity, KboparkUser user);

	/**
	 * 启用或者禁用
	 * @param id
	 * @param user
	 * @return
	 */
	Boolean upOrDown(Integer id, KboparkUser user);

	/**
	 * 审核
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional
	Boolean review(MerchantReviewParam param, KboparkUser user);


}
