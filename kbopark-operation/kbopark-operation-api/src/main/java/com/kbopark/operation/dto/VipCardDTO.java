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

package com.kbopark.operation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author laomst
 * @date 2020-08-31 16:25:21
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VipCardDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 所属运营商
	 */
	@ApiModelProperty(value = "所属运营商")
	private Integer operatorId;
	/**
	 * 所属渠道商
	 */
	@ApiModelProperty(value = "所属渠道商")
	private Integer distributorId;
	/**
	 * 商家名称
	 */
	@ApiModelProperty(value = "商家名称")
	private String merchantName;

	@ApiModelProperty("商家id")
	private Integer merchantId;
	/**
	 * 会员卡数量
	 */
	@ApiModelProperty(value = "优惠券数量")
	private Integer couponNum;
	/**
	 * 红包数量
	 */
	@ApiModelProperty(value = "红包数量")
	private Integer redPacketNum;

	private Integer memberId;

	@ApiModelProperty("商家logo")
	private String merchantLogo;

	@ApiModelProperty("余额")
	private BigDecimal balance;

	@ApiModelProperty("商家VipColor")
	private String merchantVipColor;
}
