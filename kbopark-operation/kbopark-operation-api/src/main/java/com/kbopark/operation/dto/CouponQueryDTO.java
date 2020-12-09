package com.kbopark.operation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/1 11:12
 **/
@Data
public class CouponQueryDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 查询参数经纬度，用户端定位，或者某个地铁站的经纬度
	 */
	private Double lng;
	private Double lat;

	/**行业类别**/
	private Integer merchantCategory;
	/**搜索条件，商家名称或者优惠券名称**/
	private String searchName;
	/**附近搜索，near附近  table列表搜索**/
	private String searchType;

	/**整条地铁线，地铁线的标识*/
	private String lineCode;

	/**分页信息*/
	private Integer current;
	private Integer size;

	private String couponType;

	@ApiModelProperty("是否按照权重排序，查询推荐卡券时使用")
	private Boolean orderByWeight = Boolean.FALSE;

}
