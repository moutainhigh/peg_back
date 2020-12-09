package cn.qdzhhl.kbopark.admin.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/8/28 11:01
 **/
@AllArgsConstructor
public enum UserTypeEnum {


	Administrator(1,"总平台管理员"),
	Operation(2,"运营商"),
	Channel(3,"渠道商"),
	Promoter(4,"推广员"),
	Merchant(5,"普通商家"),
	MerchantLower(6,"普通商家下级"),
	Other(999,"其他")
	;

	@Getter
	public final Integer code;

	@Getter
	public final String description;

}
