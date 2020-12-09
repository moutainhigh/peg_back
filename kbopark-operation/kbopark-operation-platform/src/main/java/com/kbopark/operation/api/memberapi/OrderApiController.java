package com.kbopark.operation.api.memberapi;

import cn.hutool.json.JSONObject;
import cn.qdzhhl.kbopark.common.core.constant.CommonConstants;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.log.annotation.SysLog;
import com.kbopark.operation.apidto.ConsumerRefundOrderDTO;
import com.kbopark.operation.apidto.MemberOrderDTO;
import com.kbopark.operation.entity.MemberAddress;
import com.kbopark.operation.service.ConsumerOrderService;
import com.kbopark.operation.service.ConsumerRefundOrderService;
import com.kbopark.operation.service.MemberAddressService;
import com.kbopark.operation.thirdplatform.dto.*;
import com.kbopark.operation.thirdplatform.entity.OrderInfo;
import com.kbopark.operation.thirdplatform.service.OrderApiService;
import com.kbopark.operation.util.OrderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 11:02
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/orderApi")
@Api(value = "orderApi", tags = "【_青小岛-订单相关接口】")
public class OrderApiController {

	private final ConsumerOrderService consumerOrderService;

	private final OrderApiService orderApiService;

	private final MemberAddressService memberAddressService;

	private final ConsumerRefundOrderService refundOrderService;

	/***
	 * 1.选择商品下单接口，同时推送订单给青小岛
	 *
	 * @param memberOrderDTO
	 * @return
	 */
	@ApiOperation(value = "生成产品订单", notes = "生成产品订单")
	@SysLog("生成产品订单")
	@PostMapping("/create")
	public R createOrder(@Valid @RequestBody MemberOrderDTO memberOrderDTO) {

		//根据地址ID查询用户地址信息
		MemberAddress memberAddress  = new MemberAddress();
		if ("0".equals(memberOrderDTO.getTreeId())) {

			//根据地址ID查询用户地址信息
			memberAddress = memberAddressService.getById(memberOrderDTO.getTakeAddressId());

			if(memberAddress == null){
				return R.failed("请添加收货地址");
			}
		}

		R check = orderApiService.checkOrderInfo(memberOrderDTO);
		if(check.getCode() == CommonConstants.FAIL){
			return check;
		}
		String orderNumber = OrderUtil.getSerialNumber(null);

		//处理待推送订单
		OrderInfo orderInfo = orderApiService.handleOrderInfo(memberOrderDTO, orderNumber, memberAddress);

		//推送
		R apiResult = orderApiService.saveOrder(orderInfo);
		JSONObject data = (JSONObject)apiResult.getData();
		JSONObject result = data.getJSONObject("result");
		if(result.getInt("status").equals(0)){
			return R.failed(result.getStr("msg"));
		}

		return consumerOrderService.createOrder(memberOrderDTO, orderNumber, result.getStr("order_id"));
	}


	/**
	 * 1.1申请退款
	 * @param refundOrderDTO
	 * @return
	 */
	@ApiOperation(value = "申请退款", notes = "申请退款")
	@SysLog("申请退款")
	@PostMapping("/applyRefund")
	public R applyRefund(@Valid @RequestBody ConsumerRefundOrderDTO refundOrderDTO) {
		return refundOrderService.handleRefundRecord(refundOrderDTO);
	}

	/**
	 * 1.2 查询订单申请退款处理状态
	 * @param orderNumber
	 * @return
	 */
	@ApiOperation(value = "查询订单申请退款处理状态", notes = "查询订单申请退款处理状态")
	@GetMapping("/refundState/{orderNumber}" )
	public R findRefundState(@PathVariable("orderNumber" ) String orderNumber) {
		return R.ok(refundOrderService.findByOrderNumber(orderNumber));
	}


	/**
	 * 2.获取青小岛订单列表
	 *
	 * @param orderListDTO
	 * @return
	 */
	@ApiOperation(value = "获取青小岛订单列表", notes = "获取青小岛订单列表")
	@SysLog("获取青小岛订单列表")
	@GetMapping("/page")
	public R getOrderPage(@Valid OrderListDTO orderListDTO) {
		return orderApiService.getOrderList(orderListDTO);
	}

	/**
	 * 3.获取青小岛订单详情
	 *
	 * @param orderDetailDTO
	 * @return
	 */
	@ApiOperation(value = "获取青小岛订单详情", notes = "获取青小岛订单详情")
	@SysLog("获取青小岛订单详情")
	@GetMapping("/detail")
	public R getOrderDetail(@Valid OrderDetailDTO orderDetailDTO) {
		return orderApiService.getOrderDetail(orderDetailDTO);
	}


	/**
	 * 4.取消青小岛订单
	 * @param orderCancelDTO
	 * @return
	 */
	@ApiOperation(value = "取消青小岛订单", notes = "取消青小岛订单")
	@SysLog("取消青小岛订单")
	@GetMapping("/cancel")
	public R cancelOrder(@Valid OrderCancelDTO orderCancelDTO) {
		orderApiService.cancelOrder(orderCancelDTO);
		return consumerOrderService.cancelPayOrder(orderCancelDTO.getOrderNumber(), false);
	}

	/**
	 * 5.部分取消青小岛订单
	 * @param orderCancelPartDTO
	 * @return
	 */
	@ApiOperation(value = "部分取消青小岛订单", notes = "部分取消青小岛订单")
	@SysLog("部分取消青小岛订单")
	@GetMapping("/cancelPart")
	public R cancelPart(@Valid OrderCancelPartDTO orderCancelPartDTO) {
		return orderApiService.cancelOrderPart(orderCancelPartDTO);
	}

	/**
	 * 6.退改青小岛订单
	 * @param orderChangeDTO
	 * @return
	 */

	@ApiOperation(value = "退改青小岛订单", notes = "退改青小岛订单")
	@SysLog("退改青小岛订单")
	@GetMapping("/changeOrder")
	public R changeOrder(@Valid OrderChangeDTO orderChangeDTO) {
		return orderApiService.changeOrder(orderChangeDTO);
	}

	/**
	 * 7.批量获取青小岛订单状态
	 * @param orderStatusDTO
	 * @return
	 */
	@ApiOperation(value = "批量获取青小岛订单状态", notes = "批量获取青小岛订单状态")
	@SysLog("批量获取青小岛订单状态")
	@GetMapping("/listStatus")
	public R listStatus(@Valid OrderStatusDTO orderStatusDTO) {
		return orderApiService.getOrderListStatus(orderStatusDTO);
	}
}
