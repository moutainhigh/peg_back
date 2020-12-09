package com.kbopark.operation.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/11 11:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubwayLineVO implements Serializable {

	private String id;

	private String parentId;

	private String text;

	private String code;

	private Double lng;

	private Double lat;


}
