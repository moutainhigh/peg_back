package com.kbopark.operation.api.app;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.api.service.ConsumerApiService;
import com.kbopark.operation.dto.CouponQueryDTO;
import com.kbopark.operation.dto.CouponReceiveDTO;
import com.kbopark.operation.dto.CreateOrderDTO;
import com.kbopark.operation.dto.GpsInfoDTO;
import com.kbopark.operation.entity.*;
import com.kbopark.operation.enums.CouponTypeEnum;
import com.kbopark.operation.service.*;
import com.kbopark.operation.subway.util.HttpConfig;
import com.kbopark.operation.util.CouponUtil;
import com.kbopark.operation.util.GpsUtil;
import com.kbopark.operation.util.OperationConstants;
import com.kbopark.operation.vo.CouponInfoVO;
import com.kbopark.operation.vo.MerchantCouponVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/8 9:47
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/consumer")
@Api(value = "consumer", tags = "【消费者接口】")
public class ConsumerController {

	private final ConsumerOrderService consumerOrderService;

	private final CouponInfoService couponInfoService;

	private final MemberInfoService memberInfoService;

	private final CouponReceiveService couponReceiveService;

	private final MerchantWelfareSettingService welfareSettingService;

	private final MerchantConsumerStrategyService consumerStrategyService;

	private final ConsumerStrategyPrizeService strategyPrizeService;

	private final ConsumerApiService consumerApiService;

	/**
	 * 创建消费订单
	 *
	 * @param createOrderDTO 消费订单
	 * @return R
	 */
	@ApiOperation(value = "创建消费订单", notes = "创建消费订单")
	@SysLog("创建消费订单")
	@PostMapping("/create")
	@Inner(value = false)
	public R createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO) {
		return consumerOrderService.createOrder(createOrderDTO);
	}


	/**
	 * 用户取消支付（订单号带银联前缀）
	 *
	 * @param orderNumber
	 * @return
	 */
	@ApiOperation(value = "用户取消支付（订单号带银联前缀）", notes = "用户取消支付")
	@GetMapping("/cancel/{orderNumber}")
	@Inner(value = false)
	public R cancelPayOrder(@PathVariable("orderNumber") String orderNumber) {
		return consumerOrderService.cancelPayOrder(orderNumber, true);
	}


	/***
	 * 检测当前用户是否存在未支付订单
	 *
	 * @return
	 */
	@ApiOperation(value = "检测当前用户是否存在未支付订单", notes = "检测当前用户是否存在未支付订单")
	@GetMapping("/checkOrder")
	public R checkPayOrder() {
		return consumerOrderService.checkPayOrder();
	}


	/**
	 * 广告位置弹出券，根据经纬度查询优惠券列表
	 *
	 * @param couponQueryDTO 经纬度信息
	 * @return
	 */
	@ApiOperation(value = "获取广告位置优惠券列表", notes = "获取广告位置优惠券列表")
	@SysLog("获取广告位置优惠券列表")
	@PostMapping("/couponAdvertList")
	public R<List<CouponInfoVO>> couponListRandom(@Valid @RequestBody CouponQueryDTO couponQueryDTO) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		couponQueryDTO.setCurrent(1);
		couponQueryDTO.setSize(20);
		// 显示红包和卡券
		couponQueryDTO.setCouponType(null);
		List<CouponInfoVO> data = couponInfoService.couponAdvertList(couponQueryDTO, currentMember.getPhone());
		List<CouponInfoVO> randomCouponList = CouponUtil.getRandomCouponList(data);
		return R.ok(randomCouponList);
	}


	/**
	 * 获取两个经纬度之间的直线距离
	 *
	 * @param gpsInfoDTO
	 * @return
	 */
	@ApiOperation(value = "获取两个经纬度之间的直线距离", notes = "获取两个经纬度之间的直线距离")
	@SysLog("获取两个经纬度之间的直线距离")
	@PostMapping("/getDistance")
	@Inner(value = false)
	public R getDistance(@Valid @RequestBody GpsInfoDTO gpsInfoDTO) {
		return R.ok(GpsUtil.getDistance(gpsInfoDTO.getCurrentLat(), gpsInfoDTO.getCurrentLng(), gpsInfoDTO.getTargetLat(), gpsInfoDTO.getTargetLng()));
	}


	/**
	 * 根据经纬度查询优惠券列表
	 *
	 * @param couponQueryDTO 经纬度信息
	 * @return
	 */
	@ApiOperation(value = "获取站点优惠券列表", notes = "获取站点优惠券列表")
	@SysLog("获取站点优惠券列表")
	@PostMapping("/couponList")
	public R couponList(@Valid @RequestBody CouponQueryDTO couponQueryDTO) {
		couponQueryDTO.setCouponType(StrUtil.isBlank(couponQueryDTO.getCouponType())? "coupon": couponQueryDTO.getCouponType());
		Page<CouponInfoVO> couponList = couponInfoService.getCouponList(couponQueryDTO, null);
		return R.ok(couponList);
	}


	/**
	 * 根据经纬度查询商家列表
	 *
	 * @param couponQueryDTO 经纬度信息
	 * @return
	 */
	@ApiOperation(value = "获取商家列表", notes = "获取商家列表")
	@SysLog("获取商家列表")
	@PostMapping("/merchantList")
	@Inner(value = false)
	public R merchantList(@Valid @RequestBody CouponQueryDTO couponQueryDTO) {
		return couponInfoService.getMerchantList(couponQueryDTO);
	}


	/**
	 * 同步会员信息
	 *
	 * @param memberInfo 同步会员信息
	 * @return
	 */
	@ApiOperation(value = "同步会员信息", notes = "同步会员信息")
	@SysLog("同步会员信息")
	@PostMapping("/setMemberInfo")
	@Inner(value = false)
	public R setMemberInfo(@RequestBody MemberInfo memberInfo) {
		MemberInfo byMobile = memberInfoService.findByMobile(memberInfo.getPhone());
		if (byMobile != null) {
			memberInfo.setId(byMobile.getId());
		} else {
			memberInfo.setSerialNumber(RandomUtil.randomString(32));
		}
		return R.ok(memberInfoService.saveOrUpdate(memberInfo));
	}


	/**
	 * 会员获取乘车券领取记录
	 *
	 * @return
	 */
	@ApiOperation(value = "获取乘车券领取记录", notes = "获取乘车券领取记录")
	@SysLog("获取乘车券领取记录")
	@PostMapping("/ticketList")
	public R getSubwayTicketRecord() {
		KboparkUser user = SecurityUtils.getUser();
		String phone = user.getPhone();
		return couponReceiveService.getTicketRecord(phone, CouponTypeEnum.SUBWAY_TICKET_TYPE.getCode());
	}


	/**
	 * 支付页获取商家福利配置
	 *
	 * @param welfareSetting 设置商家ID
	 * @return
	 */
	@ApiOperation(value = "支付页获取商家福利配置", notes = "支付页获取商家福利配置")
	@SysLog("支付页获取商家福利配置")
	@PostMapping("/pay-success-getWelfareSetting")
	@Inner(value = false)
	public R<MerchantWelfareSetting> paySuccessGetWelfareSetting(@RequestBody MerchantWelfareSetting welfareSetting) {
		if (StringUtils.isBlank(welfareSetting.getMerOrderId())) {
			return R.failed("未获取到订单号");
		}
		String orderNumber = welfareSetting.getMerOrderId();
		final String finalOrderNumber = orderNumber.substring(4);
		ConsumerOrder order = consumerOrderService.getOne(w -> w.lambda().eq(ConsumerOrder::getOrderNumber, finalOrderNumber));
		return R.ok(welfareSettingService.getOne(Wrappers.<MerchantWelfareSetting>query().lambda()
				.eq(MerchantWelfareSetting::getMerchantId, order.getMerchantId())
				.le(null != welfareSetting.getFullMoney(), MerchantWelfareSetting::getFullMoney, welfareSetting.getFullMoney())
				.orderByDesc(MerchantWelfareSetting::getFullMoney, MerchantWelfareSetting::getWeight)
				.last("limit 1")));
	}

	@ApiOperation(value = "支付页获取商家福利配置", notes = "支付页获取商家福利配置")
	@SysLog("支付页获取商家福利配置")
	@PostMapping("/getWelfareSetting")
	@Inner(value = false)
	public R getWelfareSetting(@RequestBody MerchantWelfareSetting welfareSetting) {
		if(welfareSetting.getMerchantId() == null){
			return R.failed("商家ID不能为空");
		}
		return R.ok(welfareSettingService.list(Wrappers.query(welfareSetting).lambda().orderByDesc(MerchantWelfareSetting::getWeight)));
	}


	/**
	 * 支付页获取福利乘车券
	 *
	 * @param couponReceiveDTO
	 * @return
	 */
	@ApiOperation(value = "支付页获取福利乘车券", notes = "支付页获取福利乘车券")
	@SysLog("支付页获取福利乘车券")
	@PostMapping("/welfareTicket")
	@Inner(value = false)
	public R getTicketsAfterPay(@RequestBody CouponReceiveDTO couponReceiveDTO) {
		if (StringUtils.isBlank(couponReceiveDTO.getOrderNumber())) {
			return R.failed("未获取到订单号");
		}
		String orderNumber = couponReceiveDTO.getOrderNumber();
		orderNumber = orderNumber.substring(4);
		QueryWrapper<CouponReceive> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(CouponReceive::getOrderNumber, orderNumber);
		return R.ok(couponReceiveService.list(queryWrapper));
	}


//	/***
//	 * 支付页获取商家消费策略
//	 *
//	 * @return
//	 */
//	@ApiOperation(value = "支付页获取商家消费策略", notes = "支付页获取商家消费策略")
//	@SysLog("支付页获取商家消费策略")
//	@GetMapping("/getConsumerStrategy")
//	@Inner(value = false)
//	public R<List<MerchantConsumerStrategy>> getConsumerStrategy(@RequestParam("merchantId") Integer merchantId, @RequestParam("money") Double money) {
//		List<MerchantConsumerStrategy> list = consumerStrategyService.list(w -> w.lambda().eq(MerchantConsumerStrategy::getMerchantId, merchantId)
//				.ge(MerchantConsumerStrategy::getMoney, money));
//		list.forEach(item -> {
//			item.setPrizeList(strategyPrizeService.list(w -> w.lambda().eq(ConsumerStrategyPrize::getStrategyId, item.getId())));
//		});
//		return R.ok(list);
//		QueryWrapper<MerchantConsumerStrategy> queryWrapper = new QueryWrapper<>();
//		queryWrapper.lambda().eq(MerchantConsumerStrategy::getMerchantId, consumerStrategy.getMerchantId());
//		List<MerchantConsumerStrategy> list = consumerStrategyService.list(queryWrapper);
//		if(CollectionUtils.isEmpty(list)){
//			return R.ok();
//		}
//		for (MerchantConsumerStrategy strategy : list) {
//			QueryWrapper<ConsumerStrategyPrize> query = new QueryWrapper<>();
//			query.lambda().eq(ConsumerStrategyPrize::getStrategyId, strategy.getId());
//			List<ConsumerStrategyPrize> strategyPrizes = strategyPrizeService.list(query);
//			strategy.setPrizeList(strategyPrizes);
//			Map<String, List<ConsumerStrategyPrize>> collect = strategyPrizes.stream().collect(Collectors.groupingBy(ConsumerStrategyPrize::getPrizeType));
//			for (Map.Entry<String, List<ConsumerStrategyPrize>> map : collect.entrySet()) {
//				String key = map.getKey();
//				if(CouponTypeEnum.COUPON_TYPE.getCode().equals(key)){
//					strategy.setCouponList(map.getValue());
//				}
//				if(CouponTypeEnum.RED_PACK_TYPE.getCode().equals(key)){
//					strategy.setRedpackList(map.getValue());
//				}
//			}
//		}
//
//		return R.ok(list);
//	}

	@ApiOperation(value = "支付页获取商家消费策略", notes = "支付页获取商家消费策略")
	@SysLog("支付页获取商家消费策略")
	@PostMapping("/getConsumerStrategy")
	public R<List<MerchantCouponVO>> getConsumerStrategy(@RequestBody MerchantConsumerStrategy consumerStrategy) {
		MemberInfo currentMember;
		try {
			currentMember = getMember(SecurityUtils.getUser());
		} catch (UnsupportedOperationException ex) {
			return R.failed(ex.getMessage());
		}
		if (StringUtils.isBlank(consumerStrategy.getMerOrderId())) {
			return R.failed("未获取到订单号");
		}
		String orderNumber = consumerStrategy.getMerOrderId();
		final String finalOrderNumber = orderNumber.substring(4);
		ConsumerOrder order = consumerOrderService.getOne(w -> w.lambda().eq(ConsumerOrder::getOrderNumber, finalOrderNumber));
		QueryWrapper<MerchantConsumerStrategy> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(MerchantConsumerStrategy::getMerchantId, order.getMerchantId())
				.le(null != consumerStrategy.getMoney(), MerchantConsumerStrategy::getMoney, consumerStrategy.getMoney())
				.orderByDesc(MerchantConsumerStrategy::getMoney, MerchantConsumerStrategy::getWeight)
				.last("limit 1");
		MerchantConsumerStrategy one = consumerStrategyService.getOne(queryWrapper);
		if (null == one) {
			return R.ok(Collections.emptyList());
		}
		List<ConsumerStrategyPrize> prizeList = strategyPrizeService.list(w -> w.lambda().eq(ConsumerStrategyPrize::getStrategyId, one.getId()));
		if (CollectionUtil.isEmpty(prizeList)) {
			return R.ok(Collections.emptyList());
		}
		List<Integer> ids = prizeList.stream().map(ConsumerStrategyPrize::getCouponId).collect(Collectors.toList());
		List<MerchantCouponVO> canReceiveList = couponInfoService.getCanReceiveList(ids, currentMember.getPhone(), consumerStrategy.getMerchantId());
		return R.ok(canReceiveList);
	}


	/**
	 * 根据消费订单号获取待领取的福利优惠券
	 *
	 * @param couponReceiveDTO
	 * @return
	 */
	@ApiOperation(value = "支付页获取待赠送优惠券", notes = "支付页获取待赠送优惠券")
	@SysLog("支付页获取待赠送优惠券")
	@PostMapping("/welfareCoupons")
	@Inner(value = false)
	public R getCouponsAfterPay(@RequestBody CouponReceiveDTO couponReceiveDTO) {
		if (StringUtils.isBlank(couponReceiveDTO.getOrderNumber())) {
			return R.failed("订单号不能为空");
		}
		String orderNumber = couponReceiveDTO.getOrderNumber().substring(4);
		ConsumerOrder byOrderNumber = consumerOrderService.findByOrderNumber(orderNumber);
		if (byOrderNumber == null) {
			return R.failed(OperationConstants.NOTIFY_ORDER_NO_DATA);
		}
		return R.ok(consumerApiService.getWelfareCouponByOrder(byOrderNumber));
	}


	/**
	 * 支付成功通过单号和选中的优惠券编号领取赠送的优惠券
	 *
	 * @param couponReceiveDTO
	 * @return
	 */
	@ApiOperation(value = "支付页通过单号领取赠送的优惠券", notes = "支付页通过单号领取赠送的优惠券")
	@SysLog("支付页通过单号领取赠送的优惠券")
	@PostMapping("/receiveByOrder")
	@Inner(value = false)
	public R receiveByOrder(@RequestBody CouponReceiveDTO couponReceiveDTO) {
		return consumerApiService.handlerReceive(couponReceiveDTO);
	}


	private MemberInfo getMember(KboparkUser currentUser) {
		if (null == currentUser) {
			throw new UnsupportedOperationException("获取用户信息失败");
		}
		MemberInfo member = memberInfoService.getOne(w -> {
			w.lambda().eq(MemberInfo::getPhone, currentUser.getPhone());
		});
		if (null == member) {
			throw new UnsupportedOperationException("获取用户信息失败");
		}
		return member;
	}

}
