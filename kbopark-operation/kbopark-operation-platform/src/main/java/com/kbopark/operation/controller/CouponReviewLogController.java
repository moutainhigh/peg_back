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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.entity.CouponReviewLog;
import com.kbopark.operation.service.CouponReviewLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 优惠券审核记录
 *
 * @author pigx code generator
 * @date 2020-08-30 14:50:54
 */
@RestController
@AllArgsConstructor
@RequestMapping("/couponreviewlog" )
@Api(value = "couponreviewlog", tags = "优惠券审核记录管理")
public class CouponReviewLogController {

    private final  CouponReviewLogService couponReviewLogService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param couponReviewLog 优惠券审核记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getCouponReviewLogPage(Page page, CouponReviewLog couponReviewLog) {
        return R.ok(couponReviewLogService.page(page, Wrappers.query(couponReviewLog).lambda().orderByDesc(CouponReviewLog::getId)));
    }


    /**
     * 通过id查询优惠券审核记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(couponReviewLogService.getById(id));
    }


	/**
	 * 通过couponId查询优惠券审核记录
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/coupon/{id}" )
	public R getByCoupon(@PathVariable("id" ) Integer id) {
		List<CouponReviewLog> list = couponReviewLogService.list(Wrappers.<CouponReviewLog>lambdaQuery().eq(CouponReviewLog::getCouponId, id));
		return R.ok(list);
	}

    /**
     * 新增优惠券审核记录
     * @param couponReviewLog 优惠券审核记录
     * @return R
     */
    @ApiOperation(value = "新增优惠券审核记录", notes = "新增优惠券审核记录")
    @SysLog("新增优惠券审核记录" )
    @PostMapping
    public R save(@RequestBody CouponReviewLog couponReviewLog) {
        return R.ok(couponReviewLogService.save(couponReviewLog));
    }

    /**
     * 修改优惠券审核记录
     * @param couponReviewLog 优惠券审核记录
     * @return R
     */
    @ApiOperation(value = "修改优惠券审核记录", notes = "修改优惠券审核记录")
    @SysLog("修改优惠券审核记录" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('operation_couponreviewlog_edit')" )
    public R updateById(@RequestBody CouponReviewLog couponReviewLog) {
        return R.ok(couponReviewLogService.updateById(couponReviewLog));
    }

    /**
     * 通过id删除优惠券审核记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除优惠券审核记录", notes = "通过id删除优惠券审核记录")
    @SysLog("通过id删除优惠券审核记录" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_couponreviewlog_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(couponReviewLogService.removeById(id));
    }

}
