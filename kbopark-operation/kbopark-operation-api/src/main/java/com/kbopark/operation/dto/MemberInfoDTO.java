package com.kbopark.operation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kbopark.operation.entity.MemberInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MemberInfoDTO extends MemberInfo {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("会员卡数量")
	private Integer vipCardNum;

	@ApiModelProperty("优惠券数量")
	private Integer couponNum;

	@ApiModelProperty("红包数量")
	private Integer redpackNum;

}
