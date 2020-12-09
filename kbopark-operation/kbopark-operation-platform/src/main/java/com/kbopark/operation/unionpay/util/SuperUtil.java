package com.kbopark.operation.unionpay.util;

import cn.hutool.core.util.EnumUtil;
import com.alibaba.fastjson.JSONObject;
import com.kbopark.operation.unionpay.dto.SuperDto;
import com.kbopark.operation.unionpay.exceptions.SecureUtilException;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

/**
 * Util汇总整合工具类,主要对请求和回复的报文进行组装解析，签名，验签，加密，解密等操作
 *
 * @author Roi
 */
public class SuperUtil {
	private static final Logger logger = LoggerFactory.getLogger(SuperUtil.class);
	public static final String SYS_VAR_RAW_DATA = "rawData";
	static PrivateKey privateKey;
	static PublicKey pubKey;
	static String channelId;
	static String preURL;
	static String verNo;
	static String groupId;

	/**
	 * 初始化参数
	 */
	static {
		try {
			InputStream is = new ClassPathResource("/config/ConstantsDefine.local.properties").getInputStream();
			Properties pps = new Properties();
			pps.load(is);
			String privateKeyPath = pps.getProperty("FAN_RPIVATE_KEY_PATH");
			String privateKeyPwd = pps.getProperty("FAN_RPIVATE_KEY_PWD");
			String publicKeyPath = pps.getProperty("FAN_RPUBLIC_KEY_PATH");
			InputStream priKeyStoreFileStream = new ClassPathResource(privateKeyPath).getInputStream();
			privateKey = getPriKeyPkcs12(priKeyStoreFileStream, privateKeyPwd);
			InputStream pubKeyStoreFileStream = new ClassPathResource(publicKeyPath).getInputStream();
			X509Certificate cert = X509Certificate.getInstance(pubKeyStoreFileStream);
			pubKey = cert.getPublicKey();
			channelId = pps.getProperty("CHANNEL_ID");
			preURL = pps.getProperty("URL_PRE");
			verNo = pps.getProperty("VER_NO");
			//TODO 请修改ConstantsDefine.local.properties配置文件中集团号
			groupId = pps.getProperty("GROUP_ID");
			try {
				if(is != null){
					is.close();
				}
				if(priKeyStoreFileStream != null){
					priKeyStoreFileStream.close();
				}
				if(pubKeyStoreFileStream != null){
					pubKeyStoreFileStream.close();
				}
			}catch (Exception e){

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("配置文件读取失败！");
		}
	}

	/**
	 * 获取Json格式报文加签；发送；并对返回值进行处理后验签
	 *
	 * @param superDto
	 * @return
	 */
	public static ExecuteLogger oneStep(SuperDto superDto) {
		ExecuteLogger executeLogger = new ExecuteLogger();
		Map<String, String> srcMap = sign(superDto);
		String params = JSONObject.toJSONString(srcMap);
		System.out.println(">>>请求参数：" + params);
		executeLogger.setRequestParam(params);
		try {
			String resultStr = post(preURL + superDto.getTransCode(), params, "UTF-8");
			System.out.println(">>>响应信息：" + resultStr);
			executeLogger.setResponseData(resultStr);
			if (null == resultStr) {
				logger.info("数据返回超时");
				executeLogger.setResponseData("数据返回超时");
			}
			SuperDto returnDto = JSONObject.parseObject(resultStr, SuperDto.class);
			Boolean flag = validate(returnDto);
			if (flag) {
				logger.info("返回报文验签成功");
				executeLogger.setValidResult("报文验签成功");
			} else {
				logger.info("返回报文验签失败");
				executeLogger.setValidResult("报文验签失败");
			}
			executeLogger.setSuperDto(returnDto);
			return executeLogger;
		} catch (Exception e) {
			e.printStackTrace();
			return executeLogger;
		}
	}

	/**
	 * 对参数进行判断
	 * null，"","   " == {true}
	 *
	 * @param str
	 * @return
	 */
	public static Boolean isEmpty(String str) {
		if (StringUtils.isEmpty(str) || "".equals(str.replace(" ", ""))) {
			return true;
		}
		return false;
	}

	/**
	 * 获取map类型报文
	 *
	 * @param superDto
	 * @return
	 */
	public static Map getSrcMap(SuperDto superDto) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateTime = sdf.format(Calendar.getInstance().getTime());
		Map<String, String> srcMap = new HashMap<String, String>();

		/*----------------------报文头start------------------------*/
		if (isEmpty(superDto.getTransCode())) {
			logger.error("未定义交易码");
		}
		srcMap.put("transCode", superDto.getTransCode());
		srcMap.put("verNo", verNo);
		srcMap.put("channelId", channelId);
		srcMap.put("groupId", groupId);
		//请求系统日期
		srcMap.put("srcReqDate", dateTime.substring(0, 8));
		//请求系统时间
		srcMap.put("srcReqTime", dateTime.substring(8));
		//请求系统流水号，须唯一，该处取时间戳以表示不重复序列，生产环境不建议使用
		srcMap.put("srcReqId", superDto.getSrcReqId());
		/*----------------------报文头end--------------------------*/

		/*----------------------报文体start------------------------*/
		if (isEmpty(superDto.getMerNo())) {
			logger.error("未定义企业用户号");
		}
		srcMap.put("merNo", superDto.getMerNo());
		try {
			switch (Integer.parseInt(superDto.getTransCode())) {
				case 202001:
					if (isEmpty(superDto.getMerOrderNo()) || isEmpty(superDto.getPayAmt())) {
						logger.error("未定义商户订单号或者划付金额");
						break;
					}
					srcMap.put("merOrderNo", superDto.getMerOrderNo());
					srcMap.put("payAmt", superDto.getPayAmt());
					break;
				case 202002:
					if (isEmpty(superDto.getPayAmt())) {
						logger.error("未定义划付金额");
						break;
					}
					srcMap.put("payAmt", superDto.getPayAmt());
					break;
				case 202003:
					if (isEmpty(superDto.getMerOrderNo()) || isEmpty(superDto.getPayAmt()) || isEmpty(superDto.getCardNo()) ||
							isEmpty(superDto.getPs())) {
						logger.error("未定义商户订单号或者划付金额或者卡号或者附言");
						break;
					}
					srcMap.put("merOrderNo", superDto.getMerOrderNo());
					srcMap.put("payAmt", superDto.getPayAmt());
//                    srcMap.put("cardNo", superDto.getCardNo());
					srcMap.put("cardNo", hashHex(superDto.getCardNo(), "SHA-256"));
					srcMap.put("ps", superDto.getPs());
					break;
				case 202004:
					if (isEmpty(superDto.getPayAmt()) || isEmpty(superDto.getPayType()) || isEmpty(superDto.getCardNo()) ||
							isEmpty(superDto.getPs())) {
						logger.error("未定义划付金额或者分账类型或者卡号或者附言");
						break;
					}
					srcMap.put("payAmt", superDto.getPayAmt());
					srcMap.put("cardNo", hashHex(superDto.getCardNo(), "SHA-256"));
					srcMap.put("payType", superDto.getPayType());
					srcMap.put("ps", superDto.getPs());
					break;
				case 202007:
					if (isEmpty(superDto.getTransDate()) || isEmpty(superDto.getQueryItem()) || isEmpty(superDto.getQueryValue())) {
						logger.error("未定义交易日期或者查询项或者查询值");
						break;
					}
					srcMap.put("transDate", superDto.getTransDate());
					srcMap.put("queryItem", superDto.getQueryItem());
					srcMap.put("queryValue", superDto.getQueryValue());
					break;
				case 202008:
					if (isEmpty(superDto.getReqDate()) || isEmpty(superDto.getReqJournalNo())) {
						logger.error("未定义请求日期或者请求交易流水号");
						break;
					}
					srcMap.put("reqDate", superDto.getReqDate());
					srcMap.put("reqJournalNo", superDto.getReqJournalNo());
					break;
				default:
					break;
			}
		} catch (Exception e) {
			throw new RuntimeException("报文出错了！");
		}
		/*----------------------报文体end-------------------------*/

		return srcMap;
	}

	/**
	 * json字符串转为Object对象
	 *
	 * @param jsonStr
	 * @param dtoClass
	 * @param <T>
	 * @return
	 */
//    public static <T> T jsonStrToDto(String jsonStr, Class<T> dtoClass) {
//        try {
//            Object dto;
//            JSONObject jsonDto = JSONObject.parseObject(jsonStr);
//            dto = JSONObject.parseObject(jsonDto, dtoClass);
//            return (T) dto;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

	/**
	 * 获取报文签名裸数据
	 *
	 * @param superDto
	 */
	public static Map getRawData(SuperDto superDto) {
		Map<String, String> srcMap = getSrcMap(superDto);
		List<String> sortKeys = new ArrayList<String>(srcMap.keySet());
		Collections.sort(sortKeys);
		StringBuffer sb = new StringBuffer();
		for (String key : sortKeys) {
			sb.append(key);
			sb.append("=");
			sb.append(srcMap.get(key));
			sb.append("&");
		}
		String rawData = sb.toString();
		System.out.println(rawData.substring(0, rawData.length() - 1));
		srcMap.put(SYS_VAR_RAW_DATA, rawData.substring(0, rawData.length() - 1));
		return srcMap;
	}

	/**
	 * 签名
	 *
	 * @param superDto
	 */
	public static Map sign(SuperDto superDto) {
		Map<String, String> srcMap = getRawData(superDto);
		String rawData = srcMap.get(SYS_VAR_RAW_DATA);
		srcMap.remove(SYS_VAR_RAW_DATA);
		try {
			byte[] signB = signRsa(rawData.getBytes(), privateKey, ALGORITHM_SHA256WITHRSA);
			srcMap.put("signature", byteArr2HexString(signB));
			System.out.println(byteArr2HexString(signB));
			return srcMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密解密算法，值为{@value}
	 */
	public final static String TRANSFORMATION_RSA = "RSA";

	/**
	 * 签名验签算法，值为{@value}
	 */
	public final static String ALGORITHM_MD5WITHRSA = "MD5withRSA";
	/**
	 * 签名验签算法，值为{@value}
	 */
	public final static String ALGORITHM_SHA256WITHRSA = "SHA256withRSA";
	/**
	 * 签名验签算法，值为{@value}
	 */
	public final static String ALGORITHM_SHA1WITHRSA = "SHA1withRSA";

	/**
	 * 签名
	 *
	 * @param rawData    签名裸数据
	 * @param privateKey 私钥
	 * @param algorithm  签名验签算法
	 * @return
	 * @throws SecureUtilException
	 */
	public static byte[] signRsa(byte[] rawData, PrivateKey privateKey, String algorithm) throws SecureUtilException {
		try {
			Signature instance = Signature.getInstance(algorithm);
			instance.initSign(privateKey);
			instance.update(rawData);
			return instance.sign();
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("签名失败");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 验签
	 *
	 * @param rawData   验签裸数据
	 * @param signature 签名
	 * @param publicKey 公钥
	 * @param algorithm 签名验签算法， 支持{@link #ALGORITHM_MD5WITHRSA MD5withRSA} /
	 *                  {@link #ALGORITHM_SHA256WITHRSA SHA256withRSA} /
	 *                  {@link #ALGORITHM_SHA1WITHRSA SHA1withRSA}
	 * @return true-验签通过，false-验签不通过
	 * @throws SecureUtilException
	 */
	public static Boolean verify(byte[] rawData, byte[] signature, PublicKey publicKey, String algorithm)
			throws SecureUtilException {
		try {
			Signature instance = Signature.getInstance(algorithm);
			instance.initVerify(publicKey);
			instance.update(rawData);
			return instance.verify(signature);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("验签失败");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 加密
	 *
	 * @param data           明文数据
	 * @param publicKey      公钥
	 * @param transformation 加密算法，支持{@link #TRANSFORMATION_RSA RSA}
	 * @return
	 * @throws SecureUtilException
	 */
	public static byte[] encrpty(byte[] data, PublicKey publicKey, String transformation) throws SecureUtilException {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("加密失败");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 解密
	 *
	 * @param data           密文数据
	 * @param privateKey     私钥
	 * @param transformation 加密算法，支持{@link #TRANSFORMATION_RSA RSA}
	 * @return
	 * @throws SecureUtilException
	 */
	public static byte[] decrpty(byte[] data, PrivateKey privateKey, String transformation) throws SecureUtilException {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("解密失败");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 发送https请求条件
	 */
	private static class TrustAnyTrustManager implements X509TrustManager {
		//信任管理器
		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
		}

		//信任证书。
		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
		}

		//返回受信任的X509证书数组。
		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new java.security.cert.X509Certificate[]{};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	/**
	 *  
	 * post方式请求服务器(https协议) 
	 *  
	 *
	 * @param url                 请求地址 
	 * @param content             参数 
	 * @param charset             编码 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws IOException                    
	 * @return 
	 */
	public static String post(String url, String content, String charset) throws NoSuchAlgorithmException, KeyManagementException, IOException {
		//指定信任管理器对象。
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
		URL console = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setDoOutput(true);
		//设置请求头
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conn.setConnectTimeout(60000);
		conn.connect();
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(content.getBytes(charset));
		// 刷新、关闭  
		out.flush();
		out.close();
		InputStream is = conn.getInputStream();
		if (is != null) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			is.close();
			return outStream.toString();
		}
		return null;
	}

	/**
	 * 将流转换为字符串
	 *
	 * @param inputStream
	 * @return
	 */
	private static String streamTostring(InputStream inputStream) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String str;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			while ((str = bufferedReader.readLine()) != null) {
				stringBuilder.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();

	}

	/**
	 * 证书类型，值为{@value}
	 */
	public static final String CERT_TYPE_JKS = "JKS";
	/**
	 * 证书类型，值为{@value}
	 */
	public static final String CERT_TYPE_PKCS12 = "PKCS12";
	/**
	 * 证书类型，值为{@value}
	 */
	public static final String CERT_TYPE_PKCS8 = "PKCS8";

	private static KeyStore keyStore = null;//签名证书

	/**
	 * 将证书文件读取为证书存储对象：证书文件类型可为：JKS（.keystore等），PKCS12（.pfx）
	 *
	 * @param keyFileIn 证书文件文件流
	 * @param keypwd    证书密码
	 * @param type      证书类型
	 * @return 证书对象
	 */
	public static KeyStore getKeyStore(InputStream keyFileIn, String keypwd, String type) {
		try {
			if (CERT_TYPE_JKS.equals(type)) {
				keyStore = KeyStore.getInstance(type);
			} else if (CERT_TYPE_PKCS12.equals(type)) {
				/**
				 * 动态注册SUN JCE<br/>
				 * JCE（Java Cryptography Extension）是一组包，它们提供用于加密、密钥生成和协商以及 Message
				 * Authentication Code（MAC）算法的框架和实现。<br/>
				 * 设置安全提供者为BouncyCastleProvider
				 */
				if (Security.getProvider("BC") == null) {
					// 将提供程序添加到下一个可用位置。
					Security.addProvider(new BouncyCastleProvider());
				} else {
					Security.removeProvider("BC");
					Security.addProvider(new BouncyCastleProvider());
				}
				keyStore = KeyStore.getInstance(type);
			}
			char[] nPassword = null;
			nPassword = null == keypwd || "".equals(keypwd.trim()) ? null : keypwd.toCharArray();
			keyStore.load(keyFileIn, nPassword);
			if (keyFileIn != null){
				keyFileIn.close();
			}
			return keyStore;
		} catch (Exception e) {
			if (Security.getProvider("BC") == null) {
				logger.info("BC Provider not installed.");
			}
			logger.error("Fail: load privateKey certificate", e);
		} finally {
			if (keyFileIn != null){
				try {
					keyFileIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 从私钥证书中获取私钥
	 *
	 * @param keyFileIn 证书文件流.pfx, .keystore)
	 * @param keypwd    证书密码
	 * @param type      证书类型
	 * @return：私钥
	 */
	public static PrivateKey getPriKey(InputStream keyFileIn, String keypwd, String type) {
		PrivateKey privateKey = null;
		getKeyStore(keyFileIn, keypwd, type);
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				// 第一个条目
				keyAlias = (String) aliasenum.nextElement();
			}
			privateKey = (PrivateKey) keyStore.getKey(keyAlias, keypwd.toCharArray());
		} catch (Exception e) {
			logger.error("Fail: get private key from private certificate", e);
		}
		return privateKey;
	}

	/**
	 * 从私钥证书中获取PKCS12标准私钥<br/>
	 *
	 * @param keyFileIn 私钥文件流 .pfx
	 * @param keypwd    证书密码
	 * @return：私钥
	 */
	public static PrivateKey getPriKeyPkcs12(InputStream keyFileIn, String keypwd) {
		return getPriKey(keyFileIn, keypwd, CERT_TYPE_PKCS12);
	}

	/**
	 * 从私钥证书中获取JKS标准私钥<br/>
	 *
	 * @param keyFileIn 私钥文件流 .keystore .jks
	 * @param keypwd    密码
	 * @return
	 */

	public static PrivateKey getPriKeyJks(InputStream keyFileIn, String keypwd, String type) {
		return getPriKey(keyFileIn, keypwd, CERT_TYPE_JKS);
	}

	/**
	 * 从私钥证书中获取PKCS8标准私钥<br/>
	 *
	 * @param keyFileIn 私钥文件流
	 * @return 私钥
	 */
	public static PrivateKey getPriKeyPkcs8(InputStream keyFileIn) {
		PrivateKey privateKey = null;
		try {
			byte[] buffer = readFile(keyFileIn);
			buffer = decodeBase64(buffer);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			logger.error("Fail: get private key from private certificate", e);
		} finally {
			if (keyFileIn != null){
				try {
					keyFileIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return privateKey;
	}

	/**
	 * 从公钥证书中获取公钥<br/>
	 *
	 * @param keyFileIn 文件流 .cer
	 * @return
	 */
	public static PublicKey getPubKey(InputStream keyFileIn) {
		java.security.cert.X509Certificate publicCert = getPubCert(keyFileIn, null);
		PublicKey publicKey = publicCert.getPublicKey();
		return publicKey;
	}

	/**
	 * 读取文件的方法<br/>
	 *
	 * @param keyFileIn 文件流
	 * @return
	 * @throws Exception
	 */
	private static byte[] readFile(InputStream keyFileIn) throws Exception {
		BufferedReader br;
		InputStreamReader isr = new InputStreamReader(keyFileIn);
		br = new BufferedReader(isr);
		String readLine;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.startsWith("--")) {
				continue;
			}
			sb.append(readLine);
		}
		br.close();
		isr.close();
		keyFileIn.close();
		return sb.toString().getBytes();
	}

	/**
	 * 读取公钥证书<br/>
	 *
	 * @param keyFileIn 文件路径 .cer
	 * @return 证书对象
	 */
	public static java.security.cert.X509Certificate getPubCert(InputStream keyFileIn, String provider) {
		CertificateFactory cf = null;
		InputStream in = null;
		java.security.cert.X509Certificate x509Certificate = null;
		try {
			if ("BC".equals(provider)) {
				cf = CertificateFactory.getInstance("X.509", "BC");
			} else {
				cf = CertificateFactory.getInstance("X.509");
			}
			in = keyFileIn;
			x509Certificate = (java.security.cert.X509Certificate) cf.generateCertificate(in);
			if (in != null) {
				in.close();
			}
		} catch (Exception e) {
			logger.error("Fail: load public certificate", e);
			return null;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Fail: close FileInputStream", e);
				}
			}
		}
		return x509Certificate;
	}

	/**
	 * RSA密钥生成
	 *
	 * <pre>
	 * 由{@link java.security}生成的RSA公钥格式为X509，私钥格式为PKCS#8
	 * </pre>
	 *
	 * @author 丁盛
	 *
	 */
	/**
	 * 密钥长度, 值为{@value}
	 */
	public static final int KEY_SIZE_1024 = 1024;
	/**
	 * 密钥长度, 值为{@value}
	 */
	public static final int KEY_SIZE_2048 = 2048;

	/**
	 * 密钥对MAP中公钥的key值，值为{@value}
	 */
	public static final String PUBLIC_KEY = "publicKey";
	/**
	 * 密钥对MAP中私钥的key值，值为{@value}
	 */
	public static final String PRIVATE_KEY = "privateKey";

	/**
	 * 生成密钥对
	 *
	 * @param keySize 密钥长度, 目前仅支持{@link #KEY_SIZE_1024 1024位}和
	 *                {@link #KEY_SIZE_2048 2048位}
	 * @return 密钥对 {@link String} : {@link Object}
	 * <p>
	 * 公钥 {@link #PUBLIC_KEY publicKey} :
	 * {@link RSAPublicKey}
	 * </p>
	 * <p>
	 * 私钥 {@link #PRIVATE_KEY privateKey} :
	 * {@link RSAPrivateKey}
	 * </p>
	 * @throws SecureUtilException
	 */
	public static Map<String, Object> genKeyPair(int keySize) throws SecureUtilException {
		if (keySize != KEY_SIZE_1024 && keySize != KEY_SIZE_2048) {
			SecureUtilException sue = new SecureUtilException("暂不支持" + keySize + "位");
			throw sue;
		}
		Map<String, Object> keyPairMap = new HashMap<String, Object>();
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			SecureUtilException sue = new SecureUtilException("生成密钥对发生异常");
			sue.initCause(e);
			throw sue;
		}
		keyPairGen.initialize(keySize);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		keyPairMap.put(PUBLIC_KEY, publicKey);
		keyPairMap.put(PRIVATE_KEY, privateKey);
		return keyPairMap;
	}

	/**
	 * RSA公钥生成
	 *
	 * <pre>
	 * 支持Der, X509, .NET, PEM, SSL格式与公钥
	 * {@link RSAPublicKey}的相互转换
	 * </pre>
	 *
	 * @param modulus        模
	 * @param publicExponent 公钥指数
	 * @return 公钥 {@link RSAPublicKey}
	 * @throws SecureUtilException
	 * @author 丁盛
	 * <p>
	 * <p>
	 * <p>
	 * 从模和指数生成公钥
	 */
	public static RSAPublicKey getRsaPublicKey(BigInteger modulus, BigInteger publicExponent)
			throws SecureUtilException {
		try {
			RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return (RSAPublicKey) kf.generatePublic(spec);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("生成公钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 从Der格式公钥生成公钥
	 * <p>
	 * <pre>
	 * DER格式公钥编码规则解析
	 * 30 SEQUENCE
	 * 81 长编码 89 长度
	 * 02 integer
	 * 81 长编码 81 长度
	 * 00 因为后面第一个字节的第一个bit是1，所以加了一个00.如果后面第一个bit不是0，则不加00
	 * e772...1295
	 * 02 INTEGER
	 * 03 短编码，长度
	 * 01 00 01
	 * </pre>
	 *
	 * @param publicKeyDer Der格式公钥字符串
	 * @return 公钥 {@link RSAPublicKey}
	 * @throws SecureUtilException
	 */
	public static RSAPublicKey generatePublicKeyFromDer(String publicKeyDer) throws SecureUtilException {
		try {
			ASN1InputStream in = new ASN1InputStream(hexString2ByteArr(publicKeyDer));
			org.bouncycastle.asn1.pkcs.RSAPublicKey publicKey = org.bouncycastle.asn1.pkcs.RSAPublicKey
					.getInstance(in.readObject());
			RSAPublicKeySpec spec = new RSAPublicKeySpec(publicKey.getModulus(), publicKey.getPublicExponent());
			KeyFactory kf = KeyFactory.getInstance("RSA");
			if (in != null)
				in.close();
			return (RSAPublicKey) kf.generatePublic(spec);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("生成公钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 从X509格式公钥生成公钥
	 * <p>
	 * <pre>
	 * X509格式公钥编码规则解析
	 * 30 SEQUENCE
	 * 81 长编码 9f 长度
	 * 30 SEQUENCE
	 * 0d 短编码，长度
	 * 06 对象标识符，有一列整数构成，用于确定对象，如算法或属性类型
	 * 09 短编码，长度
	 * 2a 86 48 86 f7 0d 01 01 01 rsa
	 * 05 00 NULL
	 * 03 big string
	 * 81 长编码 8d 长度
	 * 00
	 * 30 SEQUENCE
	 * 81 长编码 89 长度
	 * 02 integer
	 * 81 长编码 81 长度
	 * 00 因为后面第一个字节的第一个bit是1，所以加了一个00.如果后面第一个bit不是0，则不加00
	 * e772...1295
	 * 02 INTEGER
	 * 03 短编码，长度
	 * 01 00 01
	 * </pre>
	 *
	 * @param publicKeyX509 X509格式公钥字符串
	 * @return 公钥 {@link RSAPublicKey}
	 * @throws SecureUtilException
	 */
	public static RSAPublicKey generatePublicKeyFromX509(String publicKeyX509) throws SecureUtilException {
		try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(hexString2ByteArr(publicKeyX509));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return (RSAPublicKey) kf.generatePublic(spec);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("生成公钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 从NET格式公钥生成公钥
	 *
	 * @param publicKeyNet NET格式公钥字符串
	 * @return 公钥 {@link RSAPublicKey}
	 * @throws SecureUtilException
	 */
	public static PublicKey generatePublicKeyFromNet(String publicKeyNet) throws SecureUtilException {
		try {
			BigInteger modulus = new BigInteger(base64Decode(publicKeyNet.substring(
					publicKeyNet.indexOf("<Modulus>") + 9, publicKeyNet.indexOf("</Modulus>")).getBytes()));
			BigInteger publicExponent = new BigInteger(base64Decode(publicKeyNet.substring(
					publicKeyNet.indexOf("<Exponent>") + 10, publicKeyNet.indexOf("</Exponent>")).getBytes()));
			return getRsaPublicKey(modulus, publicExponent);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("生成公钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 从PEM格式公钥生成公钥
	 * <p>
	 * <pre>
	 * PEM格式为将X509格式公钥进行base64编码
	 * </pre>
	 *
	 * @param publicKeyPEM PEM格式公钥字符串
	 * @return 公钥 {@link RSAPublicKey}
	 * @throws SecureUtilException
	 */
	public static PublicKey generatePublicKeyFromPEM(String publicKeyPEM) throws SecureUtilException {
		try {
			// 得到X509格式公钥
			String publicKeyX509 = byteArr2HexString(base64Decode(publicKeyPEM.getBytes()));
			// 得到公钥
			return generatePublicKeyFromX509(publicKeyX509);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("生成公钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 从SSL格式公钥生成公钥
	 * <p>
	 * <pre>
	 * SSL格式公钥编码格式解析
	 * 前11字节为固定值 0007ssh-rsa
	 * 紧接着4个字节表示public exponent所占字节长度
	 * 得到长度后，从后一字节开始读取该长度字节数组作为public exponent的值
	 * 再紧接着4个字节也是一个int值，表示modulus所占字节长度
	 * 再根据长度读取字节数组得到modulus值
	 * </pre>
	 *
	 * @param publicKeySSL SSL格式公钥字符串
	 * @return 公钥 {@link RSAPublicKey}
	 * @throws SecureUtilException
	 */
	public static RSAPublicKey generatePublicKeyFromSSL(String publicKeySSL) throws SecureUtilException {
		try {
			byte[] bytes = base64Decode(publicKeySSL.getBytes());
			// public exponent长度读取起始位置
			int startIndex = 7;
			// public exponent长度
			int len = new BigInteger(Arrays.copyOfRange(bytes, startIndex, startIndex + 4)).intValue();
			// public exponent读取起始位置
			startIndex = startIndex + 4;
			// public exponent
			BigInteger publicExponent = new BigInteger(Arrays.copyOfRange(bytes, startIndex, startIndex + len));
			// modulus长度读取起始位置
			startIndex = startIndex + len;
			// modulus长度
			len = new BigInteger(Arrays.copyOfRange(bytes, startIndex, startIndex + 4)).intValue();
			// modulus读取起始位置
			startIndex = startIndex + 4;
			// modulus
			BigInteger modulus = new BigInteger(Arrays.copyOfRange(bytes, startIndex, startIndex + len));
			// 根据模和指数生成公钥
			return getRsaPublicKey(modulus, publicExponent);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("生成公钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 生成Der格式公钥
	 *
	 * @param publicKey {@link RSAPublicKey}
	 * @return Der格式公钥字符串
	 * @throws SecureUtilException
	 */
	public static String getRsaPublicKeyDer(RSAPublicKey publicKey) throws SecureUtilException {
		try {
			return byteArr2HexString(new org.bouncycastle.asn1.pkcs.RSAPublicKey(publicKey.getModulus(),
					publicKey.getPublicExponent()).toASN1Primitive().getEncoded());
		} catch (IOException e) {
			SecureUtilException sue = new SecureUtilException("生成公钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 生成X509格式公钥
	 *
	 * @param publicKey {@link RSAPublicKey}
	 * @return X509格式公钥字符串
	 */
	public static String getRsaPublicKeyX509(RSAPublicKey publicKey) {
		return byteArr2HexString(publicKey.getEncoded());
	}

	/**
	 * 生成.NET格式公钥
	 *
	 * @param publicKey {@link RSAPublicKey}
	 * @return .NET格式公钥字符串
	 */
	public static String getRsaPublicKeyNet(RSAPublicKey publicKey) {
		StringBuilder sb = new StringBuilder();
		sb.append("<RSAKeyValue>");
		sb.append("<Modulus>").append(base64Encode(publicKey.getModulus().toByteArray()))
				.append("</Modulus>");
		sb.append("<Exponent>").append(base64Encode(publicKey.getPublicExponent().toByteArray()))
				.append("</Exponent>");
		sb.append("</RSAKeyValue>");
		return sb.toString();
	}

	/**
	 * 生成PEM格式公钥
	 *
	 * @param publicKey {@link RSAPublicKey}
	 * @return PEM格式公钥字符串
	 * @throws SecureUtilException
	 */
	public static String getRsaPublicKeyPem(RSAPublicKey publicKey) throws SecureUtilException {
		StringBuilder sb = new StringBuilder();
		sb.append("-----BEGIN PUBLIC KEY-----\n");
		sb.append(new String(base64Encode(hexString2ByteArr(getRsaPublicKeyX509(publicKey)))));
		sb.append("\n-----END PUBLIC KEY-----");
		return sb.toString();
	}

	/**
	 * 将X509格式公钥转换为DER格式公钥
	 *
	 * @param publicKeyX509 X509格式公钥字符串
	 * @return DER格式公钥字符串
	 * @throws SecureUtilException
	 */
	public static String getRsaPublicKeyDerFromX509(String publicKeyX509) throws SecureUtilException {
		try {
			ASN1InputStream in = new ASN1InputStream(hexString2ByteArr(publicKeyX509));
			SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(in.readObject());
			org.bouncycastle.asn1.pkcs.RSAPublicKey publicKey = org.bouncycastle.asn1.pkcs.RSAPublicKey
					.getInstance(info.parsePublicKey());
			if (in != null) {
				in.close();
			}
			return byteArr2HexString(publicKey.toASN1Primitive().getEncoded());
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("转换公钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * RSA私钥生成
	 *
	 * @param modulus         模
	 * @param privateExponent 私钥指数
	 * @return 私钥 {@link RSAPrivateKey}
	 * @throws SecureUtilException
	 * @author 丁盛
	 * <p>
	 * <p>
	 * <p>
	 * 从模和指数生成私钥
	 */
	public static RSAPrivateKey getRsaPrivateKey(BigInteger modulus, BigInteger privateExponent)
			throws SecureUtilException {
		try {
			RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, privateExponent);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) kf.generatePrivate(spec);
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("生成私钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 从私钥字符串生成私钥
	 *
	 * @param privateKey PKCS#8格式的私钥字符串
	 * @return 私钥 {@link RSAPrivateKey}
	 * @throws SecureUtilException
	 */
	public static PrivateKey generatePrivateKey(String privateKey) throws SecureUtilException {
		try {
			// 构造PKCS8EncodedKeySpec对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey.getBytes());
			// 指定的加密算法
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// 取私钥匙对象
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
			return priKey;
		} catch (Exception e) {
			SecureUtilException sue = new SecureUtilException("生成私钥发生异常");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * base64编码
	 *
	 * @param inputByte 字节数组
	 * @return 字节数组
	 */
	public static byte[] base64Encode(byte[] inputByte) {
		return Base64.encode(inputByte);
	}

	/**
	 * base64解码
	 *
	 * @param inputByte 字节数组
	 * @return 字节数组
	 */
	public static byte[] base64Decode(byte[] inputByte) {
		return Base64.decode(inputByte);
	}

	/**
	 * 字节数组转换为十六进制字符串
	 *
	 * @param bytearr 字节数组
	 * @return 十六进制字符串
	 */
	public static String byteArr2HexString(byte[] bytearr) {
		if (bytearr == null) {
			return "null";
		}
		StringBuffer sb = new StringBuffer();

		for (int k = 0; k < bytearr.length; k++) {
			if ((bytearr[k] & 0xFF) < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(bytearr[k] & 0xFF, 16));
		}
		return sb.toString();
	}

	/**
	 * 十六进制字符串转换为字节数组
	 *
	 * @param hexstring 十六进制字符串
	 * @return 字节数组
	 */
	public static byte[] hexString2ByteArr(String hexstring) {
		if ((hexstring == null) || (hexstring.length() % 2 != 0)) {
			return new byte[0];
		}

		byte[] dest = new byte[hexstring.length() / 2];

		for (int i = 0; i < dest.length; i++) {
			String val = hexstring.substring(2 * i, 2 * i + 2);
			dest[i] = (byte) Integer.parseInt(val, 16);
		}
		return dest;
	}

	/**
	 * 哈希算法，值为{@value}
	 */
	public final static String ALGORITHM_HASH_MD5 = "MD5";
	/**
	 * 哈希算法，值为{@value}
	 */
	public final static String ALGORITHM_HASH_SHA256 = "SHA-256";
	/**
	 * 哈希算法，值为{@value}
	 */
	public final static String ALGORITHM_HASH_SHA1 = "SHA-1";

	/**
	 * 编码格式，值为{@value}
	 */
	public final static String CHARSET_UTF8 = "UTF-8";
	/**
	 * 编码格式，值为{@value}
	 */
	public final static String CHARSET_GBK = "GBK";
	/**
	 * 编码格式，值为{@value}
	 */
	public final static String CHARSET_GB2312 = "GB2312";

	/**
	 * 哈希
	 *
	 * @param data      明文数据
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return 密文数据
	 * @throws SecureUtilException
	 */
	public static byte[] hash(byte[] data, String algorithm) throws SecureUtilException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			SecureUtilException sue = new SecureUtilException("不支持" + algorithm + "算法");
			sue.initCause(e);
			throw sue;
		}
		md.update(data);
		return md.digest();
	}

	/**
	 * 哈希，字符串对字符串
	 *
	 * @param data      明文数据，字符串
	 * @param charset   编码格式，支持{@link #CHARSET_UTF8 UTF-8}， {@link #CHARSET_GBK GBK}和
	 *                  {@link #CHARSET_GB2312 GB2312}；也可自定义
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return 如果输入为空，则直接返回；不为空，返回密文数据，十六进制字符串
	 * @throws SecureUtilException
	 */
	public static String hashHex(String data, String charset, String algorithm) throws SecureUtilException {
		if (StringUtils.isEmpty(data)) {
			return data;
		}
		try {
			return byteArr2HexString(hash(data.getBytes(charset), algorithm));
		} catch (UnsupportedEncodingException e) {
			SecureUtilException sue = new SecureUtilException("不支持" + charset + "编码格式");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 哈希，字符串对字符串
	 *
	 * @param data      明文数据，字符串,UTF-8 编码
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return 如果输入为空，则直接返回；不为空，返回密文数据，十六进制字符串
	 * @throws SecureUtilException
	 */
	public static String hashHex(String data, String algorithm) throws SecureUtilException {
		return hashHex(data, CHARSET_UTF8, algorithm);
	}

	/**
	 * 校验哈希值
	 *
	 * @param hashData  密文数据，十六进制字符串
	 * @param data      明文数据
	 * @param charset   编码格式，支持{@link #CHARSET_UTF8 UTF-8}， {@link #CHARSET_GBK GBK}和
	 *                  {@link #CHARSET_GB2312 GB2312}；也可自定义
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return
	 * @throws SecureUtilException
	 */
	public static boolean checkHashHex(String hashData, String data, String charset, String algorithm)
			throws SecureUtilException {
		return hashData.equals(hashHex(data, charset, algorithm));
	}

	/**
	 * 校验哈希值
	 *
	 * @param hashData  密文数据，十六进制字符串，UTF-8编码
	 * @param data      明文数据
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return
	 * @throws SecureUtilException
	 */
	public static boolean checkHashHex(String hashData, String data, String algorithm)
			throws SecureUtilException {
		return hashData.equals(hashHex(data, algorithm));
	}

	/**
	 * 哈希Xor
	 *
	 * @param data      明文数据
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return 密文数据
	 * @throws SecureUtilException
	 */
	public static byte[] xorHash(byte[] data, String algorithm) throws SecureUtilException {
		int len = data.length;
		byte[] descData = new byte[len];
		for (int i = 0; i < len; i++) {
			descData[len - 1 - i] = data[i];
		}
		for (int i = 0; i < len; i++) {
			data[i] = (byte) (data[i] ^ descData[i]);
		}
		return hash(data, algorithm);
	}

	/**
	 * 哈希Xor
	 *
	 * @param data      明文数据
	 * @param charset   编码格式，支持{@link #CHARSET_UTF8 UTF-8}， {@link #CHARSET_GBK GBK}和
	 *                  {@link #CHARSET_GB2312 GB2312}；也可自定义
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return 如果输入为空，则直接返回；不为空，返回密文数据，十六进制字符串
	 * @throws SecureUtilException
	 */
	public static String xorHashHex(String data, String charset, String algorithm) throws SecureUtilException {
		if (StringUtils.isEmpty(data)) {
			return data;
		}
		try {
			return byteArr2HexString(xorHash(data.getBytes(charset), algorithm));
		} catch (UnsupportedEncodingException e) {
			SecureUtilException sue = new SecureUtilException("不支持" + charset + "编码格式");
			sue.initCause(e);
			throw sue;
		}
	}

	/**
	 * 哈希Xor
	 * <p>
	 * <pre>
	 * 默认编码格式为{@link #CHARSET_UTF8 UTF-8}
	 * </pre>
	 *
	 * @param data      明文数据，UTF-8编码
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return 如果输入为空，则直接返回；不为空，返回密文数据，十六进制字符串
	 * @throws SecureUtilException
	 */
	public static String xorHashHex(String data, String algorithm) throws SecureUtilException {
		return xorHashHex(data, CHARSET_UTF8, algorithm);
	}

	/**
	 * 校验哈希值Xor
	 *
	 * @param hashData  密文数据，十六进制字符串
	 * @param data      明文数据
	 * @param charset   编码格式，支持{@link #CHARSET_UTF8 UTF-8}， {@link #CHARSET_GBK GBK}和
	 *                  {@link #CHARSET_GB2312 GB2312}；也可自定义
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return
	 * @throws SecureUtilException
	 */
	public static boolean checkXorHashHex(String hashData, String data, String charset, String algorithm)
			throws SecureUtilException {
		return hashData.equals(xorHashHex(data, charset, algorithm));
	}

	/**
	 * 校验哈希值Xor
	 * <p>
	 * <pre>
	 * 默认编码格式为{@link #CHARSET_UTF8 UTF-8}
	 * </pre>
	 *
	 * @param hashData  密文数据，十六进制字符串
	 * @param data      明文数据
	 * @param algorithm 哈希算法，支持{@link #ALGORITHM_HASH_MD5 MD5}，
	 *                  {@link #ALGORITHM_HASH_SHA256 SHA-256}和
	 *                  {@link #ALGORITHM_HASH_SHA1 SHA-1}；也可自定义
	 * @return
	 * @throws SecureUtilException
	 */
	public static boolean checkXorHashHex(String hashData, String data, String algorithm) throws SecureUtilException {
		return hashData.equals(xorHashHex(data, algorithm));
	}


	/**
	 * 将对象转换成map
	 *
	 * @param bean
	 * @return
	 */
	public static <T> Map<String, String> beanToMap(T bean) {
		String s = JSONObject.toJSONString(bean);
		HashMap hashMap = JSONObject.parseObject(s, HashMap.class);
		return hashMap;
	}

	/**
	 * 获取签名裸数据
	 *
	 * @param superDto
	 */
	public static String getReturnRawData(SuperDto superDto) {
		Map<String, String> srcMap = beanToMap(superDto);
		List<String> sortKeys = new ArrayList<String>(srcMap.keySet());
		Collections.sort(sortKeys);
		StringBuffer sb = new StringBuffer();
		for (String key : sortKeys) {
			if (key.equals("signature") || isEmpty(srcMap.get(key))) {
				continue;
			}

			sb.append(key);
			sb.append("=");
			sb.append(srcMap.get(key));
			sb.append("&");
		}
		String rawData = sb.toString();
		return rawData.substring(0, rawData.length() - 1);
	}

	public static boolean validate(SuperDto superDto) {
		logger.info("========开始验签===========");
		System.out.println("result:" + superDto);
		try {
			String signature = superDto.getSignature();
			logger.info("resp signature：{}");
			logger.info("========生成rawData===========");
			String rawDataStr = getReturnRawData(superDto);
			logger.info("rawData：{}", rawDataStr);
			logger.info("========生成验签结果===========");
			Boolean flag = verify(rawDataStr.getBytes(), hexString2ByteArr(signature), pubKey, ALGORITHM_SHA256WITHRSA);
			logger.info("验签结果：{}", flag);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 哈希加密
	 *
	 * @param strSrc  需要加密的字符串
	 * @param encName 需要采用的加密方式，如：SHA-1,SHA-256,SHA-256等等
	 * @return
	 */
	public static String Encrypt(String strSrc, String encName) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
			System.out.println(strDes);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
			return null;
		}
		return strDes;
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}


	/**
	 * @title:获取指定位数的随机数，可指定为纯数字或数字字母组合
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/8/7 10:53
	 * @param:
	 * @return:
	 */
	public static String getRandomCharAndNumber(Integer length, boolean isNumber) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			boolean b = random.nextBoolean();
			// 字符串
			if (b) {
				if (isNumber) {
					str += String.valueOf(random.nextInt(10));
				} else {
					// 取得大写字母
					str += (char) (65 + random.nextInt(26));
				}
			} else {
				// 数字
				str += String.valueOf(random.nextInt(10));
			}
		}
		return str;
	}


	/**
	 * @title:获取流水号
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/8/7 10:53
	 * @param:
	 * @return:
	 */
	public static String getSerialNumber(String suffix) {
		String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String t = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
		String s = getRandomCharAndNumber(4, true);
		String e = getRandomCharAndNumber(4, true);
		if (StringUtils.isBlank(suffix)) {
			return d + s + t + e;
		} else {
			return suffix + d + s + t + e;
		}
	}
}
