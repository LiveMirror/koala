package org.openkoala.bpm.action.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.auth.application.ResourceTypeApplication;
import org.openkoala.auth.application.vo.ResourceTypeVO;

import com.opensymphony.xwork2.ActionSupport;

public class ResourceTypeAction extends ActionSupport {

	private static final long serialVersionUID = -6615121906320422295L;

	private Map<String, Object> dataMap = new HashMap<String, Object>();

	private ResourceTypeVO resourceTypeVO;

	private List<ResourceTypeVO> resourceTypeVOs = new ArrayList<ResourceTypeVO>();

	@Inject
	private ResourceTypeApplication resourceTypeApplication;

	private int page;

	private int pagesize;

	private Page pageResult;

	public String save() {
		resourceTypeApplication.save(resourceTypeVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String delete() {
		resourceTypeApplication.delete(resourceTypeVOs.toArray(new ResourceTypeVO[resourceTypeVOs.size()]));
		dataMap.put("result", "success");
		return "JSON";
	}

	public String update() {
		resourceTypeApplication.update(resourceTypeVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String pageJson() {
		pageResult = resourceTypeApplication.pageQuery(page, pagesize);
		return "PageJSON";
	}

	public String list() {
		return "METHOD";
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public ResourceTypeVO getResourceTypeVO() {
		return resourceTypeVO;
	}

	public void setResourceTypeVO(ResourceTypeVO resourceTypeVO) {
		this.resourceTypeVO = resourceTypeVO;
	}

	public List<ResourceTypeVO> getResourceTypeVOs() {
		return resourceTypeVOs;
	}

	public void setResourceTypeVOs(List<ResourceTypeVO> resourceTypeVOs) {
		this.resourceTypeVOs = resourceTypeVOs;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public Page getPageResult() {
		return pageResult;
	}

	public void setPageResult(Page pageResult) {
		this.pageResult = pageResult;
	}

}
