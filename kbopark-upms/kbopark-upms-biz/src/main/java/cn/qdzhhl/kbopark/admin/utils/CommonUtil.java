package cn.qdzhhl.kbopark.admin.utils;

import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @ClassName CommonUtil
 * @Description TODO 通用工具类，正则规则校验、获取随机字符串等
 * @Author sunwenzhi
 * @Date 2019/9/2 10:22
 **/
@Component
public class CommonUtil {

	/**默认密码**/
	public static final String DEFAULT_PASS_WORD = "123456";

	/**
	 * [用户名]只支持6-20位中英文数字下划线组合
	 */
	private static Pattern USERNAME_PATTERN = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9]{6,20}$");
	/**
	 * [密码]只支持6-20位英文数字组合
	 */
	private static Pattern PASSWORD_PATTERN = Pattern.compile("^[A-Za-z0-9]{6,20}$");
	/**
	 * [邮箱]邮箱格式 XXXX@XX.com
	 */
	private static Pattern EMAIL_PATTERN = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	/**
	 * [正整数或者0]数字格式校验
	 */
	private static Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
	/**
	 * [正浮点数或者0]数字格式校验
	 */
	private static Pattern FLOAT_PATTERN = Pattern.compile("^[0-9]\\d*\\.\\d*|0\\.\\d*[0-9]\\d*$ ");
	/**
	 * [手机号]严谨格式校验
	 */
	private static Pattern PHONE_NUMBER_REG = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");
	/**
	 * [手机号]宽泛格式校验
	 */
	private static Pattern PHONE_NUMBER_ALL = Pattern.compile("^(1[3-9])\\d{9}$");


	/**
	 * @Title:转换字符串
	 * @Description:非空判断并转成自定义字符串
	 * @Date: 2019/1/10 16:05
	 * @param:
	 * @return:
	 */
	public static String translateNull(String str, String customString) {
		if (StringUtils.isEmpty(str)) {
			return customString;
		}
		return str;
	}


	/**
	 * @Title:获取随机字符串
	 * @Description:UUID得到一个32位随机码
	 * @Date: 2019/1/11 16:07
	 * @param:
	 * @return:
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}


	/**
	 * @Title:获取客户端的IP地址
	 * @Description:
	 * @Date: 2019/1/9 9:27
	 * @param: request
	 * @return:
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * @Title:校验用户名
	 * @Description:只支持6-20位中英文数字下划线组合
	 * @param: name
	 * @return: boolean
	 */
	public static boolean checkName(String name) {
		if (name != null) {
			return USERNAME_PATTERN.matcher(name).matches();
		}
		return false;
	}


	/**
	 * @Title:校验密码
	 * @Description:只支持6-20位英文数字组合
	 * @param: password
	 * @return: boolean
	 */
	public static boolean checkPassword(String password) {
		if (password != null) {
			return PASSWORD_PATTERN.matcher(password).matches();
		}
		return false;
	}


	/**
	 * @Title:校验邮箱
	 * @Description:邮箱格式 XXXX@XX.com
	 * @param: email
	 * @return: boolean
	 */
	public static boolean checkEmail(String email) {

		if (email != null) {
			return EMAIL_PATTERN.matcher(email).matches();
		}
		return false;
	}


	/**
	 * @Title:校验数字
	 * @Description:数字格式,正整数、0、正浮点数
	 * @param: number
	 * @return: boolean
	 */
	public static boolean checkNumber(String number) {

		if (number != null) {
			return NUMBER_PATTERN.matcher(number).matches() || FLOAT_PATTERN.matcher(number).matches();
		}
		return false;
	}


	/**
	 * @title:校验手机号，严谨验证
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/9/3 9:38
	 * @param:
	 * @return:
	 **/
	public static boolean checkPhoneTypeOne(String phone) {

		if (phone != null) {
			return PHONE_NUMBER_REG.matcher(phone).matches();
		}
		return false;
	}


	/**
	 * @title:校验手机号，宽泛验证
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/9/3 9:38
	 * @param:
	 * @return:
	 **/
	public static boolean checkPhoneTypeSecond(String phone) {

		if (phone != null) {
			return PHONE_NUMBER_ALL.matcher(phone).matches();
		}
		return false;
	}


	/**
	 * @Title:根据Base64字符串判断源文件的大小
	 * @Description:
	 * @param: image
	 * @return: Integer
	 */
	public static Integer getImgSizeByBase64(String image) {
		if (StringUtils.isEmpty(image)) {
			return 0;
		}
		String t = "=";
		String str = image.substring(22);
		Integer equalIndex = str.indexOf(t);
		if (str.indexOf(t) > 0) {
			str = str.substring(0, equalIndex);
		}
		Integer strLength = str.length();
		//计算后得到的文件流大小，单位为字节
		Integer size = strLength - (strLength / 8) * 2;
		return size;
	}


	/**
	 * @Title:加法
	 * @Description:
	 * @Date: 2019/2/22 17:08
	 * @param:
	 * @return:
	 */
	public static double add(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.add(b2).doubleValue();
	}


	/**
	 * @Title: 减法
	 * @Description:
	 * @Date: 2019/2/22 17:08
	 * @param:
	 * @return:
	 */
	public static double sub(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.subtract(b2).doubleValue();
	}


	/**
	 * @Title:乘法
	 * @Description:
	 * @Date: 2019/2/22 17:08
	 * @param:
	 * @return:
	 */
	public static double mul(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.multiply(b2).doubleValue();
	}


	/**
	 * @Title:除法
	 * @Description:
	 * @Date: 2019/2/22 17:09
	 * @param:
	 * @return:
	 */
	public static double div(double d1, double d2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}


	/**
	 * @title:保留几位小数
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/8/7 10:54
	 * @param:
	 * @return:
	 */
	public static double round(double d1, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal("1");
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}


	/**
	 * @title:获取指定范围内的随机数
	 * @author:sunwenzhi
	 * @description:
	 * @date: 2019/8/7 10:54
	 * @param:
	 * @return:
	 */
	public static int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();
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


	/**
	 * @title:增加天数
	 * @author:sunwenzhi
	 * @description:时间运算，给定开始时间，返回加多少天后的时间
	 * @date:2019/8/26 17:02
	 * @param:
	 * @return:
	 **/
	public static Date addDays(Date startTime, int day) {
		if (startTime == null) {
			return null;
		}
		LocalDateTime localDateTime = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
		LocalDateTime plusDay = localDateTime.plusDays(day);
		return Date.from(plusDay.atZone(ZoneId.systemDefault()).toInstant());
	}


	/**
	 * 判断是否是超级管理员
	 *
	 * @param user
	 * @return
	 */
	public static boolean isSuperManager(KboparkUser user) {
		if(user == null){
			return false;
		}
		if(user.getDeptId().intValue() == 1){
			return true;
		}
		return false;
	}


}
