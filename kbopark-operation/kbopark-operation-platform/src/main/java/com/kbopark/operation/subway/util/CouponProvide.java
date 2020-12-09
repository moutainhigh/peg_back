package com.kbopark.operation.subway.util;

import cn.hutool.core.date.DateUtil;
import com.kbopark.operation.entity.CouponReceive;
import com.kbopark.operation.entity.SubwayTicket;
import com.kbopark.operation.enums.EffectTypeEnum;
import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description: 向地铁申请发放乘车券参数
 * @Date: 2020/9/11 15:52
 **/
@Data
public class CouponProvide {

	/**
	 * 发券手机号码
	 */
	private String phone;
	/**
	 * 优惠券标识
	 * C2001：单次抵扣券
	 */
	private String categoryId;
	/**
	 * 优惠券金额
	 * 以分为单位
	 */
	private String amount;
	/**
	 * 发放张数
	 */
	private String couponNum;
	/**
	 * 生效时间yyyy-MM-dd
	 */
	private String beginDate;
	/**
	 * 过期时间yyyy-MM-dd
	 */
	private String endDate;
	/**
	 * 领取后有效天数
	 * 两种有效期选一项必填
	 */
	private String days;
	/**
	 * 签名串
	 */
	private String sign;


	public void setData(SubwayTicket subwayTicket, CouponReceive couponReceive){
		this.phone = couponReceive.getMemberPhone();
		this.categoryId = subwayTicket.getSubwayCode();
		Double value = subwayTicket.getValue();
		int v = value == null ? 0 : value.intValue() * 100;
		this.amount = String.valueOf(v);
		this.couponNum = couponReceive.getCouponNumber().toString();
		this.beginDate = DateUtil.format(couponReceive.getCouponStartTime(),"yyyy-MM-dd");
		this.endDate = DateUtil.format(couponReceive.getCouponEndTime(),"yyyy-MM-dd");
		if(EffectTypeEnum.BY_DAY.getCode().equals(subwayTicket.getEffectType())){
			this.days = subwayTicket.getEffectDay().toString();
		}
	}

}
