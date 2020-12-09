package com.kbopark.operation.dto;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kbopark.operation.enums.CouponMiniTypeEnum;
import com.kbopark.operation.enums.CouponTypeEnum;
import com.kbopark.operation.enums.RedpackMiniTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class MemberCouponDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("会员头像")
	private String memberAvatar;
	@ApiModelProperty("会员真实姓名")
	private String memberRealname;
	@ApiModelProperty("会员昵称")
	private String memberNickname;
	@ApiModelProperty("会员手机号码")
	private String memberPhone;
	@ApiModelProperty("商家logo")
	private String merchantLogo;
	@ApiModelProperty("商家ID")
	private Integer merchantId;
	@ApiModelProperty("商家名称")
	private String merchantName;
	@ApiModelProperty("卡券名称/红包名称")
	private String couponName;
	@ApiModelProperty("类型，coupon卡券， redpack红包")
	private String type;
	@ApiModelProperty(value = "券编号")
	private String couponSerialNumber;
	@ApiModelProperty("卡券类型/红包类型")
	private Integer couponType;
	@ApiModelProperty("有效期开始时间")
	private String startTime;
	@ApiModelProperty("有效期结束时间")
	private String endTime;
	@ApiModelProperty("优惠券减去金额")
	private Double couponMoney;
	@ApiModelProperty("红包金额")
	private Double redpackMoney;
	@ApiModelProperty("满减金额")
	private String fullMoney;
	@ApiModelProperty("设置信息")
	private String setInfo;
	@ApiModelProperty("使用规则")
	private String ruleInfo;
	@ApiModelProperty("用户卡券关联id")
	private String couponReceiveId;
	@ApiModelProperty("领取时间")
	private String receiveTime;
	@ApiModelProperty("使用时间")
//	@DateTimeFormat(
//			pattern = "yyyy-MM-dd"
//	)
//	@JsonFormat(
//			pattern = "yyyy-MM-dd"
//	)
	private String usedTime;
	@ApiModelProperty("是否可用")
	private Boolean canUse;
	@JsonIgnore
	@ApiModelProperty("是否已经使用")
	private Boolean used;
	@JsonIgnore
	@ApiModelProperty("是否已经过期")
	private Boolean overdue;
	@JsonIgnore
	@ApiModelProperty("是否被锁定")
	private Boolean locked;

	@ApiModelProperty("简述")
	public String getDescription() {
		// 如果是优惠券
		if (CouponTypeEnum.COUPON_TYPE.getCode().equals(type)) {
			// 如果是满减券
			if (CouponMiniTypeEnum.FULL_CUT.getCode().equals(couponType)) {
				return StrUtil.format("满{}可用", fullMoney);
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

	@ApiModelProperty("状态")
	public String getStatus() {
		if (canUse) {
			return "未使用";
		} else if (used) {
			return "已使用";
		} else if (overdue) {
			return "已过期";
		} else {
			return "已失效";
		}
	}
}
