package com.kbopark.operation.util;

import cn.hutool.poi.excel.ExcelWriter;
import lombok.experimental.UtilityClass;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/20 17:23
 **/
@UtilityClass
public class OperationExcelUtil {


	public void addHeaderAlias(ExcelWriter bigWriter){
		bigWriter.addHeaderAlias("ledgerBatchNo","分账批次号");
		bigWriter.addHeaderAlias("ledgerStatus","分账状态");
		bigWriter.addHeaderAlias("operationName","运营商名称");
		bigWriter.addHeaderAlias("operationPercent","运营商分账比例（%）");
		bigWriter.addHeaderAlias("operationLedgerAccount","运营商分账金额（元）");
		bigWriter.addHeaderAlias("platformName","平台名称");
		bigWriter.addHeaderAlias("platformPercent","平台分账比例（%）");
		bigWriter.addHeaderAlias("platformLegerAccount","平台分账金额（元）");
		bigWriter.addHeaderAlias("merchantName","商家名称");
		bigWriter.addHeaderAlias("merchantPercent","商家分账比例（%）");
		bigWriter.addHeaderAlias("merchantLegerAccount","商家分账金额（元）");
		bigWriter.addHeaderAlias("orderNumber","消费订单号");
		bigWriter.addHeaderAlias("payable","应付金额（元）");
		bigWriter.addHeaderAlias("discount","折扣金额（元）");
		bigWriter.addHeaderAlias("money","实付金额（元）");
		bigWriter.addHeaderAlias("ledgerTime","分账日期");
		bigWriter.addHeaderAlias("ledgerCreateStatus","核对账单生成状态 ");
		bigWriter.addHeaderAlias("createTime","创建时间");
		bigWriter.addHeaderAlias("createBy","创建人姓名");
	}

}
