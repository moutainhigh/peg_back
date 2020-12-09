package com.kbopark.operation.vo;

import cn.hutool.core.bean.BeanUtil;
import com.kbopark.operation.entity.ConsumerOrder;
import lombok.Data;

/**
 * @author :ys
 * 创建消费订单返回vo类
 */
@Data
public class ConsumerOrderVo extends ConsumerOrder {

	/**
	 * 用于返回未支付的商品消费订单  类型
	 */
	private Boolean useType;

	public ConsumerOrderVo(ConsumerOrder consumerOrder) {
		try {
			BeanUtil.copyProperties(consumerOrder,this);
			//如果非成功
			if (!"SUCCESS".equals(consumerOrder.getNotifyStatus())) {
				this.setUseType(false);
			} else {
				this.setUseType(true);
			}
		} catch (Exception e) {
			throw new IllegalStateException("ConsumerOrderVo is error");
		}
	}
}
