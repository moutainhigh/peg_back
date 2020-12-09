package com.kbopark.operation.thirdplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/30 15:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMap {

    private Boolean result;

    private String msg;


    public static ResultMap success(){
        return new ResultMap(true,"接收成功");
    }

	public static ResultMap fail(){
		return new ResultMap(false,"接收处理异常");
	}

}
