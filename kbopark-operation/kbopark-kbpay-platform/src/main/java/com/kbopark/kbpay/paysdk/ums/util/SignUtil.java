package com.kbopark.kbpay.paysdk.ums.util;

import cn.hutool.crypto.SecureUtil;
import com.kbopark.kbpay.paysdk.KbSignor;

import java.util.Map;
import java.util.function.Function;

public class SignUtil {

	private static final String signKey = "sign";

	/**
	 * 计算包含签名字段的完整参数列表
	 *
	 * @param dto
	 * @param md5Key
	 * @param sha256 是不是使用sha256算法加密, 如果不是, 就是使用sha256
	 * @return
	 */
	public static Map<String, String> getParamsMapIncludeSign(Object dto, String md5Key, boolean sha256) {
		Function<String, String> signorFoo = sha256 ? str -> SecureUtil.sha256(str + md5Key) : str -> SecureUtil.md5(str + md5Key);
		KbSignor signor = KbSignor.builder()
			.withExcludeKeys(signKey)
			.withSignKey(signKey)
			.ignoreBlankValue()
			.withSignor(signorFoo)
			.build();
		return signor.getParamsMapIncludeSign(dto);
	}

	/**
	 * 计算包含签名字段的完整参数列表
	 *
	 * @param map
	 * @param md5Key
	 * @return
	 */
	public static Map<String, String> getParamsMapIncludeSign(Map<String, String> map, String md5Key, boolean sha256) {
		Function<String, String> signorFoo = sha256 ? str -> SecureUtil.sha256(str + md5Key) : str -> SecureUtil.md5(str + md5Key);
		KbSignor signor = KbSignor.builder()
			.withExcludeKeys(signKey)
			.withSignKey(signKey)
			.ignoreBlankValue()
			.withSignor(signorFoo)
			.build();
		return signor.getParamsMapIncludeSign(map);
	}

	public static String getSign(Map<String, String> map, String md5Key, boolean sha256) {
		return getParamsMapIncludeSign(map, md5Key,sha256).get(signKey);
	}
}
