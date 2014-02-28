package org.openkoala.koala.web.contorller.auth;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.dayatang.querychannel.Page;
import org.openkoala.auth.application.MenuApplication;
import org.openkoala.auth.application.ResourceTypeApplication;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.koala.auth.ss3adapter.AuthUserUtil;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth/Menu")
public class MenuController {

	@Inject
	private ResourceTypeApplication resourceTypeApplication;

	@Inject
	private MenuApplication menuApplication;

	@RequestMapping("/list")
	public String list() {
		return "auth/Menu-list";
	}

	@ResponseBody
	@RequestMapping("/findTopMenuByUser")
	public Map<String, Object> findTopMenuByUser() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<ResourceVO> all = null;
		CustomUserDetails current = AuthUserUtil.getLoginUser();
		if (current.isSuper()) {
			all = this.menuApplication.findTopMenuByUser("");
		} else {
			all = this.menuApplication.findTopMenuByUser(AuthUserUtil.getLoginUserName());
		}
		dataMap.put("data", all);
		return dataMap;
	}

	/**
	 * 查询某节点下所有子菜单
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAllSubMenuByParent")
	public Map<String, Object> findAllSubMenuByParent(ParamsPojo menuPojo) {
		ResourceVO resVO = menuPojo.getResVO();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<ResourceVO> all = null;
		CustomUserDetails current = AuthUserUtil.getLoginUser();
		if (current.isSuper()) {
			all = this.menuApplication.findAllChildByParentAndUser(resVO, "");
		} else {
			all = this.menuApplication.findAllChildByParentAndUser(resVO, AuthUserUtil.getLoginUserName());
		}
		dataMap.put("data", all);
		return dataMap;

	}

	@ResponseBody
	@RequestMapping("/findMenuType")
	public Map<String, Object> findMenuType() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", resourceTypeApplication.findMenuType());
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/findAllMenuTreeRows")
	public Page findAllMenuTreeRows() {
		List<ResourceVO> all = menuApplication.findMenuTree();
		return new Page(0, all.size(), all);
	}

	@ResponseBody
	@RequestMapping("/updateMenuOrder")
	public Map<String, Object> updateMenuOrder(ParamsPojo menuPojo) {
		List<ResourceVO> resourceVOs = menuPojo.getResourceVOs();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		menuApplication.updateMeunOrder(resourceVOs);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/del")
	public Map<String, Object> del(ParamsPojo menuPojo) {
		ResourceVO resVO = menuPojo.getResVO();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		menuApplication.removeMenu(Long.valueOf(resVO.getId()));
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(ParamsPojo menuPojo) {
		ResourceVO resVO = menuPojo.getResVO();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		this.menuApplication.updateMenu(resVO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(ParamsPojo menuPojo) {
		ResourceVO resVO = menuPojo.getResVO();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		menuApplication.saveMenu(resVO);
		dataMap.put("result", "success");
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/addAndAssignParent")
	public Map<String, Object> addAndAssignParent(ParamsPojo menuPojo) {
		ResourceVO resVO = menuPojo.getResVO();
		ResourceVO parent = menuPojo.getParent();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		this.menuApplication.saveAndAssignParent(resVO, parent);
		dataMap.put("result", "success");
		return dataMap;
	}

	/**
	 * 获取菜单图片
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getIconNames")
	public Map<String, Object> getIconNames(HttpServletRequest request) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Collection<String> filenames = new ArrayList<String>();
		String realPath = request.getSession().getServletContext().getRealPath("/images/icons/menu");
		File iconFiles = new File(realPath);
		File[] acceptFiles = getAcceptFiles(iconFiles);
		for (File iconFile : acceptFiles) {
			filenames.add(iconFile.getPath().substring(iconFile.getPath().indexOf("images")) //
					.replaceAll("\\\\", "/"));
		}
		dataMap.put("data", filenames);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/findAllMenuByUser")
	public Map<String, Object> findAllMenuByUser() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<ResourceVO> all = null;
		CustomUserDetails current = AuthUserUtil.getLoginUser();
		if (current.isSuper()) {
			all = this.menuApplication.findAllMenuByUser("");
		} else {
			all = this.menuApplication.findAllMenuByUser(AuthUserUtil.getLoginUserName());
		}
		dataMap.put("data", all);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/findMenuTreeSelectItemByRole")
	public Map<String, Object> findMenuTreeSelectItemByRole(Long roleId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		RoleVO roleVO = new RoleVO();
		roleVO.setId(roleId);
		List<ResourceVO> all = this.menuApplication.findAllTreeSelectItemByRole(roleVO);
		dataMap.put("data", all);
		return dataMap;
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
