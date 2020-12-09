package com.kbopark.operation.thirdplatform.entity;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/26 13:55
 **/
@Data
public class OrderSaveResult extends ResponsePublic {

	private String order_id;
	private String order_money;
	private String order_state;
	private String error_state;
	private String error_msg;

}
