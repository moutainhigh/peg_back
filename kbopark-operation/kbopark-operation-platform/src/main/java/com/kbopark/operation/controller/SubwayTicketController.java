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

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.dto.SubwayTicketDTO;
import com.kbopark.operation.entity.SubwayTicket;
import com.kbopark.operation.enums.UsedStatusEnum;
import com.kbopark.operation.service.SubwayTicketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 乘车券管理
 *
 * @author pigx code generator
 * @date 2020-09-08 14:52:55
 */
@RestController
@AllArgsConstructor
@RequestMapping("/subwayticket")
@Api(value = "subwayticket", tags = "乘车券管理管理")
public class SubwayTicketController {

	private final SubwayTicketService subwayTicketService;

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param subwayTicketDTO 乘车券管理
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('operation_subwayticket_view')")
	public R getSubwayTicketPage(Page page, SubwayTicketDTO subwayTicketDTO) {
		QueryWrapper<SubwayTicket> query = Wrappers.query(subwayTicketDTO);
		if(StringUtils.isNotBlank(subwayTicketDTO.getSearchName())){
			query.lambda()
					.eq(SubwayTicket::getName,subwayTicketDTO.getSearchName())
					.or()
					.eq(SubwayTicket::getSubwayCode,subwayTicketDTO.getSearchName());
		}
		return R.ok(subwayTicketService.page(page, query));
	}


	/**
	 * 通过id查询乘车券管理
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_subwayticket_view')")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(subwayTicketService.getById(id));
	}

	/**
	 * 新增乘车券管理
	 *
	 * @param subwayTicket 乘车券管理
	 * @return R
	 */
	@ApiOperation(value = "新增乘车券管理", notes = "新增乘车券管理")
	@SysLog("新增乘车券管理")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('operation_subwayticket_add')")
	public R save(@RequestBody SubwayTicket subwayTicket) {
		subwayTicket.setUsedStatus(UsedStatusEnum.USED.getCode());
		subwayTicket.setSerialNumber(RandomUtil.randomStringUpper(32));
		return R.ok(subwayTicketService.save(subwayTicket));
	}

	/**
	 * 修改乘车券管理
	 *
	 * @param subwayTicket 乘车券管理
	 * @return R
	 */
	@ApiOperation(value = "修改乘车券管理", notes = "修改乘车券管理")
	@SysLog("修改乘车券管理")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('operation_subwayticket_edit')")
	public R updateById(@RequestBody SubwayTicket subwayTicket) {
		return R.ok(subwayTicketService.updateById(subwayTicket));
	}

	/**
	 * 通过id删除乘车券管理
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除乘车券管理", notes = "通过id删除乘车券管理")
	@SysLog("通过id删除乘车券管理")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('operation_subwayticket_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(subwayTicketService.removeById(id));
	}

}
