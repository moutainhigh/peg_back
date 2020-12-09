package com.kbopark.operation.thirdplatform.entity;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:增值项目详细信息
 * @Date: 2020/10/26 16:19
 **/
@Data
public class Cond {

	private String condId;

	private String condTitle;

	private Integer isSync;

	private Integer isGotta;

	private Integer isApiece;

	private Double marketPrice;

	private Double salePrice;

	private Integer treeId;

	private String unit;


}
