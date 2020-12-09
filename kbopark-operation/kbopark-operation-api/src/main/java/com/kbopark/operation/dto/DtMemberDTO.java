package com.kbopark.operation.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DtMemberDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String phone;
	private String nickName;
	private String avatar;

}
