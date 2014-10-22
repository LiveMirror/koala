package org.openkoala.bpm.mina;

import java.util.Map;

public interface JbpmMapInvoke {

	/**
	 * 
	 * @param params
	 * @param strings
	 * @return
	 */
	public Map<String,String> invoke(Map<String,String> params,String ...strings);
	
}
