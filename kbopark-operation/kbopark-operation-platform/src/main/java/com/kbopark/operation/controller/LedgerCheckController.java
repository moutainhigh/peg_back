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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.kbopark.operation.dto.LedgerAuditDTO;
import com.kbopark.operation.dto.LedgerCheckDTO;
import com.kbopark.operation.entity.LedgerCheck;
import com.kbopark.operation.entity.LedgerOrder;
import com.kbopark.operation.enums.LedgerCreateStatusEnum;
import com.kbopark.operation.enums.LedgerStatusEnum;
import com.kbopark.operation.excelbean.LedgerOrderExcel;
import com.kbopark.operation.service.LedgerCheckService;
import com.kbopark.operation.service.LedgerOrderService;
import com.kbopark.operation.util.OperationExcelUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 分账核对账单
 *
 * @author pigx code generator
 * @date 2020-10-19 11:49:30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ledgercheck" )
@Api(value = "ledgercheck", tags = "分账核对账单管理")
public class LedgerCheckController {

    private final LedgerCheckService ledgerCheckService;

    private final LedgerOrderService ledgerOrderService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param ledgerCheck 分账核对账单
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('operation_ledgercheck_view')" )
    public R getLedgerCheckPage(Page page, LedgerCheck ledgerCheck) {
        return R.ok(ledgerCheckService.page(page, Wrappers.query(ledgerCheck)));
    }


    /**
     * 通过id查询分账核对账单
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_ledgercheck_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(ledgerCheckService.getById(id));
    }


    /**
     * 方式一，根据选中的分账订单生成分账核对账单
     * @param ledgerCheckDTO 分账核对账单
     * @return R
     */
    @ApiOperation(value = "根据选中的分账订单生成分账核对账单", notes = "根据选中的分账订单生成分账核对账单")
    @SysLog("根据选中的分账订单生成分账核对账单" )
    @PostMapping("/createByLedgerOrderIds")
    @PreAuthorize("@pms.hasPermission('operation_ledgerorder_create')" )
    public R createByLedgerOrderIds(@RequestBody LedgerCheckDTO ledgerCheckDTO) {
        return ledgerCheckService.createCheckByConsumerOrder(ledgerCheckDTO.getOrderIds());
    }

	/**
	 * 方式二，根据选中的商家和结算日期生成分账核对账单
	 * @param ledgerCheckDTO 分账核对账单
	 * @return R
	 */
	@ApiOperation(value = "根据选中的商家和结算日期生成分账核对账单", notes = "根据选中的商家和结算日期生成分账核对账单")
	@SysLog("根据选中的商家和结算日期生成分账核对账单" )
	@PostMapping("/createByTime")
	@PreAuthorize("@pms.hasPermission('operation_ledgercheck_create')" )
	public R createByTime(@RequestBody LedgerCheckDTO ledgerCheckDTO) {
		return ledgerCheckService.createCheckByMerchant(ledgerCheckDTO.getMerchantId(), ledgerCheckDTO.getDateTime());
	}

    /**
     * 修改分账核对账单
     * @param ledgerCheck 分账核对账单
     * @return R
     */
    @ApiOperation(value = "修改分账核对账单", notes = "修改分账核对账单")
    @SysLog("修改分账核对账单" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('operation_ledgercheck_edit')" )
    public R updateById(@RequestBody LedgerCheck ledgerCheck) {
        return R.ok(ledgerCheckService.updateById(ledgerCheck));
    }

    /**
     * 通过id删除分账核对账单
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除分账核对账单", notes = "通过id删除分账核对账单")
    @SysLog("通过id删除分账核对账单" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('operation_ledgercheck_del')" )
    public R removeById(@PathVariable Long id) {
        return R.ok(ledgerCheckService.removeById(id));
    }



	/**
	 * 分账订单审核更新
	 *
	 * @param ledgerAuditDTO
	 * @return
	 */
	@ApiOperation(value = "分账订单审核更新", notes = "分账订单审核更新")
	@SysLog("分账订单审核更新")
	@PostMapping("/auditUpdate")
	public R auditUpdate(@RequestBody LedgerAuditDTO ledgerAuditDTO) {
		return R.ok(ledgerCheckService.updateByAudit(ledgerAuditDTO));
	}


	/**
	 * 导出分账订单明细
	 * @param batchNo
	 */
	@ApiOperation(value = "导出分账订单明细", notes = "导出分账订单明细")
	@GetMapping("/exportExcel/{batchNo}")
	@Inner(value = false)
	public void exportExcel(@PathVariable("batchNo") String batchNo, HttpServletResponse response) {
		try {
			List<LedgerOrder> byLedgerBatchNo = ledgerOrderService.findByLedgerBatchNo(batchNo);
			List<LedgerOrderExcel> orderExcels = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(byLedgerBatchNo)){
				for (LedgerOrder order : byLedgerBatchNo) {
					LedgerOrderExcel ledgerOrderExcel = BeanUtil.copyProperties(order, LedgerOrderExcel.class);
					ledgerOrderExcel.setLedgerStatus(EnumUtil.fromString(LedgerStatusEnum.class, ledgerOrderExcel.getLedgerStatus()).getDescription());
					ledgerOrderExcel.setLedgerCreateStatus(EnumUtil.fromString(LedgerCreateStatusEnum.class, ledgerOrderExcel.getLedgerCreateStatus()).getDescription());
					orderExcels.add(ledgerOrderExcel);
				}
			}
			ArrayList<LedgerOrderExcel> ledgerOrderExcels = CollUtil.newArrayList(orderExcels);
			ExcelWriter bigWriter = ExcelUtil.getBigWriter();
			String title = "批次号"+batchNo+"订单明细";
			bigWriter.merge(19, title);
			OperationExcelUtil.addHeaderAlias(bigWriter);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
			response.setHeader("Content-Disposition","attachment;filename="+new String(title.getBytes("utf-8"), "ISO-8859-1")+".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();
			bigWriter.write(ledgerOrderExcels, true);
			SXSSFSheet sheet = (SXSSFSheet)bigWriter.getSheet();
			sheet.trackAllColumnsForAutoSizing();
			bigWriter.autoSizeColumnAll();
			bigWriter.flush(outputStream, true);
			bigWriter.close();
			IoUtil.close(outputStream);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
