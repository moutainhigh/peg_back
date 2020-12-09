package com.kbopark.operation.subway.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.kbopark.operation.entity.CouponReceive;
import com.kbopark.operation.entity.MerchantMember;
import com.kbopark.operation.entity.SubwayTicket;
import com.kbopark.operation.enums.LockStatusEnum;
import com.kbopark.operation.enums.UsedStatusEnum;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Encoder;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;


/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/11 16:46
 **/
public class RsaUtil {

	public static final String ENCRYPT_TYPE = "RSA";

	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 保存的私钥
	 */
	public static final String PRIVATE_OUR_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCAdyxa8+kVIkYIY05uo651DasBf4rfYAqYm9wu5Xzh4MvNcYc1ObM/hT8Sxj+9rS+tdfG/Z1WRy4U1COARQL21RkcVLvBxg2njFoSTCxwQoIfNsIp1fLgxx5S+4YC1GeSJgFXJFU6jCQwLHbV1q/3NacOFD3unT4QoB2DMYNPNhUNUooTNImJ41eKgblQqMdctKR3PB7qNVzZ04d1DQor1Go5MrMWVVcu6cKiRV+Zxm8Xx4Vt6VWOTpzBxc9gPkDGb4k7kY46eLOKc2cmIhZ8UouabyuZPYnqE3mEIWsDtqLU71TeTZkg3LoE9uUrgBdLLbnDoeubpR4dVaGWpu+mbAgMBAAECggEASIyE1RyYDiJb8JK8kYkIlfxRDbiModeoREUJFMbvPtTn467wj6N1UjMayqyBBMpQeaJ5EwH1di+8m5XWjIfGWyBENndavJBddSybVDta7xzpdMT1fKIhl6NBoobRW/UydmCuka/hx/rs/T4a40htPdsz2upCh9tFJCH+jY1FL/P4qcRE+etJNZY9TD9LX8gV53NcKkPODWrluHZaW3Yq0IfKNotthnlB/kHaB6m0R6HuPi7w8Jo3ZzVPiyVlYSAaPAW0u0jxENtRqDP0en7VI2L/+7EKsQJ1ydW27Rnarqk4qZMbzknRDljHS3k1LcVI1e55QZK22ffaPZnYd3p26QKBgQDcoHAe3GLl1tFvI8YDLeHfmeMDt348yu+d9dAi6ylDf5JIKkgCsxMvcDi5F6jUmrG5ByclrxcTKfat2U5OrAWymadPDC38UROXuV58NwBrLyFX8XGtg3wZDtih21WFXRuOniZBQBLnRqsOsgYBVUrQE7KrK+DUFiWaFBroQCrSHwKBgQCVEAE96Ual+5ZwcK4oJBhQ8cGgO7ZksO7annwxiWtU+9GrqidBi80I0Z/DMmoTCmw0tzrlwxZcviAoLIcPkMDRlNOtUAhHBr6mgXjuhtHLsUTb+G0Hc4PklPqVqYE2Rs+1prKclx0SbpaYhkKdLkbKD2jUvTQtpMOQxD7G+lZRBQKBgQCyK0qAUZ/4VpPJeg1Fsf7vsPmYnc1vUL9TRBiyqlAnd1Vtu99tdqourCTSrN2l28tcTf96OjybOE9Cs/O+KsNrKRTpDD/yvJLi+0rnaw+Gf4gg2hXgdwd3wPqHM3aL/dXsRaFFHnZlmTc45eFdoECLauzluMiW3c+97zCPkIpEJwKBgFSAXjPaqlIt7XqQZiobTC2W300WjK9IHh+Q+JaccFwc5R+LtfVSa0k0jHz7d2aT4gOAW8MkdOjeXxxnIamRRb2JwPXfCNI64JlDjGqrwwLV21NX4Xb35S0Px35QOQ+r1NQFS/u5LqHkTzrn4Zt5QGgLlEIFgC7f2H2Ywn3KLGSBAoGBANTmNZ06fK/81Os6nrFDPLEF/KHcIvEMSM3rwxQzRW0N7k6Y+g/kH0+ZgsVVoSvFeL2fNjXN8gHXI0AycR6nkF7o7l4voy7aBzogCysQiYzZunZGZ8OwAWaVyx2UFg9QA1J0k/NFocd2/dLEj/q9p4/f4Wbw2YFqflfBcF9zBGMq";

	/**
	 * 保存的公钥
	 */
	public static final String PUBLIC_OUR_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgHcsWvPpFSJGCGNObqOudQ2rAX+K32AKmJvcLuV84eDLzXGHNTmzP4U/EsY/va0vrXXxv2dVkcuFNQjgEUC9tUZHFS7wcYNp4xaEkwscEKCHzbCKdXy4MceUvuGAtRnkiYBVyRVOowkMCx21dav9zWnDhQ97p0+EKAdgzGDTzYVDVKKEzSJieNXioG5UKjHXLSkdzwe6jVc2dOHdQ0KK9RqOTKzFlVXLunCokVfmcZvF8eFbelVjk6cwcXPYD5Axm+JO5GOOnizinNnJiIWfFKLmm8rmT2J6hN5hCFrA7ai1O9U3k2ZINy6BPblK4AXSy25w6Hrm6UeHVWhlqbvpmwIDAQAB";

	/**
	 * 测试
	 */
	public static final String PRIVATE_TEST_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC6FTKCczjMhSbNW6SI8RyDKqySZS5Cd3eRlITnOmuo2AUjAOW+Txab9QqgiEm03OXD4L8xLdxDdp/OOoM+U0gPulh3XXh1pahmeMa06zRLOH/qMyV1J56mL+9LnubLhQV/xyEla3qUUBjHyx/IoYBvOGJxRdpqDGpniPdsbs4dF+0gPYGDUAZ1ZuQTY01djNLLaV6OACpInlpqnqqtCjZ7EDqWrAzV+8tX9JRUdN9PVZ0T7tGtqByXOv/jGXbydwy0TdLPxr2Q6gTmyVfYsohz+jXWwibcP6GIXZuVlRxNEM0qQGiFtHGVtEpnzxUuIzCn2Qu4IF7nRcU4GlP8855FAgMBAAECggEAZuar7MlswmRrZapk3TmAC+TAU6BSKX1JCJ85s2hx+oeip42qfkfb99hUzRswryxsfpo9ACQdJs1lZ7ShuQhy1Ae3PiLZJy25ZxkowQlD/Mi8q7Um5JdJOxtM1+OCO09PEC783GWH/pYgbfOcmuT8UgS7I2p+cqB1AtYSBMttfVY4kFl8Gp/DuOTSsKfHLx3+Lwhr++h4vGxjI2dDSdQbSp44Ob7Y82aTpEbkSknLeH7KZEFPSYGVCvxo7EPLkIA5Vyavd7loZWzZn5L+AWpw9UlPOQ488exFw2V7crZRdxVGI4XprGClxNC+Avsbk8+whxACkev6zILtd8z8E8Z5QQKBgQDthNIq+Gu1CDrASuQUpvLMRlZg5IQAoHcmuZ/AJc9PbH2wh3xsQxbEjublerGeJcjRdp5z/Wo4iF7UcpqzqdAkzxsrKEasSSngTK/kCeWvpSEHmkPJnOsdYUVSDEm3rD4OwIwtVNq5yypsXaoxXnU6DFIomKb0bkNsgQZP1gYpkQKBgQDIj9AmVMiulFoCFAxdausqXImmGof9dZF3UmiJXV35PbvkWz3gisKswgrYrmsikutPY8nL22yASqqN1W6fiexWV7PG9x/iuUruwF3q/FoDx3b7mamEYwsVO1ookzwl6arNqkYpTZyxSgeloUCnYX0013A+QPlfOT736hjT2fEvdQKBgF7vjt20C8EBOBJyHLYQgM2bc50o2YYynU1yNmMj+bfiQl7T2KyhKDGKeMnSf7oZmin3AqCT8ugDwfuijykuv85mpZJbd9wXJlDL9iXsusy48PnL9DpaqTNGFOIVnUV36BoXZjAcUYyfoyZLTeKLwclg8VneoiTjCboN3Er+3dmxAoGATIHPvU+KXk5pJ3HOZbRrkPVL7Pkh9yXqH8Jn7j6XooRSMII90HIeYRiEWrPuWnoBGX/Pctx5P1c8SG6qjGFormc2RSfwGeTlGL55FTK+g8KLON8dM0s05xyFAAVqZGwGo/3YVdktdNPwptYke7VMteOZBaZzeke0JY4/elEhGCECgYEAy3r9M0TGERQfuRWrMr1oJm1PmdEwYsCnRRDpOMhH7kaHru1WGrNqV4I/BWkTFJgZ+VRpMA79xGPhurLXSdqKevYSHwmRaK7Bl2lC9WgvE3ZhjaCBi8jnCN1/BOlAGM5K19mwgbk8uObtaT8SCsXvU4TcJR0FR7IYTHbm1zBQQvQ=";

	/**
	 * 公钥加密
	 *
	 * @param content
	 * @return
	 */
	public static String encrypt(String content) {
		try {
			RSA rsa = new RSA(null, PUBLIC_OUR_KEY);
			return rsa.encryptBase64(content, KeyType.PublicKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 私钥解密
	 *
	 * @param content
	 * @return
	 */
	public static String decrypt(String content) {
		try {
			RSA rsa = new RSA(PRIVATE_OUR_KEY, null);
			return rsa.decryptStr(content, KeyType.PrivateKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 获取公钥私钥后保存
	 *
	 * @return
	 */
	public static Map<String, String> generateKeyPair() {

		try {
			KeyPair pair = SecureUtil.generateKeyPair(ENCRYPT_TYPE);
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();
			byte[] pubEncBytes = publicKey.getEncoded();
			byte[] priEncBytes = privateKey.getEncoded();
			String pubEncBase64 = new BASE64Encoder().encode(pubEncBytes);
			String priEncBase64 = new BASE64Encoder().encode(priEncBytes);
			Map<String, String> map = new HashMap<>(2);
			map.put(PUBLIC_KEY, pubEncBase64);
			map.put(PRIVATE_KEY, priEncBase64);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 获取密钥对
	 *
	 * @return 密钥对
	 */
	public static KeyPair getKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024);
		return generator.generateKeyPair();
	}

	/**
	 * 获取私钥
	 *
	 * @param privateKey 私钥字符串
	 * @return
	 */
	public static PrivateKey getPrivateKey(String privateKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
		return keyFactory.generatePrivate(keySpec);
	}


	/**
	 * 生成签名
	 *
	 * @param data       待签名数据
	 * @param privateKey 私钥
	 * @return 签名
	 */
	public static String sign(String data, PrivateKey privateKey) throws Exception {
		byte[] keyBytes = privateKey.getEncoded();
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey key = keyFactory.generatePrivate(keySpec);
		Signature signature = Signature.getInstance("SHA256WithRSA");
		signature.initSign(key);
		signature.update(data.getBytes());
		return new String(Base64.encodeBase64(signature.sign()));
	}

	/**
	 * 验签
	 *
	 * @param srcData   原始字符串
	 * @param publicKey 公钥
	 * @param sign      签名
	 * @return 是否验签通过
	 */
	public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
		byte[] keyBytes = publicKey.getEncoded();
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey key = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance("SHA256WithRSA");
		signature.initVerify(key);
		signature.update(srcData.getBytes());
		return signature.verify(Base64.decodeBase64(sign.getBytes()));
	}


	/**
	 * @title: 获取有序排列签名字符串
	 * @author: sunwenzhi
	 * @description:
	 * @date:
	 * @param:
	 * @return:
	 **/
	public static String sortContent(Map<String, String> params) {
		if (params == null) {
			return null;
		} else {
			params.remove("sign");
			StringBuffer content = new StringBuffer();
			List<String> keys = new ArrayList(params.keySet());
			Collections.sort(keys);
			for (int i = 0; i < keys.size(); ++i) {
				String key = keys.get(i);
				Object value = params.get(key);
				content.append((i == 0 ? "" : "&") + key + "=" + value);
			}
			return content.toString();
		}
	}


	/***
	 * 生成签名信息
	 * @param data
	 * @return
	 */
	public static String getPrivateKeySign(String data) {
		Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, PRIVATE_TEST_KEY, null);
		byte[] sign1 = sign.sign(data.getBytes());
		return Base64.encodeBase64String(sign1);
	}


	/**
	 * 构建发券参数
	 * @param couponProvide
	 * @return
	 */
	public static String getHttpParams(CouponProvide couponProvide){
		if(couponProvide == null){
			return null;
		}
		try {
			HashMap hashMap = JSONObject.parseObject(JSONObject.toJSONString(couponProvide), HashMap.class);
			String sortContent = sortContent(hashMap);
			String sign = RSA2.buildRSAEncryptByPrivateKey(sortContent, PRIVATE_TEST_KEY);
			couponProvide.setSign(sign);
			String params = JSONObject.toJSONString(couponProvide);
			return params;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * @title: 获取MD5签名
	 * @author: sunwenzhi
	 * @description:
	 * @date:
	 * @param:
	 * @return:
	 **/
	public static String getSignMD5(String content) {
		if (StrUtil.isBlank(content)) {
			return null;
		}
		try {
			return DigestUtils.md5DigestAsHex(content.getBytes("UTF-8")).toUpperCase();
		} catch (Exception e) {
			return null;
		}
	}

	/***
	 * 地铁发券加签
	 */
	private void test() {
		CouponReceive receive = new CouponReceive();
		DateTime startDay = DateUtil.parseDate("2020-11-06");
		DateTime endDay = DateUtil.parseDate("2020-11-20");
		receive.setCouponStartTime(startDay);
		receive.setCouponEndTime(endDay);
		receive.setCouponNumber(1);
		receive.setMemberPhone("17685722520");
		SubwayTicket subwayTicket = new SubwayTicket();
		subwayTicket.setSubwayCode("C2001");
		subwayTicket.setValue(1.0);

		//先请求地铁APP发券
		CouponProvide couponProvide = new CouponProvide();
		couponProvide.setData(subwayTicket, receive);
		String httpParams = RsaUtil.getHttpParams(couponProvide);
		System.out.println(httpParams);
	}

}
