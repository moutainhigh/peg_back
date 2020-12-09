/*
 *    Copyright (c) 2018-2025, kbopark All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: kbopark
 */

package com.kbopark.operation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 商家资金表
 *
 * @author laosmt
 * @date 2020-10-29 09:41:02
 */
@Getter
@TableName("kboparkx_merchant_balance")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商家资金表")
public class MerchantBalance extends Model<MerchantBalance> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer merchantId;
	/**
	 * 可用资金
	 */
	@ApiModelProperty(value = "可用资金")
	private BigDecimal usableBalance;
	/**
	 * 冻结资金
	 */
	@ApiModelProperty(value = "冻结资金")
	private BigDecimal freezeBalance;
	/**
	 * 待入账资金
	 */
	@ApiModelProperty(value = "待入账资金")
	private BigDecimal toAccountBalance;
	/**
	 *
	 */
	@Version
	@Setter
	@ApiModelProperty(value = "")
	private Integer version;
	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer delFlag;

	@DateTimeFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private LocalDateTime createTime;

	@DateTimeFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private LocalDateTime updateTime;

	public static MerchantBalance init4Merchant(Integer merchantId) {
		MerchantBalance merchantBalance = new MerchantBalance();
		merchantBalance.merchantId = merchantId;
		return merchantBalance;
	}

	// 增加待入账金额
	public void addToAccount(Double money) throws UnsupportedOperationException {
		Objects.requireNonNull(money, "必须传入待入账金额");
		addToAccount(BigDecimal.valueOf(money));
	}
	public void addToAccount(BigDecimal money) throws UnsupportedOperationException {
		Objects.requireNonNull(money, "必须传入待入账金额");
		if (money.compareTo(BigDecimal.valueOf(0L)) < 0) {
			throw new UnsupportedOperationException("待入账金额必须大于0元");
		}
		toAccountBalance = toAccountBalance.add(money);
	}

	// 入账，从待入账资金把资金转入入账资金
	public void toAccount(Double money) throws UnsupportedOperationException {
		Objects.requireNonNull(money, "必须传入入账金额");
		toAccount(BigDecimal.valueOf(money));
	}
	public void toAccount(BigDecimal money) throws UnsupportedOperationException {
		Objects.requireNonNull(money, "必须传入入账金额");
		if (money.compareTo(BigDecimal.valueOf(0L)) < 0) {
			throw new UnsupportedOperationException("入账资金必须大于0");
		}
		if (toAccountBalance.compareTo(money) < 0) {
			throw new UnsupportedOperationException("待入账金额不足，无法入账");
		}
		toAccountBalance = toAccountBalance.subtract(money);
		usableBalance = usableBalance.add(money);
	}

	// 冻结资金，把资金从
	public void freeze(Double money) throws UnsupportedOperationException {
		Objects.requireNonNull(money, "必须传入入账金额");
		freeze(BigDecimal.valueOf(money));
	}
	public void freeze(BigDecimal money) throws UnsupportedOperationException {
		Objects.requireNonNull(money, "必须传入入账金额");
		if (money.compareTo(BigDecimal.valueOf(0L)) < 0) {
			throw new UnsupportedOperationException("冻结资金必须大于0");
		}
		if (usableBalance.compareTo(money) < 0) {
			throw new UnsupportedOperationException("可用资金不足，无法完成冻结");
		}
		usableBalance = usableBalance.subtract(money);
		freezeBalance = freezeBalance.add(money);
	}
}
