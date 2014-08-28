package org.openkoala.koala.mojo;

import org.openkoala.koala.widget.GeneralQuery;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.Organization;
import org.openkoala.koala.widget.Project;

public class Test {

	public static void main(String[] args) throws Exception {
		Project pro = new Project();
		pro.setAppName("test");
		pro.setArtifactId("ttt");
		pro.setPath("C:/Users/Administrator/Desktop/android");
		KoalaProjectCreate kpc = new KoalaProjectCreate();
		pro.initSSJProject();
	Organization organization =	new Organization();
		//organization.setOrganizationGenerateSourceCode(true);
		GeneralQuery gq = new GeneralQuery();
		for(Module module : pro.getModule()){
			if(module.getModuleType()=="war"){
				//module.setOrganization(organization);
				module.setGeneralQuery(gq);
			}
		}
		kpc.createProject(pro);
		
		
	}

}
