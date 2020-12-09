package com.kbopark.operation.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于前端vant组件选择器
 */
@Data
public class VantPickerModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String text;

	private Object stuate;

	public VantPickerModel() {
	}

	public VantPickerModel(String text, Object stuate) {
		this.text = text;
		this.stuate = stuate;
	}
}
