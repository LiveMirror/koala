package org.openkoala.bpm.action.core;

import java.util.List;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.bpm.action.BaseAction;
import org.openkoala.bpm.application.JBPMApplication;
import org.openkoala.bpm.application.KoalaAssignInfoApplication;
import org.openkoala.bpm.application.vo.*;

public class KoalaAssignInfoAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private KoalaAssignInfoVO koalaAssignInfoVO = new KoalaAssignInfoVO();

	@Inject
	private KoalaAssignInfoApplication koalaAssignInfoApplication;

	@Inject
	private JBPMApplication jbpmApplication;

	private List<ProcessVO> processes;

	private Page pageResult;

	public String add() {
		try {
			koalaAssignInfoApplication.saveKoalaAssignInfo(koalaAssignInfoVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataMap.put("result", "success");
		return "JSON";
	}

	public String update() {
		koalaAssignInfoApplication.updateKoalaAssignInfo(koalaAssignInfoVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String pageJson() {
		pageResult = koalaAssignInfoApplication.pageQueryKoalaAssignInfo(koalaAssignInfoVO, page, pagesize);
		return "PageJSON";
	}

	public String delete() {
		String idsString = getRequest().getParameter("ids");
		if (idsString != null) {
			String[] idArrs = idsString.split(",");
			Long[] ids = new Long[idArrs.length];
			for (int i = 0; i < idArrs.length; i++) {
				ids[i] = Long.parseLong(idArrs[i]);
			}
			koalaAssignInfoApplication.removeKoalaAssignInfos(ids);
		}

		dataMap.put("result", "success");
		return "JSON";
	}

	public String get() {
		dataMap.put("data", koalaAssignInfoApplication.getKoalaAssignInfo(koalaAssignInfoVO.getId()));
		return "JSON";
	}

	public String queryProcess() {
		this.processes = jbpmApplication.getProcesses();
		return "PROCESS";
	}

	public String findJbpmNamesByKoalaAssignInfo() {
		pageResult = koalaAssignInfoApplication.findJbpmNamesByKoalaAssignInfo(koalaAssignInfoVO.getId(), page, pagesize);
		return "PageJSON";
	}

	public void setKoalaAssignInfoVO(KoalaAssignInfoVO koalaAssignInfoVO) {
		this.koalaAssignInfoVO = koalaAssignInfoVO;
	}

	public KoalaAssignInfoVO getKoalaAssignInfoVO() {
		return this.koalaAssignInfoVO;
	}

	public List<ProcessVO> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessVO> processes) {
		this.processes = processes;
	}

	public Page getPageResult() {
		return pageResult;
	}

	public void setPageResult(Page pageResult) {
		this.pageResult = pageResult;
	}

}