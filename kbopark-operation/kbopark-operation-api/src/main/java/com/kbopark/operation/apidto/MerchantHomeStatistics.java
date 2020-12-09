package com.kbopark.operation.apidto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class MerchantHomeStatistics implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("可用余额")
	private Double usableBalance;

	@ApiModelProperty("冻结余额")
	private Double freezeBalance;

	@ApiModelProperty("待入账余额")
	private Double toAccountBalance;

	@ApiModelProperty("全部收益")
	private Double allIncome;

	private List<IncomeInfo> incomeList;

	@ApiModelProperty("优惠券数量详情")
	private CouponNumber couponNumberInfo;

	@ApiModelProperty("红包数量详情")
	private CouponNumber redpackNumberInfo;


	public void appendIncomeInfo(String text, Double money) {
		if (null == incomeList) {
			incomeList = new ArrayList<>(6);
		}
		incomeList.add(new IncomeInfo(text, money));
	}


	@ApiModelProperty("全部资金")
	public Double getTotalBalance() {
		Double usable = Optional.ofNullable(usableBalance).orElse(0D);
		Double freeze = Optional.ofNullable(freezeBalance).orElse(0D);
		Double toAccount = Optional.ofNullable(toAccountBalance).orElse(0D);
		return usable + freeze + toAccount;
	}

	@Data
	public static class IncomeInfo implements Serializable {
		private static final long serialVersionUID = 1L;

		@ApiModelProperty("说明")
		private String text;

		@ApiModelProperty("金额")
		private Double money;

		public IncomeInfo(String text, Double money) {
			this.text = text;
			this.money = money;
		}
	}

	@Data
	public static class CouponNumber implements Serializable {
		private static final long serialVersionUID = 1L;

		@ApiModelProperty("已经被领取的数量")
		private Integer receivedNumber;

		@ApiModelProperty("已经被使用的数量")
		private Integer usedNumber;

		public CouponNumber(Integer receivedNumber, Integer usedNumber) {
			this.receivedNumber = receivedNumber;
			this.usedNumber = usedNumber;
		}
	}


}
