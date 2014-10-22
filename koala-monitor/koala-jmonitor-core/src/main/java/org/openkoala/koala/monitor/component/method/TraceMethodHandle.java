package org.openkoala.koala.monitor.component.method;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openkoala.koala.monitor.constant.E_TraceType;
import org.openkoala.koala.monitor.core.RuntimeContext;
import org.openkoala.koala.monitor.core.TraceLiftcycleManager;
import org.openkoala.koala.monitor.def.MethodTrace;
import org.openkoala.koala.monitor.toolkit.asm.MethodInjectHandler;

/**
 * 监听方法调用的handle，在MethodComponent中对配置的包以及类的方法调用进行监听
 * @author leadyu
 * @since Jwebap 0.5
 * @date  2007-8-16
 */
public class TraceMethodHandle implements MethodInjectHandler {
	
	private static Log log = LogFactory.getLog(TraceMethodHandle.class);
	
	/**
	 * 运行轨迹容器
	 */
	private  transient TraceLiftcycleManager _container=null;
	
	public TraceMethodHandle(TraceLiftcycleManager container) {
		_container=container;
	}

	private TraceLiftcycleManager getContainer(){
		return _container;
	}
	
	public Object invoke(Object target, Method method, Method methodProxy, Object[] args)throws Throwable{
		Object o;
		MethodTrace trace = null;
		String mod = Modifier.toString(method.getModifiers());
		boolean doTrace = mod.indexOf("public")>=0 && mod.indexOf("static")<0;
		try {
			try {
				if(doTrace){
					boolean traceStack = Boolean.parseBoolean(RuntimeContext.getComponentProps(E_TraceType.METHOD.name(), "trace-stack"));
					trace = new MethodTrace(target, method, args,traceStack);
					getContainer().activateTrace(E_TraceType.METHOD.name(),trace);
				}
				
			} catch (Exception e) {
				doTrace = false;
				//log.warn(e.getMessage());
			}
			
			o = methodProxy.invoke(target, args);
			if(doTrace)trace.setSuccessed(true);
		}catch(InvocationTargetException e){
			if(doTrace){
				trace.setSuccessed(false);
				trace.setStack(e);
			}
			//抛出原有异常
			throw e.getCause(); 
		}finally {
			if(doTrace){
				try {
					getContainer().inactivateTrace(E_TraceType.METHOD.name(),trace);
				} catch (Exception e) {
					log.warn("["+method.getName() + "]监控代码未正常结束");
				}
			}
		}

		return o;

	}

}

