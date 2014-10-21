package org.openkoala.bpm.designer.web.action.auth;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.struts2.ServletActionContext;
import org.dayatang.querychannel.Page;
import org.openkoala.auth.application.MenuApplication;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.koala.auth.ss3adapter.AuthUserUtil;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;

public class MenuAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private MenuApplication menuApplication;

	private Map<String, Object> dataMap;
	private List<ResourceVO> rows = new ArrayList<ResourceVO>();
	private List<ResourceVO> resourceVOs = new ArrayList<ResourceVO>();
	private String page;
	private String pagesize;
	private ResourceVO resVO;
	private RoleVO roleVO;
	private ResourceVO parent;

	private Page pageResult;

	public RoleVO getRoleVO() {
		return roleVO;
	}

	public void setRoleVO(RoleVO roleVO) {
		this.roleVO = roleVO;
	}

	public MenuApplication getMenuApplication() {
		return menuApplication;
	}

	public void setMenuApplication(MenuApplication menuApplication) {
		this.menuApplication = menuApplication;
	}

	public ResourceVO getParent() {
		return parent;
	}

	public void setParent(ResourceVO parent) {
		this.parent = parent;
	}

	public List<ResourceVO> getResourceVOs() {
		return resourceVOs;
	}

	public void setResourceVOs(List<ResourceVO> resourceVOs) {
		this.resourceVOs = resourceVOs;
	}

	public MenuAction() {
		this.dataMap = new HashMap<String, Object>();
		roleVO = new RoleVO();
		parent = new ResourceVO();
	}

	public Map<String, Object> getDataMap() {
		return this.dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public List<ResourceVO> getRows() {
		return rows;
	}

	public void setRows(List<ResourceVO> rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public Page getPageResult() {
		return pageResult;
	}

	public void setPageResult(Page pageResult) {
		this.pageResult = pageResult;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	public ResourceVO getResVO() {
		return resVO;
	}

	public void setResVO(ResourceVO resVO) {
		this.resVO = resVO;
	}

	public String list() {
		return "METHOD";
	}

	public String findMenuTreeSelectItemByRole() {
		List<ResourceVO> all = this.menuApplication.findAllTreeSelectItemByRole(this.roleVO);
		dataMap.put("data", all);
		return "JSON";
	}

	/**
	 * 查询系统所有菜单
	 * 
	 * @return
	 */
	public String findAllMenuByUser() {
		List<ResourceVO> all = null;
		CustomUserDetails current = AuthUserUtil.getLoginUser();
		if (current.isSuper()) {
			all = this.menuApplication.findAllMenuByUser("");
		} else {
			all = this.menuApplication.findAllMenuByUser(AuthUserUtil.getLoginUserName());
		}
		dataMap.put("data", all);
		return "JSON";
	}

	/**
	 * 查询系统一级主菜单
	 * 
	 * @return
	 */
	public String findTopLevelMenuByUser() {
		List<ResourceVO> all = null;
		CustomUserDetails current = AuthUserUtil.getLoginUser();
		if (current.isSuper()) {
			all = this.menuApplication.findChildByParentAndUser(null, "");
		} else {
			all = this.menuApplication.findChildByParentAndUser(null, AuthUserUtil.getLoginUserName());
		}
		dataMap.put("data", all);
		return "JSON";

	}

	/**
	 * 查询某节点下所有子菜单
	 * 
	 * @return
	 */
	public String findAllSubMenuByParent() {
		List<ResourceVO> all = null;
		CustomUserDetails current = AuthUserUtil.getLoginUser();
		if (current.isSuper()) {
			all = this.menuApplication.findAllChildByParentAndUser(this.resVO, "");
		} else {
			all = this.menuApplication.findAllChildByParentAndUser(this.resVO, AuthUserUtil.getLoginUserName());
		}
		dataMap.put("data", all);
		return "JSON";

	}

	/**
	 * 查询某节点下一级子菜单并判断是否有权限
	 * 
	 * @return
	 */
	public String findChildSelectItemByRole() {
		List<ResourceVO> all = this.menuApplication.findChildSelectItemByRole(parent, this.roleVO);
		dataMap.put("data", all);
		dataMap.put("result", "success");
		return "JSON";

	}

	/**
	 * 查询某节点下一级子菜单并判断是否有权限
	 * 
	 * @return
	 */
	public String findTopSelectItemByRole() {
		dataMap.clear();
		List<ResourceVO> all = this.menuApplication.findChildSelectItemByRole(parent, this.roleVO);
		dataMap.put("data", all);
		dataMap.put("result", "success");
		return "JSON";

	}

	/**
	 * 查询某节点下一级菜单
	 * 
	 * @return
	 */
	public String findChildSubMenuByParent() {
		dataMap.clear();
		List<ResourceVO> all = null;
		CustomUserDetails current = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (current.isSuper()) {
			all = this.menuApplication.findChildByParentAndUser(this.resVO, "");
		} else {
			all = this.menuApplication.findChildByParentAndUser(this.resVO, current.getUsername());
		}
		dataMap.put("data", all);
		return "JSON";

	}

	public String findAllMenu() {
		dataMap.clear();
		List<ResourceVO> all = this.menuApplication.findAllMenu();
		dataMap.put("data", all);
		return "JSON";
	}

	public String findAllMenuTree() {
		dataMap.clear();
		List<ResourceVO> all = this.menuApplication.findMenuTree();
		dataMap.put("data", all);
		return "JSON";
	}

	public String findAllMenuTreeRows() {
		dataMap.clear();
		List<ResourceVO> all = this.menuApplication.findMenuTree();
		dataMap.put("Rows", all);
		return "JSON";
	}

	public String pageJson() {
		dataMap.clear();
		int start = Integer.parseInt(this.page);
		int limit = Integer.parseInt(this.pagesize);
		pageResult = this.menuApplication.pageQueryMenu(start, limit);
		return "PageJSON";

	}

	public String add() {
		this.menuApplication.saveMenu(this.resVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String addAndAssignParent() {
		this.menuApplication.saveAndAssignParent(this.resVO, this.parent);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String del() {
		this.menuApplication.removeMenu(Long.valueOf(this.resVO.getId()));
		dataMap.put("result", "success");
		return "JSON";

	}

	public String update() {
		this.menuApplication.updateMenu(this.resVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String updateMenuOrder() {
		menuApplication.updateMeunOrder(resourceVOs);
		return "JSON";
	}

	/**
	 * 获取菜单图片
	 * 
	 * @return
	 */
	public String getIconNames() {
		Collection<String> filenames = new ArrayList<String>();
		String realPath = ServletActionContext.getServletContext().getRealPath("/images/icons/menu");
		File iconFiles = new File(realPath);
		File[] acceptFiles = getAcceptFiles(iconFiles);
		for (File iconFile : acceptFiles) {
			filenames.add(iconFile.getPath().substring(iconFile.getPath().indexOf("images")) //
					.replaceAll("\\\\", "/"));
		}
		dataMap.put("data", filenames);
		return "JSON";
	}

	/**
	 * 获取符合条件的文件格式
	 * 
	 * @param iconFiles
	 * @return
	 */
	private File[] getAcceptFiles(File iconFiles) {
		return iconFiles.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				String suffix = pathname.getPath().substring(pathname.getPath().lastIndexOf(".") + 1);
				if (suffix.equalsIgnoreCase("gif") || suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpg")) {
					return true;
				}
				return false;
			}
		});
	}
}
