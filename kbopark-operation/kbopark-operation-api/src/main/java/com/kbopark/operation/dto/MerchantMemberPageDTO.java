package com.kbopark.operation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantMemberPageDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("会员id")
	private Integer memberId;
	@ApiModelProperty("所属运营商id")
	private Integer operatorId;
	@ApiModelProperty("所属渠道商id")
	private Integer distributorId;
	@ApiModelProperty("所属商家id")
	private Integer merchantId;
	@ApiModelProperty("所属商家名称")
	private String merchantName;
	@ApiModelProperty("会员用户名")
	private String memberUsername;
	@ApiModelProperty("会员姓名（真实姓名）")
	private String memberRealName;
	@ApiModelProperty("会员昵称")
	private String memberNickname;
	@ApiModelProperty("会员头像")
	private String memberAvatar;
	@ApiModelProperty("会员手机号")
	private String memberPhone;
	@ApiModelProperty("会员性别")
	private String memberSex;
	@ApiModelProperty("会员邮箱")
	private String memberEmail;
	@ApiModelProperty("领取会员卡数量")
	private Integer vipCardNum;

}
