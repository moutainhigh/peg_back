package com.kbopark.operation.apidto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MonthNumberStatistics implements Serializable {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty("年")
	private Integer yearNum;
	@ApiModelProperty("月")
	private Integer monthNum;
	@ApiModelProperty("数量")
	private String number;
}
