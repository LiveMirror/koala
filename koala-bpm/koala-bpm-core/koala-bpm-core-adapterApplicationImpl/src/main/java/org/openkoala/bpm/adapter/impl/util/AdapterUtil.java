package org.openkoala.bpm.adapter.impl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.drools.runtime.process.ProcessContext;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.openkoala.bpm.adapter.application.applicationInterface.vo.JbpmAdapterParameter;
import org.openkoala.bpm.adapter.application.util.MapSerializeUtil;

/**
 * 远程方法调用实现类
 * 
 * @author lingen
 * 
 */
public class AdapterUtil {

	/**
	 * 调用业务适配器，不返回任何
	 * 
	 * @param context
	 * @param method
	 * @param strings
	 * @throws Exception 
	 */
	public static void invoke(ProcessContext context, String name,
			String... strings) throws Exception {
//		String type = "voidInvoke";
		List<String> userParams = parseString(strings);
		byte[] params = parseByte(context);
		JbpmAdapterParameter adapter = new JbpmAdapterParameter(name,params,userParams);
		
		AdapterClient.getInstance("127.0.0.1", 9123).connectToServer(context, adapter);
	}

	
	private static List<String> parseString(String... strings) {
		List<String> params = new ArrayList<String>();
		for (String string : strings) {
			params.add(string);
		}
		return params;
	}
	
	private static byte[] parseByte(ProcessContext context) {
		RuleFlowProcessInstance in = (RuleFlowProcessInstance) context
				.getProcessInstance();
		Map<String, Object> vas = in.getVariables();
		byte[] bytes = MapSerializeUtil.serialize(vas);
		return bytes;
	}
}
