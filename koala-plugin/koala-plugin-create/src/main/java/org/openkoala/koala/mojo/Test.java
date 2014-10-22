package org.openkoala.koala.mojo;

import org.openkoala.koala.widget.Example;
import org.openkoala.koala.widget.GeneralQuery;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.Organization;
import org.openkoala.koala.widget.Project;
import org.openkoala.koala.widget.Security;
import org.openkoala.koala.widget.SecurityOrg;

public class Test {

	public static void main(String[] args) throws Exception {
		Project pro = new Project();
		pro.setAppName("test");
		pro.setArtifactId("ttt");
		pro.setPath("C:/Users/Administrator/Desktop/ran");
		KoalaProjectCreate kpc = new KoalaProjectCreate();
		pro.initSSJProject();
		//Organization organization =	new Organization();
		//organization.setOrganizationGenerateSourceCode(true);
		//GeneralQuery gq = new GeneralQuery();
		/*SecurityOrg s = new SecurityOrg();
		for(Module module : pro.getModule()){
			System.out.println(module.getModuleType());
			if(module.getModuleType()=="war"){
				//module.setOrganization(organization);
				module.setSecurityOrg(s);
			}
		}*/Example e = new Example();
		e.setExampleGenerateSourceCode(true);
		pro.setExample(e);
		kpc.createProject(pro);
	}

}
