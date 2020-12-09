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

package com.kbopark.operation.api.memberapi;

import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.entity.MemberAddress;
import com.kbopark.operation.entity.MemberInfo;
import com.kbopark.operation.service.MemberAddressService;
import com.kbopark.operation.service.MemberInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 会员收货地址
 *
 * @author pigx code generator
 * @date 2020-10-27 10:29:05
 */
@RestController
@AllArgsConstructor
@RequestMapping("/memberAddress" )
@Api(value = "memberAddress", tags = "【_青小岛-会员收货地址管理】")
public class MemberAddressController {

    private final  MemberAddressService memberAddressService;

    private final MemberInfoService memberInfoService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param memberAddress 会员收货地址
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getMemberAddressPage(Page page, MemberAddress memberAddress) {
		KboparkUser user = SecurityUtils.getUser();

		//根据当前登录手机号查询对应用户书
		MemberInfo memberInfo = this.memberInfoService.findByMobile(user.getPhone());

		//插入用户id
		memberAddress.setMemberId(memberInfo.getId());
		return R.ok(memberAddressService.page(page, Wrappers.query(memberAddress)));
    }
    
    /**
     * 通过id查询会员收货地址
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(memberAddressService.getById(id));
    }

    /**
     * 新增会员收货地址
     * @param memberAddress 会员收货地址
     * @return R
     */
    @ApiOperation(value = "新增会员收货地址", notes = "新增会员收货地址")
    @SysLog("新增会员收货地址" )
    @PostMapping
    public R save(@RequestBody MemberAddress memberAddress) {

    	//获取当前登录用户
		KboparkUser user = SecurityUtils.getUser();

		//根据当前登录人手机号查询对应用户数据
		MemberInfo memberInfo = this.memberInfoService.findByMobile(user.getPhone());

		//插入ID
		memberAddress.setMemberId(memberInfo.getId());

        return R.ok(memberAddressService.save(memberAddress));
    }

    /**
     * 修改会员收货地址
     * @param memberAddress 会员收货地址
     * @return R
     */
    @ApiOperation(value = "修改会员收货地址", notes = "修改会员收货地址")
    @SysLog("修改会员收货地址" )




    @PutMapping
    public R updateById(@RequestBody MemberAddress memberAddress) {

		KboparkUser user = SecurityUtils.getUser();
		//根据当前登录人手机号查询对应用户数据
		MemberInfo memberInfo = this.memberInfoService.findByMobile(user.getPhone());

		//插入ID
		memberAddress.setMemberId(memberInfo.getId());
        return R.ok(memberAddressService.updateById(memberAddress));
    }

    /**
     * 通过id删除会员收货地址
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除会员收货地址", notes = "通过id删除会员收货地址")
    @SysLog("通过id删除会员收货地址" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(memberAddressService.removeById(id));
    }

}
