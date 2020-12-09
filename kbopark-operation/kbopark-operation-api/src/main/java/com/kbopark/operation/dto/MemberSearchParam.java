package com.kbopark.operation.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberSearchParam implements Serializable {
	private static final long serialVersionUID = 1L;

	private String keyWord;

	private Integer operatorIdKey;

	private Integer distributorIdKey;

	private Integer merchantIdKey;

	private Integer memberId;
}
