package org.openkoala.bpm.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dayatang.domain.AbstractEntity;
import org.openkoala.koala.commons.domain.KoalaAbstractEntity;

@Entity
@Table
public class FormEntity extends KoalaAbstractEntity {
	
	private static final long serialVersionUID = -8234793881235433137L;

	@Column
	private String name;
	
	@Column
	private String remark;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "FormEntity [name=" + name + ", remark=" + remark + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
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
		FormEntity other = (FormEntity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		return true;
	}

	@Override
	public String[] businessKeys() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
