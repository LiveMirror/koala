package org.openkoala.bpm.processdyna.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.dayatang.domain.AbstractEntity;
import org.dayatang.domain.InstanceFactory;
import org.openkoala.bpm.processdyna.infra.TemplateContent;

/**
 * 流程自定义表单
 * 
 * @author lingen
 * 
 */
@Entity
@Table(name = "DYNA_PROCESS_FORM")
public class DynaProcessForm extends BpmFormEntity {
	
	private static final long serialVersionUID = -2210960935894672709L;

	/**
	 * 关联的流程ID
	 */
	
	private String processId;

	/**
	 * 业务表单名称
	 */
	
	private String bizName;

	/**
	 * 业务表单描述
	 */
	
	private String bizDescription;

	/**
	 * 是否激活
	 */
	
	private boolean active;

	private Set<DynaProcessKey> keys = new HashSet<DynaProcessKey>();
	
	
	private DynaProcessTemplate template;

	public DynaProcessForm() {
		super();
	}

	public DynaProcessForm(String processId, String bizName) {
		super();
		this.processId = processId;
		this.bizName = bizName;
	}

	public DynaProcessForm(String processId, String bizName,
			String bizDescription) {
		super();
		this.processId = processId;
		this.bizName = bizName;
		this.bizDescription = bizDescription;
	}

	@Column(name = "PROCESS_ID")
	@NotNull
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Column(name = "BIZ_NAME")
	@NotNull
	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	@Column(name = "BIZ_DESCRIPTION")
	public String getBizDescription() {
		return bizDescription;
	}

	public void setBizDescription(String bizDescription) {
		this.bizDescription = bizDescription;
	}

	@Column(name = "ACTIVE",columnDefinition="bit")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "ProcessDynaTable [processId=" + processId + ", bizName="
				+ bizName + ", active=" + active + "]";
	}

	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,
			CascadeType.MERGE, CascadeType.REMOVE }, mappedBy = "dynaTable", fetch = FetchType.EAGER)
	public Set<DynaProcessKey> getKeys() {
		return keys;
	}

	public void setKeys(Set<DynaProcessKey> keys) {
		this.keys = keys;
	}
	
	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH }, optional = true)  
	@JoinColumn(name="TEMPLATE_ID")
	public DynaProcessTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DynaProcessTemplate template) {
		this.template = template;
	}

	public void addDynaProcessKey(DynaProcessKey key) {
		if (keys == null) {
			keys = new HashSet<DynaProcessKey>();
		}
		keys.add(key);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((bizDescription == null) ? 0 : bizDescription.hashCode());
		result = prime * result + ((bizName == null) ? 0 : bizName.hashCode());
		result = prime * result
				+ ((processId == null) ? 0 : processId.hashCode());
		return result;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DynaProcessForm other = (DynaProcessForm) obj;
		if (active != other.active)
			return false;
		if (bizDescription == null) {
			if (other.bizDescription != null)
				return false;
		} else if (!bizDescription.equals(other.bizDescription))
			return false;
		if (bizName == null) {
			if (other.bizName != null)
				return false;
		} else if (!bizName.equals(other.bizName))
			return false;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		return true;
	}

	/* =============================领域方法======================================== */
	/**
	 * 创建一个新的流程自定义表单，包括表单定义及 KEY 值定义
	 */
	public void save() {
		String batchUpdateProcess = "update DynaProcessForm dynaProcessForm set dynaProcessForm.active = false where dynaProcessForm.processId = :processId";
		
		getRepository().createJpqlQuery(batchUpdateProcess).addParameter("processId", processId).executeUpdate();
		this.setActive(true);
		super.save();
	}
	
	/**
	 * 根据ID查询
	 * 
	 * @return
	 */
	public static DynaProcessForm queryActiveDynaProcessFormById(
			Long id) {
		List<DynaProcessForm> results = getRepository().createNamedQuery("queryActiveDynaProcessFormById").setParameters(id).list();
		if (results.size() == 0) {
			return null;
		} else {
			return results.get(0);
		}

	}


	/**
	 * 返回一个流程中当前有效的表单定义，如果没有则返回 NULL
	 * 
	 * @return
	 */
	public static DynaProcessForm queryActiveDynaProcessFormByProcessId(
			String processId) {
		List<DynaProcessForm> results = getRepository().createNamedQuery("queryActiveDynaProcessFormByProcessId").setParameters(processId).list();
		if (results.size() == 0) {
			return null;
		} else {
			return results.get(0);
		}

	}

	/**
	 * 返回一个流程有所有的定义，包括已经失效的定义
	 * 
	 * @param processId
	 * @return
	 */
	public static List<DynaProcessForm> queryDynaProcessFormByProcessId(
			String processId) {
		List<DynaProcessForm> result = DynaProcessForm.getRepository().createNamedQuery("queryDynaProcessFormByProcessId").setParameters(processId).list();
		return result;
	}

	private static TemplateContent templateContent;
	
	public static void setTemplateContent(TemplateContent templateContent) {
		DynaProcessForm.templateContent = templateContent;
	}

	@Transient
	public TemplateContent getTemplateContent(){
		if(templateContent==null){
			templateContent = InstanceFactory.getInstance(TemplateContent.class);
		}
		return templateContent;
	}
	
	/**
	 * 提供模版以及模版数据，用以生成html代码
	 * @return
	 */
	public String packagingHtml(){
		return getTemplateContent().process(this.findTemplateParams(),template.getTemplateData());
	}
	
	@Transient
	private Map<String,Object> findTemplateParams(){
		Map<String,DynaProcessKey> dynaProcessKeysMap = this.findDynaProcessKeys();
		return this.packagingAsTemplateParams(dynaProcessKeysMap);
	}
	
	@Transient
	private Map<String,DynaProcessKey> findDynaProcessKeys(){
		Map<String,DynaProcessKey> dynaProcessKeysMap = new LinkedHashMap<String,DynaProcessKey>();
		//将keys按showOrder升序排序
		List<DynaProcessKey> keysSort = new ArrayList<DynaProcessKey>(keys);
		Collections.sort(keysSort);
		for(DynaProcessKey key : keysSort){
			dynaProcessKeysMap.put(key.getKeyId(), key);
		}
		return dynaProcessKeysMap;
	}
	
	private Map<String,Object> packagingAsTemplateParams(Map<String,DynaProcessKey> dynaProcessKeysMap){
		Map<String,Object> root = new HashMap<String,Object>();
		root.put("params", dynaProcessKeysMap);
		root.put("size", dynaProcessKeysMap.size());
		return root;
	}
	
	/**
	 * 获取任务实例
	 * @param processId
	 * @param processInstanceId
	 * @return
	 */
	public static DynaProcessForm queryDynaProcessFormInstance(String processId, long processInstanceId){
		DynaProcessForm dynaProcessFormInstance = queryActiveDynaProcessFormByProcessId(processId);
		Set<DynaProcessKey> dynaProcessKeys = dynaProcessFormInstance.getKeys();
		for(DynaProcessKey key : dynaProcessKeys){
			List<DynaProcessValue> results = getRepository().createNamedQuery("queryDynaProcessValueByProcessInstanceIdAndKeyId").setParameters(processInstanceId, key.getKeyId()).list();
					
			if (results.size() == 0) {
				key.setKeyValueForShow("");
			} else {
				key.setKeyValueForShow(results.get(0).getKeyValue());
			}
		}
		return dynaProcessFormInstance;
	}

	public String[] businessKeys() {
		return new String[]{};
	}
}
