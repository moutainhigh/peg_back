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
import cn.qdzhhl.kbopark.admin.api.entity.SysDeptMenu;
import cn.qdzhhl.kbopark.admin.api.entity.SysRoleMenu;
import cn.qdzhhl.kbopark.common.core.constant.CacheConstants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qdzhhl.kbopark.admin.mapper.SysDeptMenuMapper;
import cn.qdzhhl.kbopark.admin.service.SysDeptMenuService;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 机构菜单关联表
 *
 * @author pigx code generator
 * @date 2020-08-26 15:01:04
 */
@Service
@AllArgsConstructor
public class SysDeptMenuServiceImpl extends ServiceImpl<SysDeptMenuMapper, SysDeptMenu> implements SysDeptMenuService {

	private final CacheManager cacheManager;



	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, key = "#deptId + '_dept_menu'")
	public Boolean saveDeptMenus(Integer deptId, String menuIds) {
		//先清除绑定关系
		this.remove(Wrappers.<SysDeptMenu>query().lambda().eq(SysDeptMenu::getDeptId, deptId));
		if (StrUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}

		List<SysDeptMenu> deptMenuList = Arrays.stream(menuIds.split(",")).map(menuId -> {
			SysDeptMenu deptMenu = new SysDeptMenu();
			deptMenu.setDeptId(deptId);
			deptMenu.setMenuId(Integer.valueOf(menuId));
			return deptMenu;
		}).collect(Collectors.toList());

		// 清空userinfo
		cacheManager.getCache(CacheConstants.USER_DETAILS).clear();

		return this.saveBatch(deptMenuList);
	}
}
