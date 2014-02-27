/*
 * Copyright (c) Koala 2012-2014 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.koala.monitor.service.local;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.openkoala.koala.monitor.core.DataPolicyHandler;
import org.openkoala.koala.monitor.core.MonitorTask;
import org.openkoala.koala.monitor.core.RuntimeContext;
import org.openkoala.koala.monitor.def.ComponentDef;
import org.openkoala.koala.monitor.def.HttpRequestTrace.ActiveUser;
import org.openkoala.koala.monitor.def.NodeDef;
import org.openkoala.koala.monitor.def.Trace;
import org.openkoala.koala.monitor.domain.MonitorNode;
import org.openkoala.koala.monitor.domain.MonitorNode.MonitorComponent;
import org.openkoala.koala.monitor.extend.BaseSchedulerBean;
import org.openkoala.koala.monitor.model.GeneralMonitorStatusVo;
import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;
import org.openkoala.koala.monitor.model.ServerStatusVo;
import org.openkoala.koala.monitor.service.MonitorDataService;
import org.openkoala.koala.monitor.support.JdbcPoolStatusCollector;
import org.openkoala.koala.monitor.support.ServerStatusCollector;
import org.openkoala.koala.monitor.task.ServerStatusCollectorTask;
import org.openkoala.koala.monitor.task.ServiceConnectionCheckTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * 功能描述：<br />
 * 
 * 创建日期：2013-5-30 上午10:17:26 <br />
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作 者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者 修改日期 文件版本 修改说明
 */
public class LocalDataPolicyHandler extends BaseSchedulerBean implements DataPolicyHandler{

	private static final Logger LOG = LoggerFactory.getLogger(LocalDataPolicyHandler.class);

	protected RuntimeContext context;
	
	@Inject
	private MonitorDataService monitorDataService;

	
	//TODO
	MonitorNode node;
	boolean nodeInitOk = false;
	public void startup() {
		try {
			this.context = RuntimeContext.getContext();
			NodeDef nodedef = context.getNodeDef();
			//final MonitorNode node = new MonitorNode();
			node = new MonitorNode();
			node.setNodeId(nodedef.getId());
			node.setNodeName(nodedef.getName());
			node.setNodeUri(nodedef.getUri());
			node.setLatestSessionTime(new Date());
			node.setActive(true);
			List<ComponentDef> components = nodedef.getComponents();
			for (ComponentDef compDef : components) {
				node.getConponents().add(new MonitorComponent(compDef));
			}
			//TODO 升级dddlib 4之后此段代码不能正常执行 节点无法初始化
			/*transactionTemplate.execute(new TransactionCallback<Object>() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					try {
						node.active();
					} catch (Exception e) {
						repository.save(node);
					}
					return null;
				}
			});*/
		} catch (Exception e) {
			LOG.error("数据同步服务启动失败["+e.getMessage() + "]");
		}
		
	}
	
	
	

	@Override
	@PostConstruct
	public void onStart() {
		super.onStart();
		startup();
	}


	@Override
	public void doJob() throws Exception {
		//TODO 临时采用这种方式进行节点初始化
		if(!nodeInitOk){
			transactionTemplate.execute(new TransactionCallback<Object>() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					node.active();
					return null;
				}
			});
			nodeInitOk = true;
		}
		
		if(LOG.isDebugEnabled())LOG.debug("====开始同步监控数据===");
		Map<String, List<Trace>> traces = RuntimeContext.getContext().getDataCache().getAllCacheTrace();
		try {
			Iterator<List<Trace>> iterator = traces.values().iterator();
			while(iterator.hasNext()){
				boolean saveOk = saveMonitorData(iterator.next());
				if(saveOk){
					iterator.remove();
				}
			}
		} catch (Exception e) {
			LOG.error("保存数据失败",e);
		}finally{
			MonitorNode.refreshNodeLastActiveTime();
			//未同步成功的
			if(traces.size()>0)RuntimeContext.getContext().getDataCache().pushAll(traces);
			if(LOG.isDebugEnabled())LOG.debug("=====完成同步监控数据========");
		}
	}


	/**
	 * @param next
	 */
	private boolean saveMonitorData(List<Trace> traces) {
		try {
			monitorDataService.saveMonitorData(context.getNodeDef().getId(),traces);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void updateComponentConfig(String clientId,ComponentDef comp) {
		RuntimeContext.getContext().getDefManager().updateComponentConfig(comp);
	}

	@Override
	public ServerStatusVo getServerStatusInfo(String nodeId) {
		return ServerStatusCollector.getServerAllStatus();
	}



	@Override
	public GeneralMonitorStatusVo getGeneralMonitorStatus(String nodeId) {
		GeneralMonitorStatusVo vo = new GeneralMonitorStatusVo();
		//在线人数
		Map<String, ActiveUser> users = RuntimeContext.getContext().getDataCache().getActiveSessions();
		//恶意访问IP
		Iterator<ActiveUser> it = users.values().iterator();
		while(it.hasNext()){
			ActiveUser user = it.next();
			if(user.isAbNormal())vo.getAbnormalIps().add(user.getIp());
		}
		vo.setActiveCount(users.size());
		//服务器状态
		MonitorTask task = RuntimeContext.getContext().getMonitorTask(ServerStatusCollectorTask.TASK_KEY);
		if(task != null){
			vo.setServerStatusDatas(task.getDatas());
		}
		//第三方服务
		task = RuntimeContext.getContext().getMonitorTask(ServiceConnectionCheckTask.TASK_KEY);
		if(task != null){
			vo.setServiceCheckDatas(task.getDatas());
		}
		return vo;
	}




	@Override
	public Map<String, JdbcPoolStatusVo> getJdbcPoolStatus(String nodeId) {
		return JdbcPoolStatusCollector.getInstance().currentAllDataSourceStatus();
	}

}
