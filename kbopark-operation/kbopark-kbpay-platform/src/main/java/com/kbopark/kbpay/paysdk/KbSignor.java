package com.kbopark.kbpay.paysdk;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author laomst
 */
public class KbSignor {

	/**
	 * 计算签名的时候排除在外的key
	 */
	private Set<String> excludeKeys = new HashSet<>();
	/**
	 * 是否忽略空值
	 */
	private boolean ignoreBlankValue = false;
	/**
	 * 生成paramsMap的时候是否包含在计算签名的时候被排除的key, 即使这个值为false，也一定会包含签名字段
	 */
	private boolean includeExcludeKeys = false;
	/**
	 * 生成paramsMap的时候是否包含value 为 blank的key
	 */
	private boolean includeBlankKeys = false;
	/**
	 * 签名字段的字段名
	 */
	private String signKey = "sign";
	/**
	 * 用于计算签名的函数
	 */
	private Function<String, String> signor = String::toString;

	private KbSignor() {
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * 计算签名字符串
	 * @param pojo
	 * @return
	 */
	public String getSign(Object pojo) {
		return getSign(pojo, null);
	}

	/**
	 * 计算签名字符串
	 * @param map
	 * @return
	 */
	public String getSign(Map<String, String> map) {
		return getSign(map, null);
	}

	/**
	 * 计算签名字符串
	 * @param pojo
	 * @param signor
	 * @return
	 */
	public String getSign(Object pojo, Function<String, String> signor) {
		Map<String, String> map = BeanUtil.beanToMap(pojo, false, true).entrySet()
				.stream().collect(Collectors.toMap(Map.Entry::getKey, item -> item.getValue().toString()));
		return getSign(map, signor);
	}

	/**
	 * 计算签名字符串
	 *
	 * @param map
	 * @return 签名
	 * @author laomst
	 */
	public String getSign(Map<String, String> map, Function<String, String> signor) {
		return getParamsMapIncludeSign(map, signor).get(signKey);
	}

	/**
	 * 获取包含签名字段的paramsMap
	 * @param pojo
	 * @return
	 */
	public Map<String, String> getParamsMapIncludeSign(Object pojo) {
		return getParamsMapIncludeSign(pojo, null);
	}

	/**
	 * 获取包含签名字段的paramsMap
	 * @param map
	 * @return
	 */
	public Map<String, String> getParamsMapIncludeSign(Map<String, String> map) {
		return getParamsMapIncludeSign(map, null);
	}


	/**
	 * 获取包含签名字段的paramsMap
	 * @param pojo
	 * @param signor
	 * @return
	 */
	public Map<String, String> getParamsMapIncludeSign(Object pojo, Function<String, String> signor) {
		Map<String, String> map = BeanUtil.beanToMap(pojo, false, true).entrySet()
				.stream().collect(Collectors.toMap(Map.Entry::getKey, item -> item.getValue().toString()));
		return getParamsMapIncludeSign(map, signor);
	}

	/**
	 * 获取包含签名字段的paramsMap
	 * @param map
	 * @param signor
	 * @return
	 */
	public Map<String, String> getParamsMapIncludeSign(Map<String, String> map, Function<String, String> signor) {
		signor = Optional.ofNullable(signor).orElse(this.signor);
		if (null == map) {
			return new HashMap<>();
		}
		// 过滤blank值，顺便排序
		TreeMap<String, String> sortedMap = map.entrySet().stream()
			.filter(item -> !ignoreBlankValue || StrUtil.isNotBlank(item.getValue()))
			.collect(TreeMap::new, (c, v) -> c.put(v.getKey(), v.getValue()), (l, r) -> {
			});
		if (map.size() <= 0) {
			return new HashMap<>();
		}
		// 开始计算签名值
		StringBuilder sb = new StringBuilder();
		sortedMap.forEach((key, value) -> {
			if (!excludeKeys.contains(key))
				sb.append("&").append(key).append("=").append(value);
		});
		sb.deleteCharAt(0);
		String sign = signor.apply(sb.toString());
		Map<String, String> resultMap = includeBlankKeys ? map : sortedMap;
		if (!includeExcludeKeys) {
			excludeKeys.forEach(resultMap::remove);
		}
		// 把计算出来的签名值放到参数map中
		resultMap.put(signKey, sign);
		return resultMap;
	}

	/**
	 * 构建器
	 */
	public static class Builder {

		private final KbSignor kbSignor = new KbSignor();

		private Builder() {
		}

		/**
		 * 设置签名在参数中对应的字段
		 *
		 * @param signKey
		 * @return
		 */
		public Builder withSignKey(String signKey) {
			if (StrUtil.isBlank(signKey)) {
				throw new IllegalArgumentException("签名所对应的键值不能是blank的");
			}
			kbSignor.signKey = signKey;
			return this;
		}

		/**
		 * 设置计算签名的函数
		 *
		 * @param signor
		 * @return
		 */
		public Builder withSignor(Function<String, String> signor) {
			kbSignor.signor = Optional.ofNullable(signor).orElse(kbSignor.signor);
			return this;
		}

		/**
		 * 设置忽略blank值
		 *
		 * @return
		 */
		public Builder ignoreBlankValue() {
			kbSignor.ignoreBlankValue = true;
			return this;
		}

		/**
		 * 设置在计算paramsMap的时候包含在计算签名的时候忽略的key
		 * @return
		 */
		public Builder includeExcludeKeys() {
			kbSignor.includeExcludeKeys = true;
			return this;
		}

		/**
		 * 设置在获取paramsMap的时候包含value为blank的key
		 * @return
		 */
		public Builder includeBlankKeys() {
			kbSignor.includeBlankKeys = true;
			return this;
		}

		/**
		 * 设置在进行签名时忽略的字段
		 *
		 * @param excludeKeys
		 * @return
		 */
		public Builder withExcludeKeys(String... excludeKeys) {
			kbSignor.excludeKeys = new HashSet<>(Arrays.asList(excludeKeys));
			return this;
		}

		/**
		 * 追加在计算签名时忽略的字段
		 *
		 * @param excludeKeys
		 * @return
		 */
		public Builder appendExcludeKeys(String... excludeKeys) {
			kbSignor.excludeKeys.addAll(Arrays.asList(excludeKeys));
			return this;
		}

		/**
		 * 构建
		 *
		 * @return
		 */
		public KbSignor build() {
			return kbSignor;
		}
	}
}
