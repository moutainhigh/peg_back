package com.kbopark.operation.thirdplatform.utils;

import com.kbopark.operation.thirdplatform.entity.ResultMap;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import lombok.experimental.UtilityClass;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/26 14:35
 **/
@UtilityClass
public class XmlUtil {

	public String head = "<?xml version=\"1.0\" encoding=\"utf-8\">";

	/**
	 * 接收到青小岛请求后返回的数据
	 * @param success
	 * @return
	 */
	public String getResultXml(boolean success){
		ResultMap resultMap = success ? ResultMap.success() : ResultMap.fail();
		XStream xStream = new XStream(new Xpp3Driver(new NoNameCoder()));
		xStream.alias("root", ResultMap.class);
		XStream.setupDefaultSecurity(xStream);
		xStream.allowTypes(new Class[]{ResultMap.class});
		String param = xStream.toXML(resultMap);
		return head + param;
	}
}
