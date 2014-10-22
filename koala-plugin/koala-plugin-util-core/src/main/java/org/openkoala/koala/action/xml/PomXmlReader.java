package org.openkoala.koala.action.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openkoala.koala.exception.JavaDoException;
import org.openkoala.koala.java.JavaManagerUtil;
import org.openkoala.koala.pojo.Dependency;
import org.openkoala.koala.pojo.MavenProject;

/**
 * 更新POM.xml的辅助类，提供删除一个dependency,新增一个dependency的功能
 * @author lingen.liu
 *
 */
public class PomXmlReader {

	public static final String POM_XMLS = "http://maven.apache.org/POM/4.0.0";
	
	/**
	 * 查询一个POM.XML中的properties配置
	 * @param document
	 * @return
	 */
	public static Map<String,String> queryProperties(Document document){
		Map<String,String> properties = new HashMap<String,String>();
		Element element = XPathQueryUtil.querySingle(POM_XMLS, "/xmlns:project/xmlns:properties", document);
		if(element==null)return properties;
		List<Element> childs = element.elements();
		for(Element child:childs){
			properties.put(child.getName(), child.getTextTrim());
		}
		return properties;
	}
	
	/**
	 * 查询一个POM中配置的modules
	 * @param document
	 * @return
	 */
	public static List<String> queryModules(Document document){
		List<String> modules = new ArrayList<String>();
		List<Element> elements = XPathQueryUtil.query(POM_XMLS, "/xmlns:project/xmlns:modules/xmlns:module", document);
		for(Element element:elements){
			modules.add(element.getText());
		}
		return modules;
	}
	
	/**
	 * 返回一个pom.xml中的依赖
	 * @param document
	 * @return
	 */
	public static List<Dependency> queryDependency(Document document){
		List<Dependency> dependencies = new ArrayList<Dependency>();
		List<Element> elements = XPathQueryUtil.query(POM_XMLS, "/xmlns:project/xmlns:dependencies/xmlns:dependency", document);
		for(Element element:elements){
			Dependency dependency = new Dependency(element.elementText("groupId"),element.elementText("artifactId"),element.elementText("version"));
			dependencies.add(dependency);
		}
		return dependencies;
	}
	
	/**
	 * 读取指定文档中指定的元素的TEXT值，元素是唯一的
	 * @param elementName
	 * @param document
	 * @return
	 */
	public static String queryText(String xPathString,Document document){
		String returnString = null;
		Element element = XPathQueryUtil.querySingle(POM_XMLS, xPathString, document);
		if(element!=null)returnString = element.getTextTrim();
		return returnString;
	}
	
	/**
	 * 读取指定文档中指定的元素的TEXT值，元素不是唯一的
	 * @param elementName
	 * @param document
	 * @return
	 */
	public static List<String> queryListText(String xPathString,Document document){
		List<String> list = new ArrayList<String>();
		
		List<Element> elements = XPathQueryUtil.query(POM_XMLS, xPathString, document);
		for(Element element:elements){
			list.add(element.getTextTrim());
		}
		return list;
	}
	
	/**
	 * 查询对应groupId及artifactId的depency是否存在
	 * @param groupId
	 * @param artifactId
	 * @return
	 */
	public static boolean isDependencyExists(String groupId,String artifactId,Document document){
		boolean exists =false;
		String xPathString = "/xmlns:project/xmlns:dependencies/xmlns:dependency[xmlns:groupId='"+groupId+"' and xmlns:artifactId='"+artifactId+"']";
		List<Element> elements = XPathQueryUtil.query(POM_XMLS, xPathString, document);
		if(elements!=null && elements.size()>0)exists=true;
		return exists;
	}

	/**
	 * 是否是领域层
	 * @param document
	 * @return
	 */
	public static boolean isBizModel(Document document){
		return isDependencyExists("com.dayatang.commons","dayatang-commons-domain",document);
	}
	
	/**
	 * 通过XML查询一个子项目是否是应用层实现模块
	 * @param document
	 * @return
	 */
	public static boolean isImpl(Document document){
		return isDependencyExists("com.dayatang.commons","dayatang-commons-querychannel",document);
	}
	
	/**
	 * 通过XML查询一个子项目是否是门面层实现模块
	 * @param document
	 * @return
	 */
	public static boolean isFacadeImpl(Document document){
		return isDependencyExists("com.dayatang.commons","dayatang-commons-querychannel",document);
	}
	
	/**
	 * 查询一个子项目是否是EAR模块
	 * @param document
	 * @return
	 */
	public static boolean isEar(Document document){
		String xPathString = "/xmlns:project/xmlns:build/xmlns:plugins/xmlns:plugin/xmlns:dependency[xmlns:groupId='org.apache.maven.plugins' and xmlns:artifactId='maven-ear-plugin']";
		List<Element> elements = XPathQueryUtil.query(POM_XMLS, xPathString, document);
		if(elements!=null && elements.size()>0)return true;
		return false;
	}
	
	/**
	 * 查询一个子项目是否是接口模块
	 * @param project
	 * @return
	 * @throws JavaDoException
	 */
	public static boolean isInterface(MavenProject project) throws JavaDoException{
		List<String> files = project.getSrcMainJavas();
		int max = files.size();
		//只取11个JAVA类来判断，以查证其是否是接口层
		if(max>11)max=11;
		int count = 0;
		for(int i=0;i<max;i++){
			if(JavaManagerUtil.isInterface(project.getPath()+"/"+project.getName()+"/"+files.get(i)))count++;
		}
		if(count>(max-count))return true;
		return false;
	}
	
	/**
	 * 查询一个子项目是否是门面层接口模块
	 * @param project
	 * @return
	 * @throws JavaDoException
	 */
	public static boolean isFacadeInterface(MavenProject project) throws JavaDoException{
		List<String> files = project.getSrcMainJavas();
		int max = files.size();
		//只取11个JAVA类来判断，以查证其是否是接口层
		if(max>11)max=11;
		int count = 0;
		for(int i=0;i<max;i++){
			if(JavaManagerUtil.isInterface(project.getPath()+"/"+project.getName()+"/"+files.get(i)))count++;
		}
		if(count>(max-count))return true;
		return false;
	}
	
	
}