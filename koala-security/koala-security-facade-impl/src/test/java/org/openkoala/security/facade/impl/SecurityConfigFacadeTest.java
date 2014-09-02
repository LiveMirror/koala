package org.openkoala.security.facade.impl;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.command.CreateUserCommand;
import org.openkoala.security.facade.dto.MenuResourceDTO;

import static org.openkoala.security.facade.impl.util.CommandHelper.*;

import com.google.common.collect.Lists;
import org.openkoala.security.facade.impl.assembler.MenuResourceAssembler;

public class SecurityConfigFacadeTest extends AbstractFacadeIntegrationTestCase {

	@Inject
	private SecurityConfigFacade securityConfigFacade;

	/*private JsonResult initJsonResult() {
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		result.setMessage("添加用户成功。");
		return result;
	}

	@Test
	public void testCreateUser() throws Exception {
		CreateUserCommand command = initCreateUserCommand();
		InvokeResult actual = securityConfigFacade.createUser(command);
		assertNotNull(actual);
	}

	private void assertResult(JsonResult expected, JsonResult actual) {
		assertEquals(expected.getMessage(), actual.getMessage());
		assertEquals(expected.isSuccess(), actual.isSuccess());
	}*/

	@Test
	public void testGrantMenuResourcesToRole() {
		MenuResource menuResource1 = new MenuResource("menu1");
		MenuResource menuResource2 = new MenuResource("menu2");
		MenuResource menuResource3 = new MenuResource("menu3");
		MenuResource menuResource4 = new MenuResource("menu4");
		menuResource1.save();
		menuResource2.save();
		menuResource3.save();
		menuResource4.save();

		MenuResourceDTO menuResourceDTO2 = MenuResourceAssembler.toMenuResourceDTO(menuResource2);
		MenuResourceDTO menuResourceDTO3 = MenuResourceAssembler.toMenuResourceDTO(menuResource3);
		MenuResourceDTO menuResourceDTO4 = MenuResourceAssembler.toMenuResourceDTO(menuResource4);

		Role role1 = new Role("role1");
		role1.save();

		role1.addSecurityResources(Lists.newArrayList(menuResource1, menuResource2));

		securityConfigFacade.grantMenuResourcesToRole(role1.getId(),new Long[]{menuResourceDTO2.getId(), menuResourceDTO3.getId(), menuResourceDTO4.getId()});

		List<MenuResource> securityResources = role1.findMenuResourceByAuthority();

		assertFalse(securityResources.isEmpty());
		assertTrue(securityResources.size() == 3);

	}

}
