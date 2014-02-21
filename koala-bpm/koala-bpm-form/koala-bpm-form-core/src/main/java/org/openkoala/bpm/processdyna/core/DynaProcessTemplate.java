package org.openkoala.bpm.processdyna.core;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.dayatang.domain.AbstractEntity;

/**
 * 动态表单模板实体类
 * 
 * @author lingen
 * 
 */
@Entity
@Table(name = "DYNA_PROCESS_TEMPLATE")
@NamedQueries({
	@NamedQuery(name = "isExisted", query = "select o from DynaProcessTemplate o where o.templateName=?")
})
public class DynaProcessTemplate extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1090840348182791271L;
	
	/**
	 * 模板名称
	 */

	private String templateName;

	/**
	 * 模板的描述
	 */
	
	private String templateDescription;

	/**
	 * 模板的内容
	 */
	
	private String templateData;

	public DynaProcessTemplate(String templateName, String templateData) {
		super();
		this.templateName = templateName;
		this.templateData = templateData;
	}

	public DynaProcessTemplate(String templateName, String templateDescription,
			String templateData) {
		super();
		this.templateName = templateName;
		this.templateDescription = templateDescription;
		this.templateData = templateData;
	}

	public DynaProcessTemplate() {
		super();
	}

	@Column(name = "TEMPLATE_NAME")
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Column(name = "TEMPLATE_DESCRIPTION")
	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	@Lob
	@Column(name = "TEMPLATE_DATA")
	public String getTemplateData() {
		return templateData;
	}

	public void setTemplateData(String templateData) {
		this.templateData = templateData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((templateData == null) ? 0 : templateData.hashCode());
		result = prime
				* result
				+ ((templateDescription == null) ? 0 : templateDescription
						.hashCode());
		result = prime * result
				+ ((templateName == null) ? 0 : templateName.hashCode());
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
		DynaProcessTemplate other = (DynaProcessTemplate) obj;
		if (templateData == null) {
			if (other.templateData != null)
				return false;
		} else if (!templateData.equals(other.templateData))
			return false;
		if (templateDescription == null) {
			if (other.templateDescription != null)
				return false;
		} else if (!templateDescription.equals(other.templateDescription))
			return false;
		if (templateName == null) {
			if (other.templateName != null)
				return false;
		} else if (!templateName.equals(other.templateName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DynaProcessTemplate [templateName=" + templateName
				+ ", templateDescription=" + templateDescription
				+ ", templateData=" + templateData + "]";
	}

	/** =============领域方法================= **/

	/**
	 * 保存一个模板，确保模板的 NAME 在数据库中是唯一的
	 */
	public void save() {
		List<DynaProcessTemplate> templates = DynaProcessTemplate
				.getRepository().createNamedQuery("getTemplateData").setParameters(templateName).list();

		if (templates.size() > 0) {
			this.setId(templates.get(0).getId());
		}
		super.save();
	}
	
	public static boolean checkIsExistedByName(String templateName){
		return !getRepository().createNamedQuery("isExisted").setParameters(templateName).list().isEmpty();
	}

	/**
	 *  根据模板名称返回模板内容
	 * 
	 * @param templateKey
	 * @return
	 */
	public static String getTemplateData(String templateName) {
		List<DynaProcessTemplate> templates = DynaProcessTemplate
				.getRepository().createNamedQuery("getTemplateData").setParameters(templateName).list();
		if (templates.size() == 0) {
			return null;
		} else {
			return templates.get(0).getTemplateData();
		}
	}

	@Override
	public String[] businessKeys() {
		// TODO Auto-generated method stub
		return new String[]{};
	}
}
