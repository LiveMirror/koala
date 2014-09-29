package org.openkoala.security.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.openkoala.security.facade.impl.util.EntitiesHelper.assertUserDTO;
import static org.openkoala.security.facade.impl.util.EntitiesHelper.initMenuResource;
import static org.openkoala.security.facade.impl.util.EntitiesHelper.initPageElementResource;
import static org.openkoala.security.facade.impl.util.EntitiesHelper.initPermission;
import static org.openkoala.security.facade.impl.util.EntitiesHelper.initRole;
import static org.openkoala.security.facade.impl.util.EntitiesHelper.initUrlAccessResource;
import static org.openkoala.security.facade.impl.util.EntitiesHelper.initUser;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.dayatang.utils.Page;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.openkoala.security.facade.dto.UrlAuthorityDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.openkoala.security.facade.impl.assembler.PageElementResourceAssembler;
import org.openkoala.security.facade.impl.assembler.PermissionAssembler;
import org.openkoala.security.facade.impl.assembler.RoleAssembler;
import org.openkoala.security.facade.impl.assembler.UrlAccessResourceAssembler;
import org.openkoala.security.facade.impl.assembler.UserAssembler;

/**
 * 完善测试 对其测试结果进行断言。
 */
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
		userDTO = UserAssembler.toUserDTO(user);
		
		role = initRole();
		role.save();
		roleDTO = RoleAssembler.toRoleDTO(role);
		
		user.grant(role);
		
		permission = initPermission();
		permission.save();
		permissionDTO = PermissionAssembler.toPermissionDTO(permission);
		
		menuResource = initMenuResource();
		menuResource.save();
		
		role.addSecurityResource(menuResource);
		permission.addSecurityResource(menuResource);
		
		urlAccessResource = initUrlAccessResource();
		urlAccessResource.save();
		urlAccessResourceDTO = UrlAccessResourceAssembler.toUrlAccessResourceDTO(urlAccessResource);
		
		pageElementResource = initPageElementResource();
		pageElementResource.save();
		pageElementResourceDTO = PageElementResourceAssembler.toPageElementResourceDTO(pageElementResource);
		
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
		List<MenuResourceDTO> results = (List<MenuResourceDTO>) securityAccessFacade.findMenuResourceByUserAsRole(user.getUserAccount(), role.getName()).getData();
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

	@Test
	public void testPagingQueryUsers() {
		Page<UserDTO> userDTOPages = securityAccessFacade.pagingQueryUsers(currentPage, pageSize, userDTO);
		assertFalse(userDTOPages.getData().isEmpty());
		assertTrue(userDTOPages.getResultCount() == 1);
	}

	@Test
	public void testPagingQueryRoles() {
		Page<RoleDTO> roleDTOPages = securityAccessFacade.pagingQueryRoles(currentPage, pageSize, roleDTO);
		assertFalse(roleDTOPages.getData().isEmpty());
		assertTrue(roleDTOPages.getResultCount() == 1);
	}

	@Test
	public void testPagingQueryPermissions() {
		Page<PermissionDTO> permissionDTOPages =  securityAccessFacade.pagingQueryPermissions(currentPage, pageSize, permissionDTO);
		assertFalse(permissionDTOPages.getData().isEmpty());
		assertTrue(permissionDTOPages.getResultCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantRolesConditionIsNull() {
		Role role = new Role("PagingQueryNotGrantRolesIntIntPermissionDTOLong");
		role.save();
		Page<RoleDTO> roleDTOPages =  securityAccessFacade.pagingQueryNotGrantRoles(currentPage, pageSize, new RoleDTO(null, ""), user.getId());
		assertFalse(roleDTOPages.getData().isEmpty());
		assertTrue(roleDTOPages.getPageCount() == 1);
	}
	
	@Test
	public void testPagingQueryNotGrantRoles() {
		Role role = new Role("PagingQueryNotGrantRolesIntIntPermissionDTOLong");
		role.save();
		Page<RoleDTO> roleDTOPages =  securityAccessFacade.pagingQueryNotGrantRoles(currentPage, pageSize, RoleAssembler.toRoleDTO(role), user.getId());
		assertFalse(roleDTOPages.getData().isEmpty());
		assertTrue(roleDTOPages.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPermissionByUserId() {
		Page<PermissionDTO> results =  securityAccessFacade.pagingQueryGrantPermissionByUserId(currentPage, pageSize, user.getId());
		assertTrue(results.getData().isEmpty());
	}

	@Test
	public void testPagingQueryGrantRolesByUserId() {
		Page<RoleDTO> results =  securityAccessFacade.pagingQueryGrantRolesByUserId(currentPage, pageSize, user.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByRoleId() {
		Permission permission = new Permission("testPagingQueryNotGrantPermissionsByRoleId", "testPagingQueryNotGrantPermissionsByRoleId");
		permission.save();
		Page<PermissionDTO> results =  securityAccessFacade.pagingQueryNotGrantPermissionsByRoleId(currentPage, pageSize, role.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPermissionsByRoleId() {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByRoleId(currentPage, pageSize, role.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryUrlAccessResources() {
		Page<UrlAccessResourceDTO> results =  securityAccessFacade.pagingQueryUrlAccessResources(currentPage, pageSize, urlAccessResourceDTO);
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantUrlAccessResourcesByRoleId() {
		Page<UrlAccessResourceDTO> results = securityAccessFacade.pagingQueryGrantUrlAccessResourcesByRoleId(currentPage, pageSize, role.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantUrlAccessResourcesByRoleId() {
		UrlAccessResource urlAccessResource = new UrlAccessResource("testPagingQueryNotGrantUrlAccessResourcesByRoleId", "testPagingQueryNotGrantUrlAccessResourcesByRoleId");
		urlAccessResource.save();
		Page<UrlAccessResourceDTO> results =  securityAccessFacade.pagingQueryNotGrantUrlAccessResourcesByRoleId(currentPage, pageSize, role.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

    @Test
	public void testPagingQueryNotGrantUrlAccessResourcesByRoleIdAndQueryCondition() {
		UrlAccessResource urlAccessResource = new UrlAccessResource("testPagingQueryNotGrantUrlAccessResourcesByRoleId", "testPagingQueryNotGrantUrlAccessResourcesByRoleId");
		urlAccessResource.save();
        UrlAccessResourceDTO queryUrlAccessResourceCondition = new UrlAccessResourceDTO("test");
        queryUrlAccessResourceCondition.setName("测试");
        queryUrlAccessResourceCondition.setDescription("测试");
		Page<UrlAccessResourceDTO> results =  securityAccessFacade.pagingQueryNotGrantUrlAccessResourcesByRoleId(currentPage, pageSize, role.getId(),queryUrlAccessResourceCondition);
		assertTrue(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 0);
	}

	@Test
	public void testPagingQueryGrantPermissionsByUrlAccessResourceId() {
		Page<PermissionDTO> results =  securityAccessFacade.pagingQueryGrantPermissionsByUrlAccessResourceId(currentPage, pageSize, urlAccessResource.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByUrlAccessResourceId() {
		Permission permission = new Permission("testPagingQueryNotGrantPermissionsByUrlAccessResourceId", "testPagingQueryNotGrantPermissionsByUrlAccessResourceId");
		permission.save();
		Page<PermissionDTO> results =  securityAccessFacade.pagingQueryNotGrantPermissionsByUrlAccessResourceId(currentPage, pageSize, urlAccessResource.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPermissionsByMenuResourceId() {
		Page<PermissionDTO> results =  securityAccessFacade.pagingQueryGrantPermissionsByMenuResourceId(currentPage, pageSize, menuResource.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByMenuResourceId() {
		Permission permission = new Permission("testPagingQueryNotGrantPermissionsByMenuResourceId", "testPagingQueryNotGrantPermissionsByMenuResourceId");
		permission.save();
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByMenuResourceId(currentPage, pageSize, menuResource.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByUserId() {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByUserId(currentPage, pageSize, permissionDTO, user.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryPageElementResources() {
		Page<PageElementResourceDTO> results = securityAccessFacade.pagingQueryPageElementResources(currentPage, pageSize, pageElementResourceDTO);
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPageElementResourcesByRoleId() {
		Page<PageElementResourceDTO> results = securityAccessFacade.pagingQueryGrantPageElementResourcesByRoleId(currentPage, pageSize, role.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryNotGrantPageElementResourcesByRoleId() {
		PageElementResource pageElementResource = new PageElementResource("测试分页查询没有授权的页面元素资源","testPagingQueryNotGrantPageElementResourcesByRoleId");
		pageElementResource.save();
		Page<PageElementResourceDTO> results = securityAccessFacade.pagingQueryNotGrantPageElementResourcesByRoleId(currentPage, pageSize, role.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

	@Test
	public void testPagingQueryGrantPermissionsByPageElementResourceId() {
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryGrantPermissionsByPageElementResourceId(currentPage, pageSize,pageElementResource.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
        PermissionDTO actualPermission = results.getData().get(0);
        assertNotNull(actualPermission);
        assertNotNull(actualPermission.getId());
        assertEquals(permission.getId(),actualPermission.getId());
        assertEquals(permission.getIdentifier(),actualPermission.getIdentifier());
        assertEquals(permission.getName(),actualPermission.getName());
        assertEquals(permission.getDescription(),actualPermission.getDescription());
	}

	@Test
	public void testPagingQueryNotGrantPermissionsByPageElementResourceId() {
		Permission permission = new Permission("testPagingQueryNotGrantPermissionsByPageElementResourceId", "testPagingQueryNotGrantPermissionsByPageElementResourceId");
		permission.save();
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByPageElementResourceId(currentPage, pageSize,pageElementResource.getId());
		assertFalse(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 1);
	}

    @Test
	public void testPagingQueryNotGrantPermissionsByPageElementResourceIdAndQueryCondition() {
		Permission permission = new Permission("testPagingQueryNotGrantPermissionsByPageElementResourceId", "testPagingQueryNotGrantPermissionsByPageElementResourceId");
		permission.save();
        PermissionDTO queryPermissionCondition = new PermissionDTO(1L,"aa","Aa","Aa");
		Page<PermissionDTO> results = securityAccessFacade.pagingQueryNotGrantPermissionsByPageElementResourceId(currentPage, pageSize,pageElementResource.getId(),queryPermissionCondition);
		assertTrue(results.getData().isEmpty());
		assertTrue(results.getPageCount() == 0);
	}

    @Test
    public void testPagingQueryRolesOfUser(){
        Page<RoleDTO> results = securityAccessFacade.pagingQueryRolesOfUser(0, 10, user.getUserAccount());
        assertFalse(results.getData().isEmpty());
        assertTrue(results.getPageCount() == 1);
    }

}
