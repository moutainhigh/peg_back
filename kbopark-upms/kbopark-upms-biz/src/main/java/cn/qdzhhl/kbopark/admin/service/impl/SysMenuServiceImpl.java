/*
 *
 *      Copyright (c) 2018-2025, kbopark All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: kbopark
 *
 */

package cn.qdzhhl.kbopark.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qdzhhl.kbopark.admin.api.entity.SysDeptMenu;
import cn.qdzhhl.kbopark.admin.api.entity.SysMerchantMenu;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.admin.mapper.SysDeptMenuMapper;
import cn.qdzhhl.kbopark.admin.mapper.SysMerchantMenuMapper;
import cn.qdzhhl.kbopark.admin.utils.CommonUtil;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qdzhhl.kbopark.admin.api.dto.MenuTree;
import cn.qdzhhl.kbopark.admin.api.entity.SysMenu;
import cn.qdzhhl.kbopark.admin.api.entity.SysRoleMenu;
import cn.qdzhhl.kbopark.admin.api.vo.MenuVO;
import cn.qdzhhl.kbopark.admin.api.vo.TreeUtil;
import cn.qdzhhl.kbopark.admin.mapper.SysMenuMapper;
import cn.qdzhhl.kbopark.admin.mapper.SysRoleMenuMapper;
import cn.qdzhhl.kbopark.admin.service.SysMenuService;
import cn.qdzhhl.kbopark.common.core.constant.CacheConstants;
import cn.qdzhhl.kbopark.common.core.constant.CommonConstants;
import cn.qdzhhl.kbopark.common.core.constant.enums.MenuTypeEnum;
import cn.qdzhhl.kbopark.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author kbopark
 * @since 2017-10-29
 */
@Service
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

	private final SysRoleMenuMapper sysRoleMenuMapper;

	private final SysDeptMenuMapper sysDeptMenuMapper;

	private final SysMerchantMenuMapper sysMerchantMenuMapper;

	@Override
	@Cacheable(value = CacheConstants.MENU_DETAILS, key = "#roleId  + '_menu'", unless = "#result.isEmpty()")
	public List<MenuVO> findMenuByRoleId(Integer roleId) {
		return baseMapper.listMenusByRoleId(roleId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
	public R removeMenuById(Integer id) {
		// 查询父节点为当前节点的节点
		List<SysMenu> menuList = this.list(Wrappers.<SysMenu>query().lambda().eq(SysMenu::getParentId, id));
		if (CollUtil.isNotEmpty(menuList)) {
			return R.failed("菜单含有下级不能删除");
		}

		sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getMenuId, id));
		// 删除当前菜单及其子菜单
		return R.ok(this.removeById(id));
	}

	@Override
	@CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
	public Boolean updateMenuById(SysMenu sysMenu) {
		return this.updateById(sysMenu);
	}

	/**
	 * 构建树查询 1. 不是懒加载情况，查询全部 2. 是懒加载，根据parentId 查询 2.1 父节点为空，则查询ID -1
	 * @param lazy 是否是懒加载
	 * @param parentId 父节点ID
	 * @return
	 */
	@Override
	public List<MenuTree> treeMenu(boolean lazy, Integer parentId) {
		KboparkUser user = SecurityUtils.getUser();
		//如果不是商家查询机构菜单关联信息，如果是商家查询商家菜单关联信息
		List<Integer> menuIds;
		if(user.getUserType() >= UserTypeEnum.Merchant.getCode()){
			List<SysMerchantMenu> merchantMenuList = sysMerchantMenuMapper.selectList(Wrappers.<SysMerchantMenu>lambdaQuery().eq(SysMerchantMenu::getMerchantId, user.getMerchantId()));
			menuIds = merchantMenuList.stream().map(SysMerchantMenu::getMenuId).collect(Collectors.toList());
		}else{
			List<SysDeptMenu> deptMenuList = sysDeptMenuMapper.selectList(Wrappers.<SysDeptMenu>lambdaQuery().eq(SysDeptMenu::getDeptId, user.getDeptId()));
			menuIds = deptMenuList.stream().map(SysDeptMenu::getMenuId).collect(Collectors.toList());
		}

		if (!lazy) {
			List<SysMenu> sysMenus = baseMapper.selectList(Wrappers.<SysMenu>lambdaQuery().orderByAsc(SysMenu::getSort));
			if(CommonUtil.isSuperManager(user)){
				return TreeUtil.buildTree(sysMenus,CommonConstants.MENU_TREE_ROOT_ID);
			}else{
				List<SysMenu> collect = sysMenus.stream().filter(sysMenu -> menuIds.contains(sysMenu.getMenuId())).collect(Collectors.toList());
				return TreeUtil.buildTree(collect,CommonConstants.MENU_TREE_ROOT_ID);
			}
		}

		Integer parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
		List<SysMenu> sysMenus = baseMapper.selectList(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, parent).orderByAsc(SysMenu::getSort));
		if(CommonUtil.isSuperManager(user)){
			return TreeUtil.buildTree(sysMenus,parent);
		}else{
			List<SysMenu> collect = sysMenus.stream().filter(sysMenu -> menuIds.contains(sysMenu.getMenuId())).collect(Collectors.toList());
			return TreeUtil.buildTree(collect,parent);
		}

	}

	/**
	 * 查询菜单
	 * @param all 全部菜单
	 * @param type 类型
	 * @param parentId 父节点ID
	 * @return
	 */
	@Override
	public List<MenuTree> filterMenu(Set<MenuVO> all, String type, Integer parentId) {
		List<MenuTree> menuTreeList = all.stream().filter(menuTypePredicate(type)).map(MenuTree::new)
				.sorted(Comparator.comparingInt(MenuTree::getSort)).collect(Collectors.toList());
		Integer parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
		return TreeUtil.build(menuTreeList, parent);
	}

	/**
	 * menu 类型断言
	 * @param type 类型
	 * @return Predicate
	 */
	private Predicate<MenuVO> menuTypePredicate(String type) {
		return vo -> {
			if (MenuTypeEnum.TOP_MENU.getDescription().equals(type)) {
				return MenuTypeEnum.TOP_MENU.getType().equals(vo.getType());
			}
			// 其他查询 左侧 + 顶部
			return !MenuTypeEnum.BUTTON.getType().equals(vo.getType());
		};
	}

}
