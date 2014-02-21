
package org.openkoala.bpm.action.core;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.bpm.action.BaseAction;
import org.openkoala.bpm.application.KoalaJbpmVariableApplication;
import org.openkoala.bpm.application.vo.*;

public class KoalaJbpmVariableAction extends BaseAction {
		
	private static final long serialVersionUID = 1L;
	
	private KoalaJbpmVariableVO koalaJbpmVariableVO = new KoalaJbpmVariableVO();
		
	@Inject
	private KoalaJbpmVariableApplication koalaJbpmVariableApplication;
	
	public String add() {
		koalaJbpmVariableVO.setScope(koalaJbpmVariableVO.getScopeType()+koalaJbpmVariableVO.getScope());
		koalaJbpmVariableApplication.saveKoalaJbpmVariable(koalaJbpmVariableVO);
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String update() {
		koalaJbpmVariableApplication.updateKoalaJbpmVariable(koalaJbpmVariableVO);
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String pageJson() {
		Page<KoalaJbpmVariableVO> all = koalaJbpmVariableApplication.pageQueryKoalaJbpmVariable(koalaJbpmVariableVO, page, pagesize);
		dataMap.put("Rows", all.getData());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", all.getResultCount());
		return "JSON";
	}
	
	public String delete() {
	    String idsString = getRequest().getParameter("ids");
        if(idsString != null){
            String[] idArrs = idsString.split(",");
            Long[] ids = new Long[idArrs.length];
            for (int i = 0; i < idArrs.length; i ++) {
            	ids[i] = Long.parseLong(idArrs[i]);
            }
            koalaJbpmVariableApplication.removeKoalaJbpmVariables(ids);
        } 
        
		dataMap.put("result", "success");
		return "JSON";
	}
	
	public String get() {
		dataMap.put("data", koalaJbpmVariableApplication.getKoalaJbpmVariable(koalaJbpmVariableVO.getId()));
		return "JSON";
	}
	
	public void setKoalaJbpmVariableVO(KoalaJbpmVariableVO koalaJbpmVariableVO) {
		this.koalaJbpmVariableVO = koalaJbpmVariableVO;
	}
	
	public KoalaJbpmVariableVO getKoalaJbpmVariableVO() {
		return this.koalaJbpmVariableVO;
	}
}