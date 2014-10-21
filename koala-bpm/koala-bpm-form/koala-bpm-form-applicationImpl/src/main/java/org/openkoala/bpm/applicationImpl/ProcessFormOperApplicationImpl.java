package org.openkoala.bpm.applicationImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.bpm.application.JBPMApplication;
import org.openkoala.bpm.application.ProcessFormOperApplication;
import org.openkoala.bpm.application.dto.DynaProcessFormDTO;
import org.openkoala.bpm.application.dto.DynaProcessKeyDTO;
import org.openkoala.bpm.application.dto.DynaProcessTemplateDTO;
import org.openkoala.bpm.application.dto.ProcessDTO;
import org.openkoala.bpm.application.dto.SelectOptions;
import org.openkoala.bpm.application.utils.EntityTurnToDTOUtil;
import org.openkoala.bpm.application.vo.ProcessVO;
import org.openkoala.bpm.processdyna.core.DynaProcessForm;
import org.openkoala.bpm.processdyna.core.DynaProcessKey;
import org.openkoala.bpm.processdyna.core.DynaProcessTemplate;
import org.openkoala.bpm.processdyna.core.DynaType;
import org.openkoala.bpm.processdyna.core.FieldType;
import org.openkoala.bpm.processdyna.core.ValidateRule;
import org.openkoala.exception.base.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional(value="transactionManager")
public class ProcessFormOperApplicationImpl implements
		ProcessFormOperApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProcessFormOperApplicationImpl.class);
	
	
	
	private QueryChannelService queryChannel;
	
	private QueryChannelService getQueryChannelService(){
		if(queryChannel==null){
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class,"queryChannel");
		}
		return queryChannel;
	}
	
    private JBPMApplication jbpmApplication;
	
	private JBPMApplication getJBPMApplication() { 
		if(jbpmApplication==null){
			jbpmApplication = InstanceFactory.getInstance(JBPMApplication.class);
		} 
		return jbpmApplication; 
	}

	public void createDynaProcessForm(DynaProcessFormDTO form) {
		DynaProcessForm processForm =new DynaProcessForm(form.getProcessId(),form.getBizName());
		
		boolean editAct = form.getId() > 0;
		if(editAct){
			//修改表单：将原表单置为不可用状态，新增一条记录
			DynaProcessForm origForm = DynaProcessForm.queryActiveDynaProcessFormById(form.getId());
			if(origForm == null){
				throw new BaseException("form_is_notexists", "指定表单不存在");
			}
			origForm.setActive(false);
			origForm.save();
			//修改表单时不改变绑定流程
			form.setProcessId(origForm.getProcessId());
		}else{
			if(DynaProcessForm.queryActiveDynaProcessFormByProcessId(form.getProcessId()) != null){
				throw new BaseException("form_is_exists", "该流程已经绑定表单");
			}
		}
		
		List<DynaProcessKeyDTO> processKeys = form.getProcessKeys();
		for (DynaProcessKeyDTO key : processKeys) {
			DynaProcessKey processKey = new DynaProcessKey(key.getKeyId(), key.getKeyName(), key.getKeyType());
			processKey.setRequired("true".equals(key.getRequired()));
			processKey.setInnerVariable("true".equals(key.getInnerVariable()));
			processKey.setValidationExpr(key.getValidationExpr());
			processKey.setValidationType(key.getValidationType());
			processKey.setShowOrder(key.getShowOrder());
			processKey.setOutputVar("true".equals(key.getOutputVar()));
			processKey.setKeyOptions(key.getKeyOptions());
			processKey.setValOutputType(key.getValOutputType());
			processKey.setDynaTable(processForm);
			processForm.addDynaProcessKey(processKey);
		}
		
		if(form.getTemplateId()>0){
			DynaProcessTemplate template = DynaProcessTemplate.getRepository().get(DynaProcessTemplate.class, form.getTemplateId());
            processForm.setTemplate(template);
		}
		processForm.setProcessId(form.getProcessId());
		processForm.setBizDescription(form.getBizDescription());
		processForm.setActive(true);
		processForm.save();
		
	}

	public Page<DynaProcessFormDTO> queryDynaProcessFormsByPage(DynaProcessFormDTO search,int currentPage, int pageSize) {
		
        List<DynaProcessFormDTO> datas = new ArrayList<DynaProcessFormDTO>();
		String jpql = "select df from DynaProcessForm df join df.template as dt where df.active is true";
		Page<DynaProcessForm> page = getQueryChannelService().createJpqlQuery(jpql).setPage(currentPage, pageSize).pagedList();
		
		for (DynaProcessForm processForm : page.getData()) {
			processForm.setKeys(null);
//			datas.add(DynaProcessForm2DTO(processForm));
			datas.add(EntityTurnToDTOUtil.DynaProcessForm2DTO(processForm));
			
		}
		return new Page<DynaProcessFormDTO>(page.getStart(), page.getResultCount(), pageSize, datas);
	}

	public List<SelectOptions> getDataTypeList() {
		List<SelectOptions> list = new ArrayList<SelectOptions>();
		for (DynaType e : DynaType.values()) {
			list.add(new SelectOptions(e.getText(), e.name()));
		}
		return list;
	}

	public List<DynaProcessTemplateDTO> getActiveProcessTemplates() {
		List<DynaProcessTemplateDTO> datas = new ArrayList<DynaProcessTemplateDTO>();
		List<DynaProcessTemplate> list = DynaProcessTemplate.getRepository().findAll(DynaProcessTemplate.class);
		for (DynaProcessTemplate template : list) {
			DynaProcessTemplateDTO dto = new DynaProcessTemplateDTO(template.getTemplateName(), template.getTemplateDescription(), template.getTemplateData());
			dto.setId(template.getId());
			datas.add(dto);
		}
		return datas;
	}

	public List<ProcessDTO> getActiveProcesses(String[] ignoreExcludeIds) {
		List<ProcessDTO> datas = new ArrayList<ProcessDTO>();
		List<String> ignoreExcludeIdList = ignoreExcludeIds == null ? new ArrayList<String>() : Arrays.asList(ignoreExcludeIds);
		try {
			List<ProcessVO> processes = getJBPMApplication().getProcesses();
			if(processes == null)return datas;
			String jpql = "select df.processId from DynaProcessForm df where df.active is true";
			//已绑定form的流程列表
			List<String> existsProcessIds = getQueryChannelService().createJpqlQuery(jpql).list();
			for (ProcessVO process : processes) {
				//排除已绑定的流程和忽略排除列表
				if(ignoreExcludeIdList.contains(process.getId()) 
						|| existsProcessIds == null || !existsProcessIds.contains(process.getId())){
					datas.add(new ProcessDTO(process.getId(), process.getName()));
				}
			}
		} catch (Exception e) {
			LOG.error("load process list", e);
		}
		
		return datas;
	}

	public List<SelectOptions> getValidateRules() {
		List<SelectOptions> list = new ArrayList<SelectOptions>();
		for (ValidateRule e : ValidateRule.values()) {
			list.add(new SelectOptions(e.getText(), e.name()));
		}
		return list;
	}
	
	public List<SelectOptions> getFieldValTypeList() {
		List<SelectOptions> list = new ArrayList<SelectOptions>();
		for (FieldType e : FieldType.values()) {
			list.add(new SelectOptions(e.getText(), e.name()));
		}
		return list;
	}

	public DynaProcessFormDTO getDynaProcessFormById(Long id) {
		String jpql = "from DynaProcessForm fetch all properties where id = ?";
		List<DynaProcessForm> list = getQueryChannelService().createJpqlQuery(jpql).setParameters(id).list();
		if(list != null && list.size()>0){
			DynaProcessForm processForm = list.get(0);
//			return DynaProcessForm2DTO(processForm);
			return EntityTurnToDTOUtil.DynaProcessForm2DTO(processForm);
		}
		
		return null;
	}


	public void deleteDynaProcessFormById(Long[] ids) {
		StringBuffer sb = new StringBuffer("(");
		for (Long id : ids) {
			sb.append(id).append(",");
		}
		sb.deleteCharAt(sb.length() - 1).append(")");
		String jpql = "from DynaProcessForm fetch all properties where id in" + sb.toString();
		List<DynaProcessForm> list = getQueryChannelService().createJpqlQuery(jpql).list();
		for (DynaProcessForm dynaProcessForm : list) {
			dynaProcessForm.remove();
		}
		
	}

	
	
}
