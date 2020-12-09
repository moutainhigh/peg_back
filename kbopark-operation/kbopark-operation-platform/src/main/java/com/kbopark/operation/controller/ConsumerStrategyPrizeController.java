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
import com.kbopark.operation.entity.ConsumerStrategyPrize;
import com.kbopark.operation.service.ConsumerStrategyPrizeService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 商家消费策略奖品表
 *
 * @author laomst
 * @date 2020-09-08 15:31:49
 */
@RestController
@AllArgsConstructor
@RequestMapping("/consumerstrategyprize" )
@Api(value = "consumerstrategyprize", tags = "商家消费策略奖品表管理")
public class ConsumerStrategyPrizeController {

    private final  ConsumerStrategyPrizeService consumerStrategyPrizeService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param consumerStrategyPrize 商家消费策略奖品表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('operation_consumerstrategyprize_view')" )
    public R getConsumerStrategyPrizePage(Page page, ConsumerStrategyPrize consumerStrategyPrize) {
        return R.ok(consumerStrategyPrizeService.page(page, Wrappers.query(consumerStrategyPrize)));
    }


    /**
     * 通过id查询商家消费策略奖品表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_consumerstrategyprize_view')" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(consumerStrategyPrizeService.getById(id));
    }

    /**
     * 新增商家消费策略奖品表
     * @param consumerStrategyPrize 商家消费策略奖品表
     * @return R
     */
    @ApiOperation(value = "新增商家消费策略奖品表", notes = "新增商家消费策略奖品表")
    @SysLog("新增商家消费策略奖品表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('operation_consumerstrategyprize_add')" )
    public R save(@RequestBody ConsumerStrategyPrize consumerStrategyPrize) {
        return R.ok(consumerStrategyPrizeService.save(consumerStrategyPrize));
    }

    /**
     * 修改商家消费策略奖品表
     * @param consumerStrategyPrize 商家消费策略奖品表
     * @return R
     */
    @ApiOperation(value = "修改商家消费策略奖品表", notes = "修改商家消费策略奖品表")
    @SysLog("修改商家消费策略奖品表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('operation_consumerstrategyprize_edit')" )
    public R updateById(@RequestBody ConsumerStrategyPrize consumerStrategyPrize) {
        return R.ok(consumerStrategyPrizeService.updateById(consumerStrategyPrize));
    }

    /**
     * 通过id删除商家消费策略奖品表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商家消费策略奖品表", notes = "通过id删除商家消费策略奖品表")
    @SysLog("通过id删除商家消费策略奖品表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_consumerstrategyprize_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(consumerStrategyPrizeService.removeById(id));
    }

}
