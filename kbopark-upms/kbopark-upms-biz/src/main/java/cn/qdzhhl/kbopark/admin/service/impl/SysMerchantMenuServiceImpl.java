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
package cn.qdzhhl.kbopark.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qdzhhl.kbopark.admin.api.entity.SysMerchantMenu;
import cn.qdzhhl.kbopark.admin.mapper.SysMerchantMenuMapper;
import cn.qdzhhl.kbopark.admin.service.SysMerchantMenuService;
import cn.qdzhhl.kbopark.common.core.constant.CacheConstants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家菜单权限关联表
 *
 * @author pigx code generator
 * @date 2020-09-04 15:35:57
 */
@Service
@AllArgsConstructor
public class SysMerchantMenuServiceImpl extends ServiceImpl<SysMerchantMenuMapper, SysMerchantMenu> implements SysMerchantMenuService {

	private final CacheManager cacheManager;

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, key = "#merchantId + '_merchant_menu'")
	public Boolean saveMerchantMenus(Integer merchantId, String menuIds) {

		//先清除绑定关系
		this.remove(Wrappers.<SysMerchantMenu>query().lambda().eq(SysMerchantMenu::getMerchantId, merchantId));
		if (StrUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}

		List<SysMerchantMenu> merchantMenuList = Arrays.stream(menuIds.split(",")).map(menuId -> {
			SysMerchantMenu merchantMenu = new SysMerchantMenu();
			merchantMenu.setMerchantId(merchantId);
			merchantMenu.setMenuId(Integer.valueOf(menuId));
			return merchantMenu;
		}).collect(Collectors.toList());

		// 清空userinfo
		cacheManager.getCache(CacheConstants.USER_DETAILS).clear();

		return this.saveBatch(merchantMenuList);
	}
}
