package com.kbopark.operation.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConsumerStrategySearchParam implements Serializable {
	private static final long serialVersionUID = 1L;

	private String keyWord;

	private Integer merchantIdKey;

	private Integer statusKey;
}
