package com.kbopark.operation.dto;

import com.kbopark.operation.entity.SubwayTicket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/1 11:12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SubwayTicketDTO extends SubwayTicket {
	private static final long serialVersionUID = 1L;

	/**页面搜索条件,其他位置可忽略**/
	private String searchName;
	private List<String> searchReceiveTime;


}
