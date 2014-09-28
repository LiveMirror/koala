package org.openkoala.koala.annotation.parse;

import java.util.List;

import org.openkoala.koala.action.XmlParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseGeneratedSourceCode implements Parse {

	private static final Logger logger = LoggerFactory
			.getLogger(ParseObjectFunctionCreate.class);

	public String name;

	public List params;
	
	private  Boolean fieldVal;
	
	private String xmlPath;
	
	public void initParms(List params, String name, Object fieldVal) {
		this.params = params;
		this.name = name;
		this.fieldVal = (Boolean)fieldVal;
	}

	public void process() throws Exception {
		logger.info("解析类对象【" + name + "】");
		name = name.replaceAll("GenerateSourceCode", "");
		if(fieldVal ){
			 xmlPath = "xml/ObjectFunctionCreate/" + name + "-generate-source-code.xml";
		}else{
			 xmlPath = "xml/ObjectFunctionCreate/" + name + "-not-generate-source-code.xml";
		}
		XmlParseUtil.parseXml(xmlPath, params);
		logger.info("类对象【" + name + "】解析成功");
	} 
}
