package org.openkoala.organisation.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.dayatang.domain.InstanceFactory;
import org.openkoala.organisation.facade.OrganizationFacade;
import org.openkoala.organisation.facade.dto.OrganizationDTO;

public class OrganisationListener implements ServletContextListener {

private OrganizationFacade organizationFacade;
	
	private OrganizationFacade getOrganizationFacade(){
		if(organizationFacade==null){
			organizationFacade = InstanceFactory.getInstance(OrganizationFacade.class);
		}
		return organizationFacade;
	}
	 
	public void contextInitialized(ServletContextEvent event) {
		initTopOrganizationIfNecessary(event);
	}
	
	private void initTopOrganizationIfNecessary(ServletContextEvent event) {
		if (!getOrganizationFacade().isTopOrganizationExists()) {
			getOrganizationFacade().createAsTopOrganization(newTopOrganization());
		}
	}
	
	private OrganizationDTO newTopOrganization() {
		OrganizationDTO organizationDTO = new OrganizationDTO("总公司", "COM-001","总公司：所有机构的根");
		organizationDTO.setOrganizationType(OrganizationDTO.COMPANY);
		return organizationDTO;
	}

	public void contextDestroyed(ServletContextEvent sce) {

	}

}
