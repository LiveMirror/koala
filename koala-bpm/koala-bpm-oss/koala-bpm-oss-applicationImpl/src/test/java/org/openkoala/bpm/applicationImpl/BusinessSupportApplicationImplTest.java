package org.openkoala.bpm.applicationImpl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.NamedQuery;
import org.dayatang.querychannel.Page;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openkoala.bpm.application.JBPMApplication;
import org.openkoala.bpm.application.dto.DynaProcessHistoryValueDTO;
import org.openkoala.bpm.application.dto.FormShowDTO;
import org.openkoala.bpm.application.dto.TaskDTO;
import org.openkoala.bpm.application.dto.TaskVerifyDTO;
import org.openkoala.bpm.application.exception.FormHavenDefinedException;
import org.openkoala.bpm.application.vo.HistoryLogVo;
import org.openkoala.bpm.application.vo.PageTaskVO;
import org.openkoala.bpm.application.vo.ProcessVO;
import org.openkoala.bpm.application.vo.TaskChoice;
import org.openkoala.bpm.application.vo.TaskVO;
import org.openkoala.bpm.processdyna.core.DynaProcessForm;
import org.openkoala.bpm.processdyna.core.DynaProcessHistoryValue;
import org.openkoala.bpm.processdyna.core.DynaProcessKey;
import org.openkoala.bpm.processdyna.core.DynaProcessTemplate;
import org.openkoala.bpm.processdyna.core.DynaProcessValue;
import org.openkoala.bpm.processdyna.infra.TemplateContent;

public class BusinessSupportApplicationImplTest{
	
	private BusinessSupportApplicationImpl instance = new BusinessSupportApplicationImpl();
	
	@Mock
	private JBPMApplication jbpmApplication;
	
	@Mock
	private EntityRepository repository;
	
	@Mock
	TemplateContent templateContent;
	
	@Mock
	DynaProcessTemplate template;

//	private String oneColumnTemplateName = "一列模版";
//	private String twoColumnsTemplateName = "两列模版";
	private String processIdCannotFindForm = "processId.notExist";
	private String processIdCanFindForm = "processId.exist";
	private String creater = "test";
	private long processInstanceId = 1;
	private long taskId = 2;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		instance.setJbpmApplication(jbpmApplication);
		DynaProcessForm.setRepository(repository);
		DynaProcessForm.setTemplateContent(templateContent);
		DynaProcessKey.setRepository(repository);
		DynaProcessTemplate.setRepository(repository);
		DynaProcessHistoryValue.setRepository(repository);
		DynaProcessValue.setRepository(repository);
		
	}

	@After
	public void tearDown() throws Exception {
		DynaProcessForm.setTemplateContent(null);
		DynaProcessForm.setRepository(null);
		DynaProcessKey.setRepository(null);
		DynaProcessTemplate.setRepository(null);
		DynaProcessHistoryValue.setRepository(null);
		DynaProcessValue.setRepository(null);
	}

	@Test
	@Ignore
	public void testInitDynaProcessTemplateDatas() {
		//haven been used
	}

	@Test
	public void testGetProcesses() {
		List<ProcessVO> list = new ArrayList<ProcessVO>();
		when(jbpmApplication.getProcesses()).thenReturn(list);
		List<ProcessVO> resultList = instance.getProcesses();
		Assert.assertEquals(list.size(), resultList.size());
	}

	@Test(expected = FormHavenDefinedException.class)
	public void testGetTemplateHtmlCodeByProcessId() {
		List<Object> results = new ArrayList<Object>();
		NamedQuery named = new NamedQuery(repository, "queryActiveDynaProcessFormByProcessId");
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId")).thenReturn(named);
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId").setParameters(processIdCannotFindForm).list()).thenReturn(results);
		instance.findTemplateHtmlCodeByProcessId(processIdCannotFindForm);
	}
	
	@Test
	public void testGetTemplateHtmlCodeByProcessId2() {
		List results = new ArrayList<DynaProcessForm>();
		DynaProcessForm dynaProcessForm = new DynaProcessForm();
		dynaProcessForm.setTemplate(template);
		results.add(dynaProcessForm);
		NamedQuery named = new NamedQuery(repository, "queryActiveDynaProcessFormByProcessId");
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId")).thenReturn(named);
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId").setParameters(processIdCanFindForm).list()).thenReturn(results);
		
		when(templateContent.process(new HashMap<String,Object>(), null)).thenReturn(null);
		FormShowDTO formShowDTO = instance.findTemplateHtmlCodeByProcessId(processIdCanFindForm);
		
		Assert.assertNotNull(formShowDTO);
	}

	@Test
	public void testStartProcess() {
		when(jbpmApplication.startProcess(creater, processIdCanFindForm, "")).thenReturn(processInstanceId);
		instance.startProcess(creater, processIdCanFindForm, new HashSet<DynaProcessKey>());
		Assert.assertTrue(true);
	}

	@Test
	public void testGetTodoList() {
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();
		when(jbpmApplication.processQueryTodoListWithGroup(processIdCanFindForm, creater, null)).thenReturn(taskVOs);
		List<TaskDTO> resultList = instance.getTodoList(processIdCanFindForm, creater);
		Assert.assertEquals(taskVOs.size(), resultList.size());
	}

	@Test
	public void testGetDynaProcessHistoryValuesByProcessInstanceId() {
		List list = new ArrayList<DynaProcessHistoryValue>();
		NamedQuery named = new NamedQuery(repository, "queryDynaProcessHistoryValuesByProcessInstanceId");
		when(repository.createNamedQuery("queryDynaProcessHistoryValuesByProcessInstanceId")).thenReturn(named);
		when(repository.createNamedQuery("queryDynaProcessHistoryValuesByProcessInstanceId").setParameters(processInstanceId).list()).thenReturn(list);
		List<DynaProcessHistoryValueDTO> resultList = instance
				.getDynaProcessHistoryValuesByProcessInstanceId(processInstanceId);
		Assert.assertEquals(list.size(), resultList.size());
	}

	@Test
	public void testGetDoneTasks() {
		PageTaskVO pageTaskVO = new PageTaskVO();
		when(jbpmApplication.pageQueryDoneTask(processIdCanFindForm, creater, 1, 10)).thenReturn(pageTaskVO);
		Page<TaskDTO> page = instance.getDoneTasks(processIdCanFindForm, creater, 1, 10);
		Assert.assertEquals(pageTaskVO.getTotalCount(), page.getResultCount());
	}

	@Test
	public void testGetDynaProcessKeysByProcessId() {
		List list = new ArrayList<DynaProcessForm>();
		DynaProcessForm dynaProcessForm = new DynaProcessForm();
		list.add(dynaProcessForm);
		NamedQuery named = new NamedQuery(repository, "queryActiveDynaProcessFormByProcessId");
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId")).thenReturn(named);
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId").setParameters(processIdCanFindForm).list()).thenReturn(list);
		Set<DynaProcessKey> keys = instance.getDynaProcessKeysByProcessId(processIdCanFindForm);
		Assert.assertEquals(dynaProcessForm.getKeys().size(), keys.size());
	}

	@Test
	@Ignore
	public void testGetDynaProcessTaskContentForVerify() {
		List<HistoryLogVo> historys = new ArrayList<HistoryLogVo>();
		historys.add(new HistoryLogVo());
		when(jbpmApplication.queryHistoryLog(processInstanceId)).thenReturn(historys);
		
		List<TaskChoice> taskChoices = new ArrayList<TaskChoice>();
		taskChoices.add(new TaskChoice());
		when(jbpmApplication.queryTaskChoice(processInstanceId,taskId)).thenReturn(taskChoices);
		
		List list = new ArrayList<DynaProcessForm>();
		DynaProcessForm dynaProcessForm = new DynaProcessForm();
		dynaProcessForm.setTemplate(template);
		list.add(dynaProcessForm);
		NamedQuery named = new NamedQuery(repository, "queryActiveDynaProcessFormByProcessId");
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId")).thenReturn(named);
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId").setParameters(processIdCanFindForm).list()).thenReturn(list);
		
		FormShowDTO formShowDTO = instance.getDynaProcessTaskContentForVerify(processIdCanFindForm, processInstanceId, taskId);
		Assert.assertEquals(historys.size(), formShowDTO.getHistoryLogs().size());
		Assert.assertEquals(taskChoices.size(), formShowDTO.getTaskChoices().size());
	}
	
	@Test
	public void testGetDynaProcessTaskContentForHistory() {
		List<HistoryLogVo> historys = new ArrayList<HistoryLogVo>();
		historys.add(new HistoryLogVo());
		when(jbpmApplication.queryHistoryLog(processInstanceId)).thenReturn(historys);
		
		List list = new ArrayList<DynaProcessForm>();
		DynaProcessForm dynaProcessForm = new DynaProcessForm();
		dynaProcessForm.setTemplate(template);
		list.add(dynaProcessForm);
		NamedQuery named = new NamedQuery(repository, "queryActiveDynaProcessFormByProcessId");
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId")).thenReturn(named);
		when(repository.createNamedQuery("queryActiveDynaProcessFormByProcessId").setParameters(processIdCanFindForm).list()).thenReturn(list);
		
		FormShowDTO formShowDTO = instance.getDynaProcessTaskContentForHistory(processIdCanFindForm, processInstanceId, taskId);
		Assert.assertEquals(historys.size(), formShowDTO.getHistoryLogs().size());
	}

	@Test
	public void testGetPorcessInstanceImageStream() {
		byte[] image = new byte[20];
		when(jbpmApplication.getPorcessImageStream(processInstanceId)).thenReturn(image);
		
		byte[] result = instance.getPorcessInstanceImageStream(processInstanceId);
		Assert.assertEquals(image.length, result.length);
	}

	@Test
	public void testVerifyTask() {
		TaskVerifyDTO taskVerifyDTO = this.createAndInitTaskVerifyDTO();
		when(jbpmApplication.completeTask(taskVerifyDTO.getProcessInstanceId(), taskVerifyDTO.getTaskId(),
				taskVerifyDTO.getUser(), instance.convertTaskChoiceToXml(taskVerifyDTO.getComment(),
						taskVerifyDTO.getTaskChoice()), null)).thenReturn(true);
		boolean result = instance.verifyTask(taskVerifyDTO);
		Assert.assertTrue(result);
	}
	
	private TaskVerifyDTO createAndInitTaskVerifyDTO(){
		TaskVerifyDTO taskVerifyDTO = new TaskVerifyDTO();
		taskVerifyDTO.setProcessInstanceId(processInstanceId);
		taskVerifyDTO.setTaskId(taskId);
		taskVerifyDTO.setUser(creater);
		taskVerifyDTO.setComment("情况属实");
		taskVerifyDTO.setTaskChoice(this.createAndInitTaskChoice());
		return taskVerifyDTO; 
	}
	
	private TaskChoice createAndInitTaskChoice(){
		TaskChoice taskChoice = new TaskChoice();
		taskChoice.setKey("keyId");
		taskChoice.setName("同意");
		taskChoice.setValue("1");
		taskChoice.setValueType("String");
		return taskChoice;
	}
	
}
