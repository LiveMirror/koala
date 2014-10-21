package org.openkoala.koala.monitor.analyser;

import org.openkoala.koala.monitor.constant.E_TraceType;
import org.openkoala.koala.monitor.core.Analyser;
import org.openkoala.koala.monitor.core.RuntimeContext;
import org.openkoala.koala.monitor.def.HttpRequestTrace;
import org.openkoala.koala.monitor.def.Trace;
/**
 * 
 * 功能描述：会话分析统计处理器<br />
 *  
 * 创建日期：2013-7-1 上午11:07:20  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class SessionFilterAnalyser implements Analyser{
	
	protected String traceType;


	public SessionFilterAnalyser(String traceType) {
		this.traceType = traceType;
	}


	@Override
	public void activeProcess(Trace trace) {
		if(E_TraceType.HTTP.name().equals(trace.getTraceType())){
			//刷新在线人数
			HttpRequestTrace _trace = (HttpRequestTrace)trace;
			RuntimeContext.getContext().getDataCache().refreshActiveSessions(_trace);
		}
	}


	@Override
	public void inactiveProcess(Trace trace) {}


	@Override
	public void destoryProcess(Trace trace) {}


	@Override
	public void clear() {}
	
	
}