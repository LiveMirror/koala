package org.openkoala.koala.web.action.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.openkoala.auth.application.RoleApplication;
import org.openkoala.auth.application.ResourceApplication;
import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.QueryItemVO;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.ss3adapter.ehcache.CacheUtil;
import org.dayatang.querychannel.Page;
import com.opensymphony.xwork2.ActionSupport;

public class RoleAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RoleApplication roleApplication;

	@Inject
	private ResourceApplication urlApplication;

	@Inject
	private UserApplication userApplication;

	private Map<String, Object> dataMap = new HashMap<String, Object>();
	private List<RoleVO> roles = new ArrayList<RoleVO>();
	private List<ResourceVO> menus = new ArrayList<ResourceVO>();
	private String page;
	private String pagesize;
	private RoleVO roleVO;
	private QueryConditionVO search = new QueryConditionVO();
	private List<UserVO> users = new ArrayList<UserVO>();
	private Long userId;
	private String userAccount;
	private List<ResourceVO> addList;
	private List<ResourceVO> delList;

	private String roleNameForSearch;

	public RoleApplication getRoleApplication() {
		return roleApplication;
	}

	public void setRoleApplication(RoleApplication roleApplication) {
		this.roleApplication = roleApplication;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public List<RoleVO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	public RoleVO getRoleVO() {
		return roleVO;
	}

	public void setRoleVO(RoleVO roleVO) {
		this.roleVO = roleVO;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public List<ResourceVO> getMenus() {
		return menus;
	}

	public void setMenus(List<ResourceVO> menus) {
		this.menus = menus;
	}

	public String list() {
		return "METHOD";
	}

	public String getRoleNameForSearch() {
		return roleNameForSearch;
	}

	public void setRoleNameForSearch(String roleNameForSearch) {
		this.roleNameForSearch = roleNameForSearch;
	}

	public List<ResourceVO> getAddList() {
		return addList;
	}

	public void setAddList(List<ResourceVO> addList) {
		this.addList = addList;
	}

	public List<ResourceVO> getDelList() {
		return delList;
	}

	public void setDelList(List<ResourceVO> delList) {
		this.delList = delList;
	}

	public String abolishResource() {
		roleApplication.abolishMenu(roleVO, menus);
		for (ResourceVO mv : menus) {
			CacheUtil.refreshUrlAttributes(mv.getIdentifier());
		}
		dataMap.put("result", "success");
		return "JSON";
	}

	public String queryUrlResourceByRole() {

		dataMap.clear();
		List<ResourceVO> all = roleApplication.findResourceByRole(roleVO);
		dataMap.put("data", all);

		return "JSON";

	}

	public String queryUserByRole() {
		List<UserVO> all = roleApplication.findUserByRole(roleVO);
		dataMap.put("data", all);
		return "JSON";
	}

	public String queryMenuByRole() {
		List<ResourceVO> all = roleApplication.findMenuByRole(roleVO);
		dataMap.put("data", all);
		return "JSON";
	}

	public List<UserVO> getUsers() {
		return users;
	}

	public void setUsers(List<UserVO> users) {
		this.users = users;
	}

	public String pageJson() {
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);
		Page<RoleVO> all = null;
		if (userAccount == null || userAccount.isEmpty()) {
			all = roleApplication.pageQueryRole(start - 1, limit);
		} else {
			List<RoleVO> roles = roleApplication.findRoleByUserAccount(userAccount);
			all = new Page<RoleVO>(start - 1, roles.size(), limit, roles);
		}
		dataMap.put("Rows", all.getData());
		dataMap.put("start", all.getStart());
		dataMap.put("limit", all.getPageSize());
		dataMap.put("Total", all.getResultCount());

		return "JSON";
	}

	public String query() {
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);
		initSearchCondition();

		Page<RoleVO> all = roleApplication.pageQueryByRoleCustom(start - 1, limit, search);
		dataMap.put("Rows", all.getData());
		dataMap.put("start", all.getStart());
		dataMap.put("limit", all.getPageSize());
		dataMap.put("Total", all.getResultCount());
		return "JSON";
	}

	public String queryRolesForAssign() {
		int start = Integer.parseInt(this.page);
		int limit = Integer.parseInt(this.pagesize);

		initSearchCondition();

		Page<RoleVO> all = roleApplication.pageQueryByRoleCustom(start - 1, limit, search);

		if (userId != null) {
			List<RoleVO> roles = all.getData();
			for (RoleVO role : roleApplication.findRoleByUserAccount(userAccount)) {
				roles.remove(role);
			}
		}

		dataMap.put("Rows", roles);
		dataMap.put("start", all.getStart());
		dataMap.put("limit", all.getPageSize());
		dataMap.put("Total", all.getResultCount());
		this.page = "";
		this.pagesize = "";

		return "JSON";
	}

	private void initSearchCondition() {
		search.setObjectName("Role");
		List<QueryItemVO> searchConditions = new ArrayList<QueryItemVO>();
		if (roleNameForSearch != null) {
			if (!roleNameForSearch.isEmpty()) {
				QueryItemVO queryItem = new QueryItemVO();
				queryItem.setPropName("name");

				queryItem.setPropValue("'%" + roleNameForSearch + "%'");

				queryItem.setOperaType(QueryConditionVO.LIKE);
				searchConditions.add(queryItem);
			}
		}
		search.setItems(searchConditions);
	}

	public String queryNotAssignRoleByUser() {
		int start = Integer.parseInt(page);
		int limit = Integer.parseInt(pagesize);

		UserVO userVoForFind = new UserVO();
		userVoForFind.setId(userId);
		Page<RoleVO> all = userApplication.pageQueryNotAssignRoleByUser(start - 1, limit, userVoForFind);

		dataMap.put("Rows", all.getData());
		dataMap.put("start", all.getStart());
		dataMap.put("limit", all.getPageSize());
		dataMap.put("Total", all.getResultCount());

		return "JSON";
	}

	public String add() {
		roleVO.setSerialNumber("test");
		roleApplication.saveRole(roleVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	public String del() {
		for (RoleVO role : roles) {
			roleApplication.removeRole(Long.valueOf(role.getId()));
		}
		dataMap.put("result", "success");
		return "JSON";

	}

	public String update() {
		roleApplication.updateRole(roleVO);
		dataMap.put("result", "success");
		return "JSON";
	}

	/**
	 * 为角色分配菜单资源
	 * 
	 * @return
	 */
	public String assignMenuResources() {
		// 原先拥有的菜单资源
		List<ResourceVO> ownMenus = roleApplication.findAllResourceByRole(roleVO);
		// 勾选中的菜单资源
		List<ResourceVO> tmpList = new ArrayList<ResourceVO>(menus);
		List<ResourceVO> addList = new ArrayList<ResourceVO>();
		List<ResourceVO> delList = new ArrayList<ResourceVO>();
		// 得到相到的菜单资源
		menus.retainAll(ownMenus);
		// 去掉相同的菜单资源
		ownMenus.removeAll(menus);
		// 得到被删除的菜单资源
		delList.addAll(ownMenus);
		// 得到被新添加的菜单资源
		tmpList.removeAll(menus);

		addList.addAll(tmpList);
		roleApplication.abolishMenu(roleVO, delList);
		roleApplication.assignMenuResource(roleVO, addList);
		for (ResourceVO mVO : addList) {
			CacheUtil.refreshUrlAttributes(((ResourceVO) urlApplication.getResource(mVO.getId())).getIdentifier());
		}
		for (ResourceVO mVO : delList) {
			CacheUtil.refreshUrlAttributes(((ResourceVO) urlApplication.getResource(mVO.getId())).getIdentifier());
		}
		dataMap.put("result", "success");

		return "JSON";
	}

	/**
	 * 为角色分配资源
	 * 
	 * @return
	 */
	public String assignResources() {
		if (addList != null && addList.size() > 0) {
			roleApplication.assignMenuResource(roleVO, addList);
		}
		if (delList != null && delList.size() > 0) {
			roleApplication.abolishMenu(roleVO, delList);
		}
		dataMap.put("result", "success");
		return "JSON";
	}

	/**
	 * 为角色分配URL资源
	 * 
	 * @return
	 */
	public String assignUrlResources() {

		roleApplication.assignMenuResource(roleVO, menus);
		for (ResourceVO mVO : menus) {
			CacheUtil.refreshUrlAttributes(mVO.getIdentifier());
		}
		dataMap.put("result", "success");

		return "JSON";
	}

	/**
	 * 查询某个角色下的所有用户
	 * 
	 * @return
	 */
	public String findUsersByRole() {
		dataMap.put("roleUsers", roleApplication.findUserByRole(roleVO));
		dataMap.put("result", "success");
		return "JSON";
	}

	/**
	 * 给角色分配用户
	 * 
	 * @return
	 */
	public String assignUsers() {
		for (UserVO user : users) {
			roleApplication.assignUser(roleVO, user);
			CacheUtil.refreshUserAttributes(((UserVO) userApplication.getUser(user.getId())).getUserAccount());
		}
		dataMap.put("result", "success");
		return "JSON";
	}

	/**
	 * 返回角色所拥有的资源页面
	 * 
	 * @return
	 */
	public String resourceList() {
		return "METHOD";
	}

	/**
	 * 删除某个用户下的某个角色
	 * 
	 * @return
	 */
	public String removeRoleForUser() {
		UserVO user = new UserVO();
		user.setId(userId);
		userApplication.abolishRole(user, roles);
		CacheUtil.refreshUserAttributes(((UserVO) userApplication.getUser(user.getId())).getUserAccount());
		dataMap.put("result", "success");
		return "JSON";
	}

}
