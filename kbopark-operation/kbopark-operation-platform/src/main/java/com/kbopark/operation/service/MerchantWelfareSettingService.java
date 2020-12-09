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
import com.kbopark.operation.entity.MerchantWelfareSetting;

/**
 * 商家福利设置
 *
 * @author pigx code generator
 * @date 2020-09-09 15:53:58
 */
public interface MerchantWelfareSettingService extends IService<MerchantWelfareSetting> {

	/**
	 * 保存福利设置信息并验证福利比例
	 *
	 * @param merchantWelfareSetting
	 * @return
	 */
	R saveAndCheckSetting(MerchantWelfareSetting merchantWelfareSetting);


	/**
	 * 更新福利设置信息并验证福利比例
	 *
	 * @param merchantWelfareSetting
	 * @return
	 */
	R updateAndCheckSetting(MerchantWelfareSetting merchantWelfareSetting);


	/**
	 * 验证福利比例
	 *
	 * @param merchantId 商家Id
	 * @param fullMoney  消费金额
	 * @param ruleInfo   福利规则
	 * @return
	 */
	boolean checkSettingPercent(Integer merchantId, Double fullMoney, String ruleInfo);

}
