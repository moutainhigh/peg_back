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

package com.kbopark.operation.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.dto.MemberSearchParam;
import com.kbopark.operation.dto.MerchantMemberPageDTO;
import com.kbopark.operation.dto.VipCardDTO;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.entity.MerchantMember;
import com.kbopark.operation.service.MerchantMemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 商家会员
 *
 * @author laomst
 * @date 2020-08-31 15:16:27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/merchantmember" )
@Api(value = "merchantmember", tags = "商家会员管理")
public class MerchantMemberController {

    private final  MerchantMemberService merchantMemberService;

    /**
     * 分页查询
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('operation_merchantmember_view')" )
    public R getMerchantMemberPage(Page<MerchantMemberPageDTO> page, MemberSearchParam param) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		// 如果当前用户是渠道商
		else if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Channel.code)) {
			param.setDistributorIdKey(user.getDeptId());
		}
		// 如果当前用户是运营平台
		else if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Operation.code)) {
			param.setOperatorIdKey(user.getDeptId());
		}
		// 除此之外，如果不是总平台
		else if (!ObjectUtil.equal(user.getUserType(), UserTypeEnum.Administrator.code)) {
			return R.failed("没有权限");
		}
        return R.ok(merchantMemberService.selectMemberPage(page, param));
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/vipcard-page" )
//    @PreAuthorize("@pms.hasPermission('operation_merchantmember_vipCard')" )
    public R getVipcardPage(Page<VipCardDTO> page, VipCardDTO param) {
		KboparkUser user = SecurityUtils.getUser();
		if (null == user) {
			return R.failed("获取用户信息失败");
		}
		// 如果当前用户是渠道商
		else if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Channel.code)) {
			param.setDistributorId(user.getDeptId());
		}
		// 如果当前用户是运营平台
		else if (ObjectUtil.equal(user.getUserType(), UserTypeEnum.Operation.code)) {
			param.setOperatorId(user.getDeptId());
		}
		// 除此之外，如果不是总平台
		else if (!ObjectUtil.equal(user.getUserType(), UserTypeEnum.Administrator.code)) {
			return R.failed("没有权限");
		}
        return R.ok(merchantMemberService.selectVipcardPage(page, param));
    }


    /**
     * 通过id查询商家会员
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_merchantmember_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(merchantMemberService.getById(id));
    }

    /**
     * 新增商家会员
     * @param merchantMember 商家会员
     * @return R
     */
    @ApiOperation(value = "新增商家会员", notes = "新增商家会员")
    @SysLog("新增商家会员" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('operation_merchantmember_add')" )
    public R save(@RequestBody MerchantMember merchantMember) {
        return R.ok(merchantMemberService.save(merchantMember));
    }

    /**
     * 修改商家会员
     * @param merchantMember 商家会员
     * @return R
     */
    @ApiOperation(value = "修改商家会员", notes = "修改商家会员")
    @SysLog("修改商家会员" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('operation_merchantmember_edit')" )
    public R updateById(@RequestBody MerchantMember merchantMember) {
        return R.ok(merchantMemberService.updateById(merchantMember));
    }

    /**
     * 通过id删除商家会员
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商家会员", notes = "通过id删除商家会员")
    @SysLog("通过id删除商家会员" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_merchantmember_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(merchantMemberService.removeById(id));
    }

}
