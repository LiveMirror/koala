package org.openkoala.security.facade.impl;

import static org.junit.Assert.*;
import static org.openkoala.security.facade.impl.assembler.GenerateDTOUtils.*;
import static org.openkoala.security.facade.impl.util.EntitiesHelper.*;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.*;

/**
 * 先忽略测试。
 */
@Ignore
public class SecurityAccessFacadeTest extends AbstractFacadeIntegrationTestCase{

	@Inject
	private SecurityAccessFacade securityAccessFacade;
	
	private User user;
	private UserDTO userDTO;
	
	private Role role;
	private RoleDTO roleDTO;
	
	private Permission permission;
	private PermissionDTO permissionDTO;
	
	private MenuResource menuResource;
	
	private UrlAccessResource urlAccessResource;
	private UrlAccessResourceDTO urlAccessResourceDTO;
	
	private PageElementResource pageElementResource;
	private PageElementResourceDTO pageElementResourceDTO;
	
	private int currentPage = 0;
	private int pageSize = 10;
	
	@Before
	public void setUp(){
		user = initUser();
		user.save();
		userDTO = generateUserDTOBy(user);
		
		role = initRole();
		role.save();
		roleDTO = generateRoleDTOBy(role);
		
		user.grant(role);
		
		permission = initPermission();
		permission.save();
		permissionDTO = generatePermissionDTOBy(permission);
		
		menuResource = initMenuResource();
		menuResource.save();
		
		role.addSecurityResource(menuResource);
		permission.addSecurityResource(menuResource);
		
		urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		urlAccessResourceDTO = generateUrlAccessResourceDTOBy(urlAccessResource);
		
		pageElementResource = initPageElementResource();
		pageElementResource.save();
		pageElementResourceDTO = generatePageElementResourceDTOBy(pageElementResource);
		
		role.addSecurityResource(urlAccessResource);
		permission.addSecurityResource(urlAccessResource);
		
		role.addSecurityResource(pageElementResource);
		permission.addSecurityResource(pageElementResource);
		
		role.addPermission(permission);
	}
	
	@Test
	public void testGetUserDtoById() {
		Long userId = user.getId();
		assertNotNull(userId);
		UserDTO getUserDTO = securityAccessFacade.getUserById(userId);
		assertNotNull(getUserDTO);
		assertUserDTO(userDTO, getUserDTO);
	}

	@Test
	public void testGetUserDtoByUserAccount() {
		UserDTO getUserDTO = securityAccessFacade.getUserByUserAccount(user.getUserAccount());
		assertNotNull(getUserDTO);
		assertUserDTO(userDTO, getUserDTO);
	}

	@Test
	public void testFindRoleDtosByUserAccount() {
		List<RoleDTO> roleDTOs = (List<RoleDTO>) securityAccessFacade.findRolesByUserAccount(user.getUserAccount()).getData();
		assertNotNull(roleDTOs);
		assertTrue(roleDTOs.size() == 1);
	}

	@Test
	public void testFindPermissionsByUserAccountAndRoleName() {
		Set<PermissionDTO> results =  securityAccessFacade.findPermissionsByUserAccountAndRoleName(user.getUserAccount(), role.getName());
		assertFalse(results.isEmpty());
		assertTrue(results.size() == 1);
	}

	@Test
	public void testFindMenuResourceDtoByUserAccount() {
		List<MenuResourceDTO> results = securityAccessFacade.findMenuResourceByUserAccount(user.getUserAccount());
		assertFalse(results.isEmpty());
		assertTrue(results.size() == 1);
	}

	@Test
	public void testFindMenuResourceDTOByUserAccountAsRole() {
		List<MenuResourceDTO> results = (List<MenuResourceDTO>) securityAccessFacade.findMenuResourceByUserAsRole(user.getUserAccount(), role.getId()).getData();
		assertFalse(results.isEmpty());
		System.out.println(results);
		assertTrue(results.size() == 1);
	}
	
	@Test
	public void testFindAllMenusTree() {
		List<MenuResourceDTO> results = (List<MenuResourceDTO>) securityAccessFacade.findAllMenusTree().getData();
		assertFalse(results.isEmpty());
		assertTrue(results.size() == 1);
	}

	@Test
	public void testFindMenuResourceTreeSelectItemByRoleId() {
		List<MenuResourceDTO> results  = (List<MenuResourceDTO>) securityAccessFacade.findMenuResourceTreeSelectItemByRoleId(role.getId()).getData();
		assertFalse(results.isEmpty());
		assertTrue(results.size() == 1);
	}


	@Test
	public void testFindPermissionDTOsByMenuOrUrl() {
		Set<PermissionDTO> results = securityAccessFacade.findPermissionsByMenuOrUrl();
		assertNotNull(results);
		assertTrue(results.size() == 2);
	}

	@Test
	public void testFindRoleDTOsByMenuOrUrl() {
		Set<RoleDTO> results = securityAccessFacade.findRolesByMenuOrUrl();
		assertNotNull(results);
		assertTrue(results.size() == 2);
	}

	@Test
	public void testFindAllUrlAccessResources() {
		List<UrlAuthorityDTO> result = securityAccessFacade.findAllUrlAccessResources();
		assertNotNull(result);
		assertTrue(result.size() == 1);
        UrlAuthorityDTO urlAuthority = result.get(0);
		Set<String> roles = urlAuthority.getRoles();
		Set<String> permissions = urlAuthority.getPermissions();
		assertNotNull(roles);
		assertNotNull(permissions);
        assertFalse(roles.isEmpty());
        assertFalse(permissions.isEmpty());
        assertTrue(roles.size() == 1);
        assertTrue(permissions.size() == 1);
        assertTrue(roles.contains(role.getName()));
        assertTrue(permissions.contains(permission.getIdentifier()));
	}

	// 不能用User中的password,因为已经加密了。
	@Test
	public void testLogin() {
		String password = "888888";
		UserDTO getUserDTO = securityAccessFacade.login(user.getUserAccount(), password);
		assertNotNull(getUserDTO);
		assertUserDTO(userDTO, getUserDTO);
	}

	@Test
	public void testPagingQueryUsers() {
		Page<UserDTO> userDTOPages = (Page<UserDTO>) securityAccessFacade.pagingQueryUsers(currentPage, pageSize, userDTO).getData();
		assertFalse(userDTOPages.getData().isEmpty());
		assertTrue(userDTOPages.getResultCount() == 1);
	}

	@Test
	public void testPagingQueryRoles() {
		Page<RoleDTO> roleDTOPages = (Page<RoleDTO>) securityAccessFacade.pagingQueryRoles(currentPage, pageSize, roleDTO).getData();
		assertFalse(roleDTOPages.getData().isEmpty());
		assertTrue(roleDTOPages.getResultCount() == 1);
	}

	@Test
	public void testPagingQueryPermissions() {
		Page<PermissionDTO> permissionDTOPages = (Page<PermissionDTO>) securityAccessFacade.pagingQueryPermissions(currentPage, pageSize, permissionDTO).getData();
		assertFalse(permissionDTOPages.getData().isEmpty());
		assertTrue(permissionDTOPages.getResultCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantRolesConditionIsNull() {
		Role role = new Role("PagingQueryNotGrantRolesIntIntPermissionDTOLong");
		role.save();
		Page<RoleDTO> roleDTOPages = (Page<RoleDTO>) securityAccessFacade.pagingQueryNotGrantRoles(currentPage, pageSize, new RoleDTO(null, ""), user.getId()).getData();
		assertFalse(roleDTOPages.getData().isEmpty());
		assertTrue(roleDTOPages.getPageCount() == 1);
	}
	
	@Test
	public void testPagingQueryNotGrantRoles() {
		Role role = new Role("PagingQueryNotGrantRolesIntIntPermissionDTOLong");
		role.save();
		Page<RoleDTO> roleDTOPages = (Page<RoleDTO>) securityAccessFacade.pagingQueryNotGrantRoles(currentPage, pageSize, generateRoleDTOBy(role), user.getId()).getData();
		assertFalse(roleDTOPages.getData().isEmpty());
		assertTrue(roleDTOPages.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPermissionByUserId() {
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryGrantPermissionByUserId(currentPage, pageSize, user.getId()).getData();
		assertTrue(results.getData().isEmpty());
	}

	@Test
	public void testPagingQueryGrantRolesByUserId() {
		Page<RoleDTO> results = (Page<RoleDTO>) securityAccessFacade.pagingQueryGrantRolesByUserId(currentPage, pageSize, user.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByRoleId() {
		Permission permission = new Permission("testPagingQueryNotGrantPermissionsByRoleId", "testPagingQueryNotGrantPermissionsByRoleId");
		permission.save();
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryNotGrantPermissionsByRoleId(currentPage, pageSize, role.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPermissionsByRoleId() {
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryGrantPermissionsByRoleId(currentPage, pageSize, role.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryUrlAccessResources() {
		Page<UrlAccessResourceDTO> results = (Page<UrlAccessResourceDTO>) securityAccessFacade.pagingQueryUrlAccessResources(currentPage, pageSize, urlAccessResourceDTO).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantUrlAccessResourcesByRoleId() {
		Page<UrlAccessResourceDTO> results = (Page<UrlAccessResourceDTO>) securityAccessFacade.pagingQueryGrantUrlAccessResourcesByRoleId(currentPage, pageSize, role.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantUrlAccessResourcesByRoleId() {
		UrlAccessResource urlAccessResource = new UrlAccessResource("testPagingQueryNotGrantUrlAccessResourcesByRoleId", "testPagingQueryNotGrantUrlAccessResourcesByRoleId");
		urlAccessResource.save();
		Page<UrlAccessResourceDTO> results = (Page<UrlAccessResourceDTO>) securityAccessFacade.pagingQueryNotGrantUrlAccessResourcesByRoleId(currentPage, pageSize, role.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPermissionsByUrlAccessResourceId() {
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryGrantPermissionsByUrlAccessResourceId(currentPage, pageSize, urlAccessResource.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByUrlAccessResourceId() {
		Permission permission = new Permission("testPagingQueryNotGrantPermissionsByUrlAccessResourceId", "testPagingQueryNotGrantPermissionsByUrlAccessResourceId");
		permission.save();
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryNotGrantPermissionsByUrlAccessResourceId(currentPage, pageSize, urlAccessResource.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPermissionsByMenuResourceId() {
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryGrantPermissionsByMenuResourceId(currentPage, pageSize, menuResource.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByMenuResourceId() {
		Permission permission = new Permission("testPagingQueryNotGrantPermissionsByMenuResourceId", "testPagingQueryNotGrantPermissionsByMenuResourceId");
		permission.save();
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryNotGrantPermissionsByMenuResourceId(currentPage, pageSize, menuResource.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByUserId() {
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryNotGrantPermissionsByUserId(currentPage, pageSize, permissionDTO, user.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryPageElementResources() {
		Page<PageElementResourceDTO> results = (Page<PageElementResourceDTO>) securityAccessFacade.pagingQueryPageElementResources(currentPage, pageSize, pageElementResourceDTO).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPageElementResourcesByRoleId() {
		Page<PageElementResourceDTO> results = (Page<PageElementResourceDTO>) securityAccessFacade.pagingQueryGrantPageElementResourcesByRoleId(currentPage, pageSize, role.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPageElementResourcesByRoleId() {
		PageElementResource pageElementResource = new PageElementResource("测试分页查询没有授权的页面元素资源","testPagingQueryNotGrantPageElementResourcesByRoleId");
		pageElementResource.save();
		Page<PageElementResourceDTO> results = (Page<PageElementResourceDTO>) securityAccessFacade.pagingQueryNotGrantPageElementResourcesByRoleId(currentPage, pageSize, role.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPermissionsByPageElementResourceId() {
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryGrantPermissionsByPageElementResourceId(currentPage, pageSize,pageElementResource.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByPageElementResourceId() {
		Permission permission = new Permission("testPagingQueryNotGrantPermissionsByPageElementResourceId", "testPagingQueryNotGrantPermissionsByPageElementResourceId");
		permission.save();
		Page<PermissionDTO> results = (Page<PermissionDTO>) securityAccessFacade.pagingQueryNotGrantPermissionsByPageElementResourceId(currentPage, pageSize,pageElementResource.getId()).getData();
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

}
