package com.kbopark.operation.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/8 11:31
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionQ {
	private Double minLng;
	private Double maxLng;
	private Double minLat;
	private Double maxLat;
}
