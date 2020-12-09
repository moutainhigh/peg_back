package com.kbopark.operation.dto;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kbopark.operation.enums.CouponMiniTypeEnum;
import com.kbopark.operation.enums.CouponTypeEnum;
import com.kbopark.operation.enums.RedpackMiniTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantCouponUsedDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("会员头像")
	private String memberAvatar;
	@ApiModelProperty("会员真实姓名")
	private String memberRealname;
	@ApiModelProperty("会员昵称")
	private String memberNickname;
	@ApiModelProperty("会员手机号码")
	private String memberPhone;
	@ApiModelProperty("使用事件")
	private String useTime;
	@JsonIgnore
	@ApiModelProperty("卡券金额")
	private Double couponMoney;
	@JsonIgnore
	@ApiModelProperty("卡券金额")
	private Double couponFullMoney;
	@JsonIgnore
	@ApiModelProperty("红包金额")
	private Double redpackMoney;
	@ApiModelProperty("卡券/红包使用规则")
	private String setInfo;
	@ApiModelProperty("卡券/红包名称")
	private String couponName;
	@JsonIgnore
	@ApiModelProperty("卡券类型，是红包还是优惠券")
	private String type;
	@ApiModelProperty("卡券类型/红包类型")
	private Integer couponType;
	@ApiModelProperty("")
	private String id;

	@ApiModelProperty("简述")
	public String getDescription() {
		// 如果是优惠券
		if (CouponTypeEnum.COUPON_TYPE.getCode().equals(type)) {
			// 如果是满减券
			if (CouponMiniTypeEnum.FULL_CUT.getCode().equals(couponType)) {
				return StrUtil.format("满{}可用", couponFullMoney);
			} else {
				return "";
			}
		}
		// 如果是红包
		else {
			return StrUtil.format("{}红包", RedpackMiniTypeEnum.codeOf(couponType).getDescription());
		}
	}

	@ApiModelProperty("金额")
	public Double getMoney() {
		// 如果是优惠券
		if (CouponTypeEnum.COUPON_TYPE.getCode().equals(type)) {
			return couponMoney;
		}
		// 如果是红包
		else {
			return redpackMoney;
		}
	}

}
