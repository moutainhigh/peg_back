package com.kbopark.operation.util;

import com.kbopark.operation.enums.CouponTypeEnum;
import com.kbopark.operation.vo.CouponInfoVO;
import com.kbopark.operation.vo.MerchantInfoVO;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/18 15:56
 **/
public class CouponUtil {

	/***
	 * 默认展示距离5000米
	 */
	public static final int DEFAULT_DISTANCE = 5000;

	/***
	 * 默认展示数量
	 */
	public static final int DEFAULT_NUM = 10;

	/***
	 * 默认提取商家券数量
	 */
	public static final int DEFAULT_FILTER_NUM = 2;

	/***
	 * 根据活动设置的距离过滤优惠券红包
	 * @param couponInfoVOList    数据
	 * @param couponDistance    优惠券活动距离
	 * @param redPackDistance    红包活动距离
	 * @return
	 */
	public static List<CouponInfoVO> filterCouponByDistance(List<CouponInfoVO> couponInfoVOList, Double couponDistance, Double redPackDistance) {
		if (CollectionUtils.isEmpty(couponInfoVOList)) {
			return null;
		}
		List<CouponInfoVO> filterList = new ArrayList<>();
		for (CouponInfoVO vo : couponInfoVOList) {
			Double distance = vo.getDistance();
			if (CouponTypeEnum.COUPON_TYPE.getCode().equals(vo.getType())) {
				if (distance < couponDistance) {
					filterList.add(vo);
				}
			} else if (CouponTypeEnum.RED_PACK_TYPE.getCode().equals(vo.getType())) {
				if (distance < redPackDistance) {
					filterList.add(vo);
				}
			}
		}
		return filterList;
	}


	/**
	 * 根据活动设置的数量过滤优惠券和红包
	 *
	 * @param couponInfoVOList
	 * @param couponNumber
	 * @param redPackNumber
	 * @return
	 */
	public static List<CouponInfoVO> filterCouponByNumber(List<CouponInfoVO> couponInfoVOList, Double couponNumber, Double redPackNumber) {
		if (CollectionUtils.isEmpty(couponInfoVOList)) {
			return null;
		}
		List<CouponInfoVO> filterList = new ArrayList<>();
		//第一次分组按券类型，分组红包和优惠券的数据
		Map<String, List<CouponInfoVO>> collect = couponInfoVOList.stream().collect(Collectors.groupingBy(CouponInfoVO::getType));
		for (Map.Entry<String, List<CouponInfoVO>> map : collect.entrySet()) {
			String key = map.getKey();
			List<CouponInfoVO> value = map.getValue();
			//第二次分组按商家，商家如果发布了多条优惠券或者红包需要过滤显示
			Map<Integer, List<CouponInfoVO>> itemMap = value.stream().collect(Collectors.groupingBy(CouponInfoVO::getMerchantId));
			List<CouponInfoVO> valueItemList = new ArrayList<>();
			for (Map.Entry<Integer, List<CouponInfoVO>> item : itemMap.entrySet()) {
				List<CouponInfoVO> itemValue = item.getValue();
				if (itemValue.size() > DEFAULT_FILTER_NUM) {
					valueItemList.addAll(itemValue.subList(0, DEFAULT_FILTER_NUM));
				} else {
					valueItemList.addAll(itemValue);
				}
			}

			if (CouponTypeEnum.COUPON_TYPE.getCode().equals(key)) {
				if (valueItemList.size() > couponNumber) {
					filterList.addAll(valueItemList.subList(0, couponNumber.intValue()));
				} else {
					filterList.addAll(valueItemList);
				}
			}
			if (CouponTypeEnum.RED_PACK_TYPE.getCode().equals(key)) {
				if (valueItemList.size() > redPackNumber) {
					filterList.addAll(valueItemList.subList(0, couponNumber.intValue()));
				} else {
					filterList.addAll(valueItemList);
				}
			}
		}

		return filterList;
	}


	/**
	 * 根据活动设置参数过滤商家信息
	 *
	 * @param merchantInfoVOList 商家信息列表
	 * @param merchantDistance   活动设置商家距离
	 * @param merchantNumber     活动设置商家数量
	 * @return
	 */
	public static List<MerchantInfoVO> filterMerchantByDistance(List<MerchantInfoVO> merchantInfoVOList, Double merchantDistance, Double merchantNumber) {
		if (CollectionUtils.isEmpty(merchantInfoVOList)) {
			return null;
		}
		List<MerchantInfoVO> filterList = new ArrayList<>();
		for (MerchantInfoVO vo : merchantInfoVOList) {
			Double distance = vo.getDistance();
			if (distance < merchantDistance) {
				filterList.add(vo);
			}
		}
		if (filterList.size() > merchantNumber) {
			return filterList.subList(0, merchantNumber.intValue());
		} else {
			return filterList;
		}
	}


	/***
	 * 随机获取三个优惠券
	 * @param data
	 * @return
	 */
	public static List<CouponInfoVO> getRandomCouponList(List<CouponInfoVO> data) {
		List<CouponInfoVO> resultData = new ArrayList<>();
		if (!CollectionUtils.isEmpty(data)) {
			int size = data.size();
			int num = 3;
			if (size > num) {
				Collections.shuffle(data);
				resultData = data.subList(0, num);
			} else {
				resultData = data;
			}
		}
		return resultData;
	}
}
