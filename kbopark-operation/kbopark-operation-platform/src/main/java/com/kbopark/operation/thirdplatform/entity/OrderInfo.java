package com.kbopark.operation.thirdplatform.entity;

import com.kbopark.operation.entity.CondValue;
import com.kbopark.operation.entity.Detail;
import com.kbopark.operation.entity.People;
import com.kbopark.operation.entity.Prod;
import lombok.Data;

import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 14:25
 **/
@Data
public class OrderInfo {

	private String travel_date;
	private String end_travel_date;
	private String arrived_time;
	private String info_id;
	private String cust_id;
	private String get_type;
	private String order_source_id;
	private String seat;
	private String order_memo;
	private Integer num;
	private String user_id;
	private String link_man;
	private String link_phone;
	private String link_email;
	private String link_address;
	private String linkCode;
	private String link_credit_type;
	private String link_credit_no;
	private String fields;
	private String spec_name;

	/**
	 * 参数其他联系人节点
	 */
	private List<People> peoples;

	/**
	 * 参数增值项目节点
	 */
	private List<CondValue> conds;

	/**
	 * 参数打包产品子产品节点
	 */
	private List<Prod> prods;

	/**
	 * 参数规格节点
	 */
	private List<Detail> details;

}
