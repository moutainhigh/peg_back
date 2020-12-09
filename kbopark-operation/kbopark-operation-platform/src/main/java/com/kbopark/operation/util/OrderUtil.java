package com.kbopark.operation.util;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/3 10:29
 **/
public class OrderUtil {

	public static final Double defaultMaxNum = 100.0;
	public static final Double defaultMinNum = 0.0;
	public static final Integer scale = 2;
	public static final String SUB_ACCOUNT_PRE = "TDN";

	/**
	 * @Title:加法
	 * @Description:
	 * @Date: 2019/2/22 17:08
	 * @param:
	 * @return:
	 */
	public static double add(double d1,double d2){
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.add(b2).doubleValue();
	}


	/**
	 * @Title: 减法
	 * @Description:
	 * @Date: 2019/2/22 17:08
	 * @param:
	 * @return:
	 */
	public static double sub(double d1,double d2){
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.subtract(b2).doubleValue();
	}


	/**
	 * @Title:乘法
	 * @Description:
	 * @Date: 2019/2/22 17:08
	 * @param:
	 * @return:
	 */
	public static double mul(double d1,double d2){
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.multiply(b2).doubleValue();
	}


	/**
	 * @Title:除法
	 * @Description:
	 * @Date: 2019/2/22 17:09
	 * @param:
	 * @return:
	 */
	public static double div(double d1,double d2,int scale){
		if(scale < 0){
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double div(double d1,double d2){
		return div(d1, d2, scale);
	}

	/**
	 * 在指定范围内获取随机数并保留指定位数
	 *
	 * @param min   最小值
	 * @param max   最大值
	 * @param scale 精确位数
	 * @return 结果
	 */
	public static double getRandomDouble(double min, double max, int scale) {
		Random random = new Random();
		double v = random.nextDouble() * (max - min) + min;
		return new BigDecimal(v).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}


	/**
	 * @title:获取指定范围内的随机数
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/8/7 10:54
	 * @param:
	 * @return:
	 */
	public static int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();
	}


	/**
	 * @title:获取指定位数的随机数，可指定为纯数字或数字字母组合
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/8/7 10:53
	 * @param:
	 * @return:
	 */
	public static String getRandomCharAndNumber(Integer length, boolean isNumber) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			boolean b = random.nextBoolean();
			// 字符串
			if (b) {
				if (isNumber) {
					str += String.valueOf(random.nextInt(10));
				} else {
					// 取得大写字母
					str += (char) (65 + random.nextInt(26));
				}
			} else {
				// 数字
				str += String.valueOf(random.nextInt(10));
			}
		}
		return str;
	}


	/**
	 * @title:获取流水号
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/8/7 10:53
	 * @param:
	 * @return:
	 */
	public static String getSerialNumber(String suffix) {
		String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String t = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
		String s = getRandomCharAndNumber(4, true);
		String e = getRandomCharAndNumber(4, true);
		if (StringUtils.isBlank(suffix)) {
			return d + s + t + e;
		} else {
			return suffix + d + s + t + e;
		}
	}


	/**
	 * 过滤处理
	 *
	 * @param ruleInfo
	 * @return
	 */
	public static List<TicketRule> collectFilter(String ruleInfo){
		List<TicketRule> result = new ArrayList<>();
		List<TicketRule> ticketRules = JSONArray.parseArray(ruleInfo, TicketRule.class);
		Map<Integer, List<TicketRule>> collect = ticketRules.stream().collect(Collectors.groupingBy(TicketRule::getCouponId));
		for (Map.Entry<Integer,List<TicketRule>> map : collect.entrySet()) {
			Integer key = map.getKey();
			List<TicketRule> value = map.getValue();
			TicketRule ticketRule = new TicketRule();
			IntSummaryStatistics summary = value.stream().collect(Collectors.summarizingInt(TicketRule::getCouponNum));
			Long sum = summary.getSum();
			ticketRule.setCouponId(key);
			ticketRule.setCouponNum(sum.intValue());
			result.add(ticketRule);
		}
		return result;
	}

}
