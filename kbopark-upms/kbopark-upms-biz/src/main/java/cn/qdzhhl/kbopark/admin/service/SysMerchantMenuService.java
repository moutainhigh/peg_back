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

import cn.qdzhhl.kbopark.admin.api.entity.SysMerchantMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 商家菜单权限关联表
 *
 * @author pigx code generator
 * @date 2020-09-04 15:35:57
 */
public interface SysMerchantMenuService extends IService<SysMerchantMenu> {


	/**
	 * 更新商家菜单关联
	 *
	 * @param merchantId 商家ID
	 * @param menuIds    菜单ID拼成的字符串，每个id之间根据逗号分隔
	 * @return
	 */
	Boolean saveMerchantMenus(Integer merchantId, String menuIds);

}
