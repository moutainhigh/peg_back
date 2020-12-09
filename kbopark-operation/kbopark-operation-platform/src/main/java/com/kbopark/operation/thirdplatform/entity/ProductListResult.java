package com.kbopark.operation.thirdplatform.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/26 13:55
 **/
@Data
public class ProductListResult extends ResponsePublic {


	private List<ProductInfo> products;

}
