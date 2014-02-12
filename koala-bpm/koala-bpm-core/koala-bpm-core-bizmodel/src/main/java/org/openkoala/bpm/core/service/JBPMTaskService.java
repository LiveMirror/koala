package org.openkoala.bpm.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.drools.persistence.info.WorkItemInfo;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.task.Content;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.jbpm.task.query.TaskSummary;
import org.openkoala.bpm.core.KoalaAssignInfo;
import org.openkoala.bpm.core.KoalaJbpmVariable;
import org.openkoala.bpm.core.KoalaProcessInfo;
import org.openkoala.bpm.core.ProcessInstanceExpandLog;

/**
 * 
 * * @author lingen
 * 
 */
public interface JBPMTaskService {

	public void removeTaskUser(long taskId, String user);

	public void repairTask(Task task);

	public void failedTask(Task task);

	public ProcessInstanceExpandLog getInstanceExpandLog(long instanceLogId);

	public List<TaskSummary> getErrorTasks();

	public List<WorkItemInfo> getWorkItemInfo(long processInstanceId);

	public void saveWorkItem(WorkItemInfo info);

	public void addProcessInfo(KoalaProcessInfo info);

	public List<KoalaProcessInfo> findActiveProcess();

	public Content getContent(long contentId);

	public void removeAssignUser(User user);

	public List<KoalaProcessInfo> getDBResource();

	public List<KoalaJbpmVariable> queryJbpmVariable();

	public List<KoalaAssignInfo> queryKoalaAssignInfo(String user, Date nowDate);

	public KoalaProcessInfo getKoalaProcessInfo(Map<String, Object> params);

	public List<VariableInstanceLog> findVariableInstances(
			long processInstanceId, String variableId);

	public List<VariableInstanceLog> findVariableInstances(
			long processInstanceId);

	public List<NodeInstanceLog> findNodeInstances(long processInstanceId,
			String nodeId);

	public List<NodeInstanceLog> findNodeInstances(long processInstanceId);

	public ProcessInstanceLog findProcessInstance(long processInstanceId);

	public List<ProcessInstanceLog> findActiveProcessInstances(String processId);

	public List<ProcessInstanceLog> findProcessInstances(String processId);

	public List<ProcessInstanceLog> findProcessInstances();
	
	public List<TaskSummary> findProcessTaskSummary(List<String> processes,
			String user);
	
	public List<TaskSummary> findProcessTaskSummaryByGroups(List<String> processes,
			String user, List<String> groups);
}
