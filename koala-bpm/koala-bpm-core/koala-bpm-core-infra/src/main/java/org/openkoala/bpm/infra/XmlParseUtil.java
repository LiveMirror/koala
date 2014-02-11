package org.openkoala.bpm.infra;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * XML解析的类
 * 
 * @author lingen
 * 
 */
public class XmlParseUtil {

	/**
	 * 把返回的XML值解析成List值的模式
	 * 
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> parseListXml(String xml) {
		SAXReader reader = new SAXReader();
		Document document = null;
		List<String> values = new ArrayList<String>();
		if(xml==null || xml.trim().equals("")){
			return values;
		}
		try {
			document = reader.read(new ByteArrayInputStream(xml
					.getBytes("UTF-8")));
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			for (Element element : elements) {
				values.add(element.getTextTrim());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return values;
	}

	/**
	 * 将指定的参数解析成一个XML格式
	 * 
	 * @param paramsMap
	 * @return
	 */
	public static String paramsToXml(Map<String, Object> paramsMap) {
		if (paramsMap == null || paramsMap.size() == 0)
			return "";
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("params");
		Set<String> keys = paramsMap.keySet();
		for (String key : keys) {
			Object value = paramsMap.get(key);
			if(key.startsWith("~KJ")){
				continue;
			}
			Element param = root.addElement(key);
			if (value instanceof Integer) {
				param.addAttribute("type", "int");
			}
			if (value instanceof Long) {
				param.addAttribute("type", "long");
			}
			if (value instanceof Float) {
				param.addAttribute("type", "float");
			}
			if (value instanceof Double) {
				param.addAttribute("type", "double");
			}
			if (value instanceof Boolean) {
				param.addAttribute("type", "boolean");
			}
			param.setText(value.toString());
		}
		return document.asXML();
	}

	/**
	 * 将传入的XML格式，解析成为
	 * 
	 * @param content
	 * @return
	 */
	public static Map<String, Object> xmlToPrams(String content) {
		return xmlToPrams(content, null);
	}
	
	/**
	 * 将传入的XML内容根据一定的元素名称过滤规则转换成Map
	 * @param content
	 * @param filter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> xmlToPrams(String content, ElementFilter filter) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (content == null || "".equals(content)) {
			return params;
		}
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new ByteArrayInputStream(content.getBytes("UTF-8")));
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			for (Element element : elements) {
				String key = element.getName();
				String value = element.getTextTrim();
				Object val = value;
				if (filter != null) {
					if (key.matches(filter.getElementName())) {
						val = handleTypeAttribute(element, value, val);
						params.put(key, val);
					}
				} else {
					val = handleTypeAttribute(element, value, val);
					params.put(key, val);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return params;
	}

	private static Object handleTypeAttribute(Element element, String value, Object val) {
		if (element.attribute("type") != null) {
			if ("int".equals(element.attribute("type").getText())) {
				val = Integer.parseInt(value);
			}
			if ("long".equals(element.attribute("type").getText())) {
				val = Long.parseLong(value);
			}
			if ("float".equals(element.attribute("type").getText())) {
				val = Float.parseFloat(value);
			}
			if ("double".equals(element.attribute("type").getText())) {
				val = Double.parseDouble(value);
			}
			if ("boolean".equals(element.attribute("type").getText())) {
				val = Boolean.valueOf(value);
			}
		}
		return val;
	}
}