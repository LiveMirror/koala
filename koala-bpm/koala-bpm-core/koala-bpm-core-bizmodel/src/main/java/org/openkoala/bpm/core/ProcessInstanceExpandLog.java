package org.openkoala.bpm.core;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.dayatang.domain.AbstractEntity;
import org.dayatang.domain.CriteriaQuery;
import org.openkoala.koala.commons.domain.KoalaAbstractEntity;

@Entity
@Table
public class ProcessInstanceExpandLog extends KoalaAbstractEntity{
	
	private static final long serialVersionUID = -4360238319408676454L;
	
	private long instanceLogId;
	
	private String processName;//步骤名称
	
	
	@Lob
	private String processData;//流程级参数
	
	private int state;
	
	public long getInstanceLogId() {
	
		return instanceLogId;
	}

	public void setInstanceLogId(long instanceLogId) {
	
		this.instanceLogId = instanceLogId;
	}

	public String getProcessName() {
	
		return processName;
	}

	public void setProcessName(String processName) {
	
		this.processName = processName;
	}
	
	public String getProcessData() {
		return processData;
	}

	public void setProcessData(String processData) {
		this.processData = processData;
	}

	public int getState() {
	
		return state;
	}

	public void setState(int state) {
	
		this.state = state;
	}
	
	public static ProcessInstanceExpandLog find(Map<String,Object> params){
		CriteriaQuery query = getRepository().createCriteriaQuery(ProcessInstanceExpandLog.class);
		
		for(String prop:params.keySet()){
			if(params.get(prop)!=null){
				query.eq(prop, params.get(prop));
			}
		}
		return query.singleResult();
	}
	
	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result+ ((processName == null) ? 0 : processName.hashCode());
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
		ProcessInstanceExpandLog other = (ProcessInstanceExpandLog) obj;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		return true;
		
	}

	@Override
	public String toString() {
		return "ProcessInstanceExpandLog [processName=" + processName + "]";
	}

	@Override
	public String[] businessKeys() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
