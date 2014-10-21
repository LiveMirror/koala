package org.openkoala.koala.action.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * properties配置文件的读，写，修改操作的辅助类
 * 
 * @author lingen
 * 
 */
public class PropertiesUtil {

	/**
	 * properties所在的路径
	 */
	private String propertyPath;

	private PropertiesUtil() {
	}

	private Properties properties;

	/**
	 * 工厂方法，根据路径返回一个propertiesUtil实例
	 * @param path
	 * @return
	 */
	public static PropertiesUtil getInstance(String path) {
		PropertiesUtil util = new PropertiesUtil();
		Properties properties = new Properties();
		try {
			
			properties.load(new FileInputStream(path));
			util.propertyPath = path;
			util.properties = properties;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return util;
	}

	/**
	 * 读取Properties值
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 修改某个KEY-VALUE值
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	/**
	 * 在文件中新增KEY-VALUE值
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	/**
	 * 删除Properties中的某个KEY值
	 * @param key
	 */
	public void removeProperty(String key){
		properties.remove(key);
	}

	/**
	 * 对修改后的properties进行重建
	 */
	public void restore() {
		OutputStream out = null;
		try {
			out = new FileOutputStream(propertyPath);
			properties.store(out, "auto modify");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
