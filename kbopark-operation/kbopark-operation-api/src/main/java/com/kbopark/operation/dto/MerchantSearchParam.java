package com.kbopark.operation.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantSearchParam implements Serializable {
	private static final long serialVersionUID = 1L;

	private String keyWords;

	private String reviewStatusKey;

	private Integer statusKey;

	private Integer operatorId;

	private Integer distributorId;

	private Integer promoterId;

	private String promoteCode;

	private String merchantName;
}
