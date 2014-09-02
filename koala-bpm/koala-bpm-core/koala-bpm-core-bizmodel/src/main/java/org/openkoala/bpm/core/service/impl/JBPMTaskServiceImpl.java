package org.openkoala.bpm.core.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.drools.persistence.info.WorkItemInfo;
import org.jbpm.persistence.processinstance.ProcessInstanceInfo;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.task.Content;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.jbpm.task.query.TaskSummary;
import org.openkoala.bpm.core.KoalaAssignInfo;
import org.openkoala.bpm.core.KoalaJbpmVariable;
import org.openkoala.bpm.core.KoalaProcessInfo;
import org.openkoala.bpm.core.ProcessInstanceExpandLog;
import org.openkoala.bpm.core.service.JBPMTaskService;

@Named("jbpmTaskService")
@SuppressWarnings("unchecked")
public class JBPMTaskServiceImpl implements JBPMTaskService {

	
	@PersistenceContext(unitName="org.jbpm.persistence.jpa")
	EntityManager jbpmEM;

	public void removeTaskUser(long taskId, String user) {
		jbpmEM.createNativeQuery(
				"delete from peopleassignments_potowners where TASK_ID='"
						+ taskId + "' and ENTITY_ID='" + user + "' ")
				.executeUpdate();
	}

	public void failedTask(Task task) {
		task.getTaskData().setStatus(Status.Failed);
		jbpmEM.merge(task);
	}

	public void repairTask(Task task) {
		task.getTaskData().setStatus(Status.Reserved);
		jbpmEM.merge(task);
	}

	public ProcessInstanceExpandLog getInstanceExpandLog(long instanceLogId) {
		final Query instanceExpandLogQuery = jbpmEM
				.createQuery("select log from ProcessInstanceExpandLog log where log.instanceLogId = :instanceLogId");
		instanceExpandLogQuery.setParameter("instanceLogId", instanceLogId);
		List<ProcessInstanceExpandLog> results = null;
		results = instanceExpandLogQuery.getResultList();
		if (results == null || results.size() == 0) {
			return null;
		} else {
			return (ProcessInstanceExpandLog) results.get(0);
		}

	}

	public List<TaskSummary> getErrorTasks() {
		final Query tasksAssignedAsPotentialOwner = jbpmEM
				.createNamedQuery("GetErrorTasks");
		tasksAssignedAsPotentialOwner.setParameter("language", "en-UK");
		return (List<TaskSummary>) tasksAssignedAsPotentialOwner
				.getResultList();
	}

	public List<WorkItemInfo> getWorkItemInfo(long processInstanceId) {
		final Query workItemQuery = jbpmEM
				.createQuery("select w from WorkItemInfo w where w.processInstanceId = :processInstanceId");
		workItemQuery.setParameter("processInstanceId", processInstanceId);
		return (List<WorkItemInfo>) workItemQuery.getResultList();
	}

	public List<KoalaJbpmVariable> queryJbpmVariable() {
		List<KoalaJbpmVariable> varsiableList = jbpmEM.createQuery(
				"select k from KoalaJbpmVariable k").getResultList();
		return varsiableList;
	}

	public List<KoalaProcessInfo> findActiveProcess() {
		String sql = "from KoalaProcessInfo k where k.active is true";
		return jbpmEM.createQuery(sql).getResultList();
	}

	public List<KoalaAssignInfo> queryKoalaAssignInfo(String user, Date nowDate) {
		final Query koalaProcessInfoQuery = jbpmEM
				.createQuery("select k from KoalaAssignInfo k where k.assignerTo = ? and k.beginTime<=? and ? <=k.endTime");
		koalaProcessInfoQuery.setParameter(1, user);
		koalaProcessInfoQuery.setParameter(2, nowDate);
		koalaProcessInfoQuery.setParameter(3, nowDate);
		return (List<KoalaAssignInfo>) koalaProcessInfoQuery.getResultList();
	}

	public KoalaProcessInfo getKoalaProcessInfo(Map<String, Object> params) {
		return KoalaProcessInfo.findKoalaProcessInfo(params);
	}

	public void addProcessInfo(KoalaProcessInfo info) {
		jbpmEM.persist(info);

	}

	public List<KoalaProcessInfo> getDBResource() {
		final Query processQuery = jbpmEM
				.createQuery("select w from KoalaProcessInfo w");
		return processQuery.getResultList();
	}

	public void removeAssignUser(User user) {
		jbpmEM.remove(jbpmEM.merge(user));
	}
	
	public void saveWorkItem(WorkItemInfo info) {
		jbpmEM.persist(info);
	}

	public ProcessInstanceInfo getProcessInstanceInfo(long processInstanceId) {
		return jbpmEM.find(ProcessInstanceInfo.class, processInstanceId);
	}

	public void updateProcessInstanceInfo(ProcessInstanceInfo info) {
		jbpmEM.merge(info);
	}

	public Content getContent(long contentId) {
		return jbpmEM.find(Content.class, contentId);
	}

	public List<ProcessInstanceLog> findProcessInstances() {

		List<ProcessInstanceLog> result = jbpmEM.createQuery(
				"FROM ProcessInstanceLog").getResultList();

		return result;
	}

	public List<ProcessInstanceLog> findProcessInstances(String processId) {

		List<ProcessInstanceLog> result = jbpmEM
				.createQuery(
						"FROM ProcessInstanceLog p WHERE p.processId like :processId")
				.setParameter("processId", processId + "@%").getResultList();

		return result;
	}

	public List<ProcessInstanceLog> findActiveProcessInstances(String processId) {

		List<ProcessInstanceLog> result = jbpmEM
				.createQuery(
						"FROM ProcessInstanceLog p WHERE p.processId = :processId AND p.end is null")
				.setParameter("processId", processId).getResultList();

		return result;
	}

	public ProcessInstanceLog findProcessInstance(long processInstanceId) {

		try {
			return (ProcessInstanceLog) jbpmEM
					.createQuery(
							"FROM ProcessInstanceLog p WHERE p.processInstanceId = :processInstanceId")
					.setParameter("processInstanceId", processInstanceId)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
		}
	}

	public List<NodeInstanceLog> findNodeInstances(long processInstanceId) {

		List<NodeInstanceLog> result = jbpmEM
				.createQuery(
						"FROM NodeInstanceLog n WHERE n.processInstanceId = :processInstanceId ORDER BY date,id")
				.setParameter("processInstanceId", processInstanceId)
				.getResultList();

		return result;
	}

	public List<NodeInstanceLog> findNodeInstances(long processInstanceId,
			String nodeId) {

		List<NodeInstanceLog> result = jbpmEM
				.createQuery(
						"FROM NodeInstanceLog n WHERE n.processInstanceId = :processInstanceId AND n.nodeId = :nodeId ORDER BY date,id")
				.setParameter("processInstanceId", processInstanceId)
				.setParameter("nodeId", nodeId).getResultList();

		return result;
	}

	public List<VariableInstanceLog> findVariableInstances(
			long processInstanceId) {

		List<VariableInstanceLog> result = jbpmEM
				.createQuery(
						"FROM VariableInstanceLog v WHERE v.processInstanceId = :processInstanceId ORDER BY date")
				.setParameter("processInstanceId", processInstanceId)
				.getResultList();

		return result;
	}

	public List<VariableInstanceLog> findVariableInstances(
			long processInstanceId, String variableId) {

		List<VariableInstanceLog> result = jbpmEM
				.createQuery(
						"FROM VariableInstanceLog v WHERE v.processInstanceId = :processInstanceId AND v.variableId = :variableId ORDER BY date")
				.setParameter("processInstanceId", processInstanceId)
				.setParameter("variableId", variableId).getResultList();
		return result;
	}

	public List<TaskSummary> findProcessTaskSummary(List<String> processes,
			String user) {
		List<TaskSummary> result = jbpmEM
				.createNamedQuery(
						"ProcessTasksAssignedAsPotentialOwner")
				.setParameter("userId", user)
				.setParameter("language", "en-UK")
				.setParameter("processes", processes)
				.getResultList();

		return result;
	}

	public List<TaskSummary> findProcessTaskSummaryByGroups(List<String> processes,
			String user, List<String> groups) {
		List<TaskSummary> result = jbpmEM
				.createNamedQuery(
						"ProcessTasksAssignedAsPotentialOwnerWithGroups")
				.setParameter("userId", user)
				.setParameter("language", "en-UK")
				.setParameter("processes", processes)
				.setParameter("groupIds", groups).getResultList();
		
		return result;
	}
}
