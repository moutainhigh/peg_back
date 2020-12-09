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
import cn.qdzhhl.kbopark.admin.mapper.SysDeptMenuMapper;
import cn.qdzhhl.kbopark.admin.utils.CommonUtil;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qdzhhl.kbopark.admin.api.dto.DeptTree;
import cn.qdzhhl.kbopark.admin.api.entity.SysDept;
import cn.qdzhhl.kbopark.admin.api.entity.SysDeptRelation;
import cn.qdzhhl.kbopark.admin.api.vo.TreeUtil;
import cn.qdzhhl.kbopark.admin.mapper.SysDeptMapper;
import cn.qdzhhl.kbopark.admin.service.SysDeptRelationService;
import cn.qdzhhl.kbopark.admin.service.SysDeptService;
import cn.qdzhhl.kbopark.common.data.datascope.DataScope;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author kbopark
 * @since 2018-01-20
 */
@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

	private final SysDeptRelationService sysDeptRelationService;

	private final SysDeptMapper deptMapper;

	private final SysDeptMenuMapper deptMenuMapper;

	/**
	 * 添加信息部门
	 * @param dept 部门
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveDept(SysDept dept) {
		SysDept sysDept = new SysDept();
		BeanUtils.copyProperties(dept, sysDept);
		this.save(sysDept);
		sysDeptRelationService.insertDeptRelation(sysDept);
		return Boolean.TRUE;
	}

	/**
	 * 删除部门
	 * @param id 部门 ID
	 * @return 成功、失败
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeDeptById(Integer id) {
		// 级联删除部门
		List<Integer> idList = sysDeptRelationService
				.list(Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getAncestor, id)).stream()
				.map(SysDeptRelation::getDescendant).collect(Collectors.toList());

		if (CollUtil.isNotEmpty(idList)) {
			this.removeByIds(idList);
		}

		// 删除部门级联关系
		sysDeptRelationService.deleteAllDeptRealtion(id);
		// 删除部门菜单关联关系
		deptMenuMapper.delete(Wrappers.<SysDeptMenu>lambdaQuery().eq(SysDeptMenu::getDeptId, id));
		return Boolean.TRUE;
	}

	/**
	 * 更新部门
	 * @param sysDept 部门信息
	 * @return 成功、失败
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateDeptById(SysDept sysDept) {
		// 更新部门状态
		this.updateById(sysDept);
		// 更新部门关系
		SysDeptRelation relation = new SysDeptRelation();
		relation.setAncestor(sysDept.getParentId());
		relation.setDescendant(sysDept.getDeptId());
		sysDeptRelationService.updateDeptRealtion(relation);
		return Boolean.TRUE;
	}

	/**
	 * 查询全部部门树
	 * @return 树
	 */
	@Override
	public List<DeptTree> selectTree() {
		KboparkUser user = SecurityUtils.getUser();
		QueryWrapper<SysDept> sysDeptQueryWrapper = new QueryWrapper<>();
		if(!CommonUtil.isSuperManager(user)){
			sysDeptQueryWrapper.lambda()
					.eq(SysDept::getDeptId,user.getDeptId())
					.or()
					.eq(SysDept::getParentId,user.getDeptId());
		}
		// 查询全部部门
		List<SysDept> deptAllList = deptMapper.selectList(sysDeptQueryWrapper);
		// 查询数据权限内部门
		List<Integer> deptOwnIdList = deptMapper.selectListByScope(Wrappers.emptyWrapper(), new DataScope()).stream()
				.map(SysDept::getDeptId).collect(Collectors.toList());

		// 权限内部门
		List<DeptTree> collect = deptAllList.stream().filter(dept -> dept.getDeptId().intValue() != dept.getParentId())
				.sorted(Comparator.comparingInt(SysDept::getSort)).map(dept -> {
					DeptTree node = new DeptTree();
					node.setId(dept.getDeptId());
					node.setParentId(dept.getParentId());
					node.setName(dept.getName());

					// 有权限不返回标识
					if (deptOwnIdList.contains(dept.getDeptId())) {
						node.setIsLock(Boolean.FALSE);
					}
					return node;
				}).collect(Collectors.toList());
		if(user.getDeptId().intValue() != 1){
			SysDept sysDept = deptMapper.selectById(user.getDeptId());
			return TreeUtil.build(collect, sysDept.getParentId());
		}else {
			return TreeUtil.build(collect, 0);
		}

	}

}
