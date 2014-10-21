package org.openkoala.bpm.adapter.application.applicationInterface;

import java.util.List;
import java.util.Map;

public interface JbpmAdapterInvoke {

	/**
	 * 
	 * @param params
	 * @param strings
	 * @return
	 */
	public Object invoke(Map<String,Object> contextParams,List<String> userParams);
	
}
