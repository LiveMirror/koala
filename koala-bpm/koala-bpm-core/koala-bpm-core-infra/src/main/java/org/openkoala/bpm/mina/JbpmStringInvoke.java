package org.openkoala.bpm.mina;

import java.util.Map;

public interface JbpmStringInvoke {

	/**
	 * 
	 * @param params
	 * @param strings
	 * @return
	 */
	public String invoke(Map<String,String> params,String ...strings);
	
}
