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

package cn.qdzhhl.kbopark.admin.service;

import cn.qdzhhl.kbopark.admin.api.entity.SysDeptMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 机构菜单关联表
 *
 * @author pigx code generator
 * @date 2020-08-26 15:01:04
 */
public interface SysDeptMenuService extends IService<SysDeptMenu> {


	/**
	 * 更新机构菜单关联
	 *
	 * @param deptId
	 * @param menuIds	菜单ID拼成的字符串，每个id之间根据逗号分隔
	 * @return
	 */
	Boolean saveDeptMenus(Integer deptId, String menuIds);

}
