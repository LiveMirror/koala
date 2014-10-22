package org.openkoala.bpm.action.core;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.bpm.action.BaseAction;
import org.openkoala.bpm.application.JoinAssignApplication;
import org.openkoala.bpm.application.vo.JoinAssignVO;

public class JoinAssignAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private JoinAssignVO joinAssignVO = new JoinAssignVO();

	private Page pageResult;

	@Inject
	private JoinAssignApplication joinAssignApplication;

	public String add() {
		joinAssignApplication.saveJoinAssign(joinAssignVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String update() {
		joinAssignApplication.updateJoinAssign(joinAssignVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String pageJson() {
		pageResult = joinAssignApplication.pageQueryJoinAssign(joinAssignVO, page, pagesize);
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
			joinAssignApplication.removeJoinAssigns(ids);
		}

		dataMap.put("result", "success");
		return "JSON";
	}

	public String get() {
		dataMap.put("data", joinAssignApplication.getJoinAssign(joinAssignVO.getId()));
		return "JSON";
	}

	public void setJoinAssignVO(JoinAssignVO joinAssignVO) {
		this.joinAssignVO = joinAssignVO;
	}

	public JoinAssignVO getJoinAssignVO() {
		return this.joinAssignVO;
	}

	public Page getPageResult() {
		return pageResult;
	}

	public void setPageResult(Page pageResult) {
		this.pageResult = pageResult;
	}

}