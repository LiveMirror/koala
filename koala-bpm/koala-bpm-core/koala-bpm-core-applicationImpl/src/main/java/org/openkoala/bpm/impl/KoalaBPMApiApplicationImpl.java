package org.openkoala.bpm.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Named;

import org.dayatang.domain.InstanceFactory;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.task.Content;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.openkoala.bpm.application.KoalaBPMApiApplication;
import org.openkoala.bpm.impl.util.KoalaBPMSession;

@Named(value = "KoalaBPMApiApplication")
public class KoalaBPMApiApplicationImpl implements KoalaBPMApiApplication {

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock rLock = lock.readLock();
	private final Lock wLock = lock.writeLock();

	private KoalaBPMSession koalaBPMSession;

	public KoalaBPMSession getKoalaBPMSession() {
		if (koalaBPMSession == null) {
			koalaBPMSession = InstanceFactory
					.getInstance(KoalaBPMSession.class);
		}
		return koalaBPMSession;
	}

	@Override
	public ProcessInstance startProcess(String processName,
			Map<String, Object> params) {

		ProcessInstance instance = null;
			instance = getKoalaBPMSession().getKsession().startProcess(
					processName, params);
			getKoalaBPMSession().getKsession().insert(instance);
			getKoalaBPMSession().getKsession().fireAllRules();

		return instance;
	}

	public List<TaskSummary> findTaskSummary(String user) {
		return getKoalaBPMSession().getLocalTaskService()
				.getTasksAssignedAsPotentialOwner(user, "en-UK");
	}

	public List<TaskSummary> findTaskSummaryByGroup(String user,
			List<String> groups) {
		return getKoalaBPMSession().getLocalTaskService()
				.getTasksAssignedAsPotentialOwner(user, groups, "en-UK");
	}

	public void clearTaskByProcessInstanceId(long processInstanceId) {
		List<Status> status = new ArrayList<Status>();
		status.add(Status.Created);
		status.add(Status.InProgress);
		status.add(Status.Reserved);
		status.add(Status.Ready);
		List<TaskSummary> summarys = getKoalaBPMSession()
				.getLocalTaskService()
				.getTasksByStatusByProcessId(processInstanceId, status, "en-UK");
		for (TaskSummary summary : summarys) {
			Task task = getKoalaBPMSession().getLocalTaskService().getTask(summary.getId());
			getKoalaBPMSession().getLocalTaskService().exit(task.getId(),task.getPeopleAssignments().getBusinessAdministrators().get(0).getId());
		}
	}

	public Collection<org.drools.definition.process.Process> queryProcesses() {
		Collection<org.drools.definition.process.Process> process = null;
		rLock.lock();
		try {
			process = getKoalaBPMSession().getKsession().getKnowledgeBase()
					.getProcesses();
		} finally {
			rLock.unlock();
		}
		return process;
	}

	public org.drools.definition.process.Process getProcess(String processId) {
		return getKoalaBPMSession().getKsession().getKnowledgeBase()
				.getProcess(processId);
	}

	public ProcessInstanceLog getProcessInstanceLog(long processId) {
		return JPAProcessInstanceDbLog.findProcessInstance(processId);
	}

	public List<ProcessInstanceLog> findActiveProcessInstances(
			String processIdActual) {
		return JPAProcessInstanceDbLog
				.findActiveProcessInstances(processIdActual);
	}

	public List<ProcessInstanceLog> findProcessInstances(String processIdActual) {
		return JPAProcessInstanceDbLog.findProcessInstances();
	}

	public List<VariableInstanceLog> findVariableInstances(long processId) {
		return JPAProcessInstanceDbLog.findVariableInstances(processId);
	}

	public List<VariableInstanceLog> findVariableInstances(long processId,
			String variableId) {
		return JPAProcessInstanceDbLog.findVariableInstances(processId,
				variableId);
	}

	public ProcessInstance getProcessInstance(long processInstanceId) {
		RuleFlowProcessInstance in = null;
		rLock.lock();
		try {
			in = (RuleFlowProcessInstance) getKoalaBPMSession().getKsession()
					.getProcessInstance(processInstanceId);
		} finally {
			rLock.unlock();
		}
		return in;
	}

	public void abortProcessInstance(long processInstanceId) {
		wLock.lock();
		try {
			getKoalaBPMSession().getKsession().abortProcessInstance(
					processInstanceId);
		} finally {
			wLock.unlock();
		}

	}

	public void delegate(long taskId, String userId, String targetUserId) {
		getKoalaBPMSession().getLocalTaskService().delegate(taskId, userId,
				targetUserId);
	}

	public Task getTask(long taskId) {
		return getKoalaBPMSession().getLocalTaskService().getTask(taskId);
	}

	public Content getContent(long contentId) {
		return getKoalaBPMSession().getLocalTaskService().getContent(contentId);
	}

	public void startTask(long taskId, String userId) {
		getKoalaBPMSession().getLocalTaskService().start(taskId, userId);
	}

	public void completeTask(long taskId, String userId, ContentData outputData) {
		getKoalaBPMSession().getLocalTaskService().complete(taskId, userId,
				outputData);
	}

}
