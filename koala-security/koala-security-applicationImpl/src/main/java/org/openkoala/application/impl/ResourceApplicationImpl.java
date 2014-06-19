package org.openkoala.application.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.querychannel.Page;
import org.dayatang.utils.DateUtils;
import org.openkoala.auth.application.ResourceApplication;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.koala.auth.core.domain.Resource;
import org.openkoala.koala.auth.core.domain.ResourceLineAssignment;
import org.openkoala.koala.auth.core.domain.ResourceType;
import org.openkoala.koala.auth.core.domain.ResourceTypeAssignment;
import org.openkoala.koala.auth.core.domain.Role;
import org.openkoala.util.DateFormatUtils;
import org.springframework.transaction.annotation.Transactional;

//@Remote
@Named("urlApplication")
//@Stateless(name = "ResourceApplication")
@Transactional(value = "transactionManager_security")
//@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
public class ResourceApplicationImpl extends BaseImpl implements ResourceApplication {

	public static ResourceVO domainObject2Vo(Resource resource) {
		ResourceVO treeVO = new ResourceVO();
		treeVO.setId(resource.getId() == null ? null : resource.getId());
		treeVO.setName(resource.getName());
		treeVO.setText(resource.getName());
		treeVO.setDesc(resource.getDesc());
		treeVO.setIdentifier(resource.getIdentifier());
		treeVO.setAbolishDate(DateFormatUtils.format(resource.getAbolishDate()));
		treeVO.setCreateDate(DateFormatUtils.format(resource.getCreateDate()));
		treeVO.setSortOrder(resource.getSortOrder());
		treeVO.setLevel(resource.getLevel() == null ? "0" : resource.getLevel().toString());
		ResourceTypeAssignment assignment = ResourceTypeAssignment.findByResource(resource.getId());
		treeVO.setTypeId(assignment == null ? "" : String.valueOf(assignment.getResourceType().getId()));
		treeVO.setTypeName(assignment == null ? "" : assignment.getResourceType().getName());
		treeVO.setVersion(resource.getVersion());
		return treeVO;
	}

	public static Resource vo2DomainObject(ResourceVO vo) {
		Resource resource = new Resource();
		resource.setId(vo.getId());
		resource.setName(vo.getName());
		resource.setIdentifier(vo.getIdentifier());
		resource.setName(vo.getName());
		resource.setAbolishDate(DateUtils.MAX_DATE);
		resource.setCreateDate(new Date());
		resource.setSerialNumber("0");
		resource.setDesc(vo.getDesc());
		resource.setVersion(vo.getVersion());
		resource.setSortOrder(vo.getSortOrder());
		return resource;
	}

	public boolean isResourceEmpty() {
		String jpql = "select count(r.name) from Resource r";
		Long count = (Long) queryChannel().createJpqlQuery(jpql).singleResult();
		if (count == 0) {
			return true;
		}
		return false;
	}

	/*
	 * @see org.openkoala.auth.application.ResourceApplication#findResource(org.openkoala.auth.application.vo.ResourceVO, boolean)
	 */
	public List<ResourceVO> findResource(ResourceVO params, boolean loadChildNode) {
		List<ResourceVO> result = new ArrayList<ResourceVO>();

		StringBuilder hql = new StringBuilder("from Resource r where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		if (params != null) {
			if (StringUtils.isNotBlank(params.getName())) {
				hql.append(" and r.name like ?");
				conditionVals.add("%" + params.getName() + "%");
			}

			if (StringUtils.isNotBlank(params.getIdentifier())) {
				hql.append(" and r.identifier like ?");
				conditionVals.add("%" + params.getIdentifier() + "%");
			}

			if (StringUtils.isNotBlank(params.getLevel())) {
				hql.append(" and r.level = ?");
				conditionVals.add(params.getLevel());
			}
		}

		List<Resource> list = queryChannel().createJpqlQuery(hql.toString()).setParameters(conditionVals).list();
		if (list != null) {
			for (Resource o : list) {
				result.add(domainObject2Vo(o));
				if (loadChildNode) {
				}
			}
		}
		return result;
	}

	/*
	 * @see org.openkoala.auth.application.ResourceApplication#loadFirstStageResource(java.lang.Long)
	 */
	public List<ResourceVO> loadFirstStageResource(Long resourceId) {

		return null;
	}

	public List<ResourceVO> findResourceTree() {
//		List<ResourceVO> treeVOs = new ArrayList<ResourceVO>();
//		List<Resource> topResources = Resource.findChildByParent(null);
//		for (Resource res : topResources) {
//			if (!Resource.isMenu(res)) {
//				ResourceVO treeVO = domainObject2Vo(res);
//				treeVOs.add(treeVO);
//				innerFindResourceByParent(treeVO, null);
//			}
//		}
//		return treeVOs;
		
		List<ResourceVO> treeVOs = new ArrayList<ResourceVO>();

		String selectTopResource = "SELECT DISTINCT NEW org.openkoala.auth.application.vo.ResourceVO("
				+ "resource.id, resource.desc, resource.version, resource.menuIcon, resource.level, "
				+ "resource.identifier, resource.valid, resource.name, resource.name, "
				+ "resource.sortOrder, resource.serialNumber, resource.abolishDate, resource.createDate, resourceType.id) "
				+ "FROM ResourceTypeAssignment assignment LEFT JOIN assignment.resource resource LEFT JOIN assignment.resourceType resourceType "
				+ "WHERE resource.level=1 AND resource.abolishDate>:abolishDate AND resourceType.name <> :dir AND resourceType.name <> :menu ORDER BY resource.level ,resource.sortOrder ,resource.createDate ,resource.name";
		treeVOs = queryChannel().createJpqlQuery(selectTopResource).addParameter("abolishDate", new Date())
				.addParameter("dir", "KOALA_MENU")
				.addParameter("menu", "KOALA_DIRETORY")
				.list();

		String selectAllResource = "SELECT DISTINCT NEW org.openkoala.auth.application.vo.ResourceVO("
				+ "resourceLineAssignment.parent.id,resource.id, resource.desc, resource.version, resource.menuIcon, resource.level, "
				+ "resource.identifier, resource.valid, resource.name, resource.name, "
				+ "resource.sortOrder, resource.serialNumber, resource.abolishDate, resource.createDate, resourceType.id)"
				+ "FROM ResourceLineAssignment resourceLineAssignment LEFT JOIN resourceLineAssignment.child resource, "
				+ "ResourceTypeAssignment assignment LEFT JOIN assignment.resource _resource LEFT JOIN assignment.resourceType resourceType "
				+ "WHERE resourceLineAssignment.parent.id IS NOT NULL AND resource.abolishDate>:abolishDate AND resource.id = _resource.id "
				+ "AND resourceType.name <> :dir AND resourceType.name <> :menu "
				+ "ORDER BY resource.level ,resource.sortOrder ,resource.createDate ,resource.name";

		List<ResourceVO> all = queryChannel().createJpqlQuery(selectAllResource.toString()).addParameter("abolishDate", new Date())
				.addParameter("dir", "KOALA_MENU")
				.addParameter("menu", "KOALA_DIRETORY")
				.list();

		all.addAll(treeVOs);
		if (!all.isEmpty()) {
			LinkedHashMap<Long, ResourceVO> map = new LinkedHashMap<Long, ResourceVO>();
			for (ResourceVO resourceVO : all) {
				map.put(resourceVO.getId(), resourceVO);
			}
			for (ResourceVO resourceVO : map.values()) {
				Long pid = resourceVO.getParentId();
				if (pid == null || map.get(pid) == null) {
					continue;
				}
				map.get(pid).getChildren().add(resourceVO);
			}
		}
		return treeVOs;

	}

	public void assign(ResourceVO parentVO, ResourceVO childVO) {
		Resource parent = new Resource();
		parent.setId(parentVO.getId());
		Resource child = Resource.load(Resource.class, childVO.getId());
		child.setLevel(parentVO.getLevel() == null ? "1" : String.valueOf(Integer.parseInt(parentVO.getLevel()) + 1));
		parent.assignChild(child);
	}

	public ResourceVO saveAndAssignParent(ResourceVO resourceVO, ResourceVO parent) {
		ResourceVO child = this.saveResource(resourceVO);
		this.assign(parent, child);
		return child;
	}

	private void innerFindResourceByParent(ResourceVO parent, RoleVO roleVO) {
		List<ResourceVO> childs = new ArrayList<ResourceVO>();
		List<Resource> resources = Resource.findChildByParent(parent.getId());
		for (Resource res : resources) {
			if (!Resource.isMenu(res)) {
				ResourceVO treeVO = domainObject2Vo(res);
				if (roleVO != null) {
					treeVO.setIschecked(Resource.hasPrivilegeByRole(res.getId(), roleVO.getId()));
				}
				childs.add(treeVO);
				parent.setChildren(childs);
				innerFindResourceByParent(treeVO, roleVO);
			}
		}
	}

	public ResourceVO getResource(Long resourceId) {
		return domainObject2Vo(Resource.get(Resource.class, resourceId));
	}

	public ResourceVO saveResource(ResourceVO resourceVO) {
		if (isNameExist(resourceVO)) {
			throw new ApplicationException("resource.name.exist");
		}
		if(StringUtils.isBlank(resourceVO.getIdentifier())){
			throw new ApplicationException("resource.identifier.null");
		}
		if (isIdentifierExist(resourceVO)) {
			throw new ApplicationException("resource.identifier.exist");
		}
		Resource resource = vo2DomainObject(resourceVO);
		resource.setLevel("1");
		resource.save();
		saveResourceTypeAssignment(resourceVO, resource);
		resourceVO.setId(resource.getId());
		return resourceVO;
	}

	private void saveResourceTypeAssignment(ResourceVO resourceVO, Resource resource) {
		ResourceTypeAssignment resourceTypeAssignment = new ResourceTypeAssignment();
		resourceTypeAssignment.setCreateDate(new Date());
		resourceTypeAssignment.setAbolishDate(DateUtils.MAX_DATE);
		resourceTypeAssignment.setResource(resource);
		resourceTypeAssignment.setResourceType(ResourceType.load(ResourceType.class, Long.valueOf(resourceVO.getTypeId())));
		resourceTypeAssignment.save();
	}

	public void updateResource(ResourceVO vo) {

		Resource resource = Resource.load(Resource.class, vo.getId());

		if (!resource.getName().equals(vo.getName())) {
			if (isNameExist(vo)) {
				throw new ApplicationException("resource.name.exist");
			}
		}

		if (!resource.getIdentifier().equals(vo.getIdentifier())) {
			if (isIdentifierExist(vo)) {
				throw new ApplicationException("resource.identifier.exist");
			}
		}

		resource.setName(vo.getName());
		resource.setIdentifier(vo.getIdentifier());
		resource.setDesc(vo.getDesc());
		ResourceTypeAssignment resourceTypeAssignment = ResourceTypeAssignment.findByResource(vo.getId());
		if (resourceTypeAssignment == null) {
			resourceTypeAssignment = new ResourceTypeAssignment();
			resourceTypeAssignment.setAbolishDate(DateUtils.MAX_DATE);
			resourceTypeAssignment.setCreateDate(new Date());
			resourceTypeAssignment.setResource(resource);
			resourceTypeAssignment.setResourceType(ResourceType.load(ResourceType.class, Long.valueOf(vo.getTypeId())));
			resourceTypeAssignment.save();
			return;
		}
		resourceTypeAssignment.setResourceType(ResourceType.load(ResourceType.class, Long.valueOf(vo.getTypeId())));
	}

	public void removeResource(long id) {
		Resource resource = Resource.load(Resource.class, id);
		resource.removeResource();
	}

	public List<ResourceVO> findAllResource() {
		List<ResourceVO> list = new ArrayList<ResourceVO>();
		List<Resource> all = Resource.findAll(Resource.class);
		for (Resource res : all) {
			list.add(domainObject2Vo(res));
		}
		return list;
	}

	public List<RoleVO> findAllRoleByResource(ResourceVO urlVO) {
		List<RoleVO> list = new ArrayList<RoleVO>();
		List<Role> all = Resource.findRoleByResource(urlVO.getIdentifier());
		for (Role role : all) {
			RoleVO roleVO = new RoleVO();
			roleVO.setAbolishDate(role.getAbolishDate().toString());
			roleVO.setCreateDate(role.getCreateDate().toString());
			roleVO.setCreateOwner(role.getCreateOwner());
			roleVO.setId(role.getId());
			roleVO.setName(role.getName());
			roleVO.setRoleDesc(role.getRoleDesc());
			roleVO.setSerialNumber(role.getSerialNumber());
			roleVO.setSortOrder(role.getSortOrder());
			roleVO.setValid(role.isValid());
			list.add(roleVO);
		}
		return list;
	}

	private Page<ResourceVO> basePageQuery(String query, Object[] params, int currentPage, int pageSize) {
		List<ResourceVO> result = new ArrayList<ResourceVO>();
		Page<Resource> pages = queryChannel().createJpqlQuery(query).setParameters(Arrays.asList(params)).setPage(currentPage, pageSize).pagedList();
		for (Resource ne : pages.getData()) {
			result.add(domainObject2Vo(ne));
		}
		Page<ResourceVO> returnPage = new Page<ResourceVO>(pages.getStart(), pages.getResultCount(), pages.getPageSize(), result);
		return returnPage;
	}

	public Page<ResourceVO> pageQueryNotAssignByRole(int currentPage, int pageSize, RoleVO roleVO) {
		return basePageQuery("select k from org.openkoala.koala.auth.core.domain.UrlResource k where k.id "
				+ "not in(select r.id from org.openkoala.koala.auth.core.domain.Role m," + "org.openkoala.koala.auth.core.domain.UrlResource r,"
				+ " org.openkoala.koala.auth.core.domain.IdentityResourceAuthorization t where m.id=t.identity.id and r.id=t.resource.id " + " and m.id=?1)",
				new Object[] { roleVO.getId() }, currentPage, pageSize);
	}

	public boolean isNameExist(ResourceVO resourceVO) {
		return getResource(resourceVO).isNameExist();
	}

	public boolean isIdentifierExist(ResourceVO resourceVO) {
		return getResource(resourceVO).isIdentifierExist();
	}

	private Resource getResource(ResourceVO resourceVO) {
		Resource resource = new Resource();
		resource.setName(resourceVO.getName());
		resource.setIdentifier(resourceVO.getIdentifier());
		return resource;
	}

	private static final String MENUICON = "glyphicon  glyphicon-list-alt";
	private static final String ORGANIATION = "organization";

	public void initMenus(String type, List<String> inits) {
		this.initResourceMenu();
		this.initUserManagerMenu();
		if (inits != null && inits.contains(ORGANIATION)) {
			this.initOrganizationMenu();
		}

	}

	/**
	 * 初始化资源菜单
	 */
	private void initResourceMenu() {
		ResourceType koalaMenu = ResourceType.newResourceType("KOALA_MENU");
		ResourceType koalaDirectory = ResourceType.newResourceType("KOALA_DIRETORY");
		koalaMenu.save();
		koalaDirectory.save();

		Resource resourceManager = Resource.newResource("资源", "resource", "1", MENUICON);
		Resource resource = Resource.newResource("资源管理", "/pages/auth/resource-list.jsp", "2", MENUICON);
		Resource menuResource = Resource.newResource("菜单管理", "/pages/auth/menu-list.jsp", "2", MENUICON);
		Resource typeResource = Resource.newResource("资源类型管理", "/pages/auth/resource-type-list.jsp", "2", MENUICON);
		resourceManager.save();
		resource.save();
		menuResource.save();
		typeResource.save();

		ResourceLineAssignment.newResourceLineAssignment(resourceManager, resource).save();
		ResourceLineAssignment.newResourceLineAssignment(resourceManager, menuResource).save();
		ResourceLineAssignment.newResourceLineAssignment(resourceManager, typeResource).save();

		ResourceTypeAssignment.newResourceTypeAssignment(resourceManager, koalaDirectory).save();
		ResourceTypeAssignment.newResourceTypeAssignment(resource, koalaMenu).save();
		ResourceTypeAssignment.newResourceTypeAssignment(menuResource, koalaMenu).save();
		ResourceTypeAssignment.newResourceTypeAssignment(typeResource, koalaMenu).save();

	}

	/**
	 * 初始化用户管理菜单
	 */
	private void initUserManagerMenu() {

		ResourceType koalaMenu = ResourceType.newResourceType("KOALA_MENU");
		ResourceType koalaDirectory = ResourceType.newResourceType("KOALA_DIRETORY");
		koalaMenu.save();
		koalaDirectory.save();

		Resource userRoleResource = Resource.newResource("用户角色管理", "userole", "1", MENUICON);
		Resource userManager = Resource.newResource("用户管理", "/pages/auth/user-list.jsp", "2", MENUICON);
		Resource roleManager = Resource.newResource("角色管理", "/pages/auth/role-list.jsp", "2", MENUICON);

		userRoleResource.save();
		userManager.save();
		roleManager.save();

		ResourceLineAssignment.newResourceLineAssignment(userRoleResource, userManager).save();
		ResourceLineAssignment.newResourceLineAssignment(userRoleResource, roleManager).save();

		ResourceTypeAssignment.newResourceTypeAssignment(userRoleResource, koalaDirectory).save();
		ResourceTypeAssignment.newResourceTypeAssignment(userManager, koalaMenu).save();
		ResourceTypeAssignment.newResourceTypeAssignment(roleManager, koalaMenu).save();
	}

	/**
	 * 初始化组织菜单
	 */
	private void initOrganizationMenu() {
		ResourceType koalaMenu = ResourceType.newResourceType("KOALA_MENU");
		ResourceType koalaDirectory = ResourceType.newResourceType("KOALA_DIRETORY");
		koalaMenu.save();
		koalaDirectory.save();

		Resource organization = Resource.newResource("组织机构", "organization", "1", MENUICON);
		Resource department = Resource.newResource("机构管理", "/pages/organisation/departmentList.jsp", "2", MENUICON);
		Resource job = Resource.newResource("职务管理", "/pages/organisation/jobList.jsp", "2", MENUICON);
		Resource position = Resource.newResource("岗位管理", "/pages/organisation/positionList.jsp", "2", MENUICON);
		Resource employee = Resource.newResource("人员管理", "/pages/organisation/employeeList.jsp", "2", MENUICON);
		organization.save();
		department.save();
		job.save();
		position.save();
		employee.save();

		ResourceLineAssignment.newResourceLineAssignment(organization, department).save();
		ResourceLineAssignment.newResourceLineAssignment(organization, job).save();
		ResourceLineAssignment.newResourceLineAssignment(organization, position).save();
		ResourceLineAssignment.newResourceLineAssignment(organization, employee).save();

		ResourceTypeAssignment.newResourceTypeAssignment(organization, koalaDirectory).save();

		ResourceTypeAssignment.newResourceTypeAssignment(department, koalaMenu).save();
		ResourceTypeAssignment.newResourceTypeAssignment(job, koalaMenu).save();
		ResourceTypeAssignment.newResourceTypeAssignment(position, koalaMenu).save();
		ResourceTypeAssignment.newResourceTypeAssignment(employee, koalaMenu).save();

	}

}
