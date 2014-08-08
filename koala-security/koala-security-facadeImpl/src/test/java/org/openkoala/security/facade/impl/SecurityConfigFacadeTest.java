package org.openkoala.security.facade.impl;

import static org.openkoala.security.facade.assembler.GenerateDTOUtils.*;

import java.util.Set;

import javax.inject.Inject;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.SecurityResource;
import org.openkoala.security.facade.SecurityConfigFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;

import com.google.common.collect.Lists;

@Ignore
public class SecurityConfigFacadeTest extends AbstractFacadeIntegrationTestCase {

	@Inject
	private SecurityConfigFacade securityConfigFacade;
	
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

		MenuResourceDTO menuResourceDTO2 = generateMenuResourceDTOBy(menuResource2);
		MenuResourceDTO menuResourceDTO3 = generateMenuResourceDTOBy(menuResource3);
		MenuResourceDTO menuResourceDTO4 = generateMenuResourceDTOBy(menuResource4);

		Role role1 = new Role("role1");
		role1.save();

		role1.addSecurityResources(Lists.newArrayList(menuResource1, menuResource2));

		securityConfigFacade.grantMenuResourcesToRole(role1.getId(), Lists.newArrayList(menuResourceDTO2, menuResourceDTO3, menuResourceDTO4));
		
		Set<SecurityResource> securityResources = role1.getSecurityResources();
		
		assertFalse(securityResources.isEmpty());
		assertTrue(securityResources.size() == 3);

	}

}
