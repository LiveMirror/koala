package org.openkoala.jbpm.infra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dayatang.utils.Assert;
import org.junit.Test;
import org.openkoala.bpm.infra.XmlParseUtil;

public class XmlParseUtilTest {

	@Test
	public void testParseListXml() {
		 String xml = "<params><creater>lingen</creater><isOpen type=\"boolean\">true</isOpen></params>";
		 List<String> values = XmlParseUtil.parseListXml(xml);
		 Assert.isTrue(values.size()==2);
	}

	@Test
	public void testParamsToXml() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("creater", "lingen");
		params.put("isOpen", true);
		params.put("count", 2);
		String xml = XmlParseUtil.paramsToXml(params);
		System.out.println(xml);
		String resultXml="<params><count type=\"int\">2</count><creater>lingen</creater><isOpen type=\"boolean\">true</isOpen></params>";
		
		Assert.isTrue(xml.endsWith(resultXml));
	}

	@Test
	public void testXmlToPrams() {
		String resultXml="<params><count type=\"int\">2</count><creater>lingen</creater><isOpen type=\"boolean\">true</isOpen></params>";
		Map<String,Object> params = XmlParseUtil.xmlToPrams(resultXml);
		Assert.isTrue(params.get("count") instanceof Integer);
	}

}
