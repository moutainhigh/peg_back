package com.kbopark.operation.entity;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 14:32
 **/
@Data
public class People {

	/**
	 * 必须真是姓名用于景区验证。
	 */
	private String link_man;
	/**
	 * 0 身份证 1 学生证 2 军官证 3 护照 4 户口本(儿童请选择 此项)
	 * 5 港澳通行证 6 台胞证 7 台湾通行证 8 入台证 9 香港居民往来内地通行证
	 * 10 警官证 11 驾驶证 12 海员证 13 外国人在中国永久居留证
	 */
	private Integer link_credit_type;
	/**
	 * 游玩人证件号码
	 */
	private String link_credit_no;
	/**
	 * 客人自定义信息
	 */
	private String fields;

}
