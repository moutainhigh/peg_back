package com.kbopark.operation.util;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/11 11:23
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SubwaySiteTree extends TreeNode implements Serializable {

	/**名称**/
	private String text;
	/**编号**/
	private String code;
	/**经度**/
	private Double lng;
	/**纬度**/
	private Double lat;


}
