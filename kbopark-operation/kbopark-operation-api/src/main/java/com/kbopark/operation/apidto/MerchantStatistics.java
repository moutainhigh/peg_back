package com.kbopark.operation.apidto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MerchantStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("今日业绩")
	private TodayNumber todayNumber;
	@ApiModelProperty("近n个月会员")
	private List<MonthNumberStatistics> memberNumberList;
	@ApiModelProperty("近n个月订单")
	private List<MonthNumberStatistics> orderNumberList;
	@ApiModelProperty("近n个月收益")
	private List<MonthNumberStatistics> incomeList;

	@Data
	public static class TodayNumber implements Serializable {
		private static final long serialVersionUID = 1L;

		@ApiModelProperty("今日订单数量")
		private Integer orderNumber = 0;
		@JsonIgnore
		@ApiModelProperty("昨日订单数量")
		private Integer yesterdayOrderNumber = 0;

		@ApiModelProperty("今日收益")
		private Double money = 0D;
		@JsonIgnore
		@ApiModelProperty("昨日收益")
		private Double yesterdayMoney = 0D;

		@ApiModelProperty("今日会员数量")
		private Integer memberNumber = 0;
		@JsonIgnore
		@ApiModelProperty("昨日会员数量")
		private Integer yesterdayMemberNumber = 0;

		@ApiModelProperty("今日订单数与昨日差距")
		public Integer getOrderNumberDiff() {
			return orderNumber - yesterdayOrderNumber;
		}

		@ApiModelProperty("今日收益与昨日差距")
		public Double getMoneyDiff() {
			return money = yesterdayMoney;
		}

		@ApiModelProperty("今日会员数量与昨日差距")
		public Integer getMemberNumberDiff() {
			return memberNumber - yesterdayMemberNumber;
		}
	}

}
