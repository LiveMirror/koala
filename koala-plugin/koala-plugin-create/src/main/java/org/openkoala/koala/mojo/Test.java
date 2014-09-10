package org.openkoala.koala.mojo;

import org.openkoala.koala.widget.Example;
import org.openkoala.koala.widget.GeneralQuery;
import org.openkoala.koala.widget.Module;
import org.openkoala.koala.widget.Organization;
import org.openkoala.koala.widget.Project;

public class Test {

	public static void main(String[] args) throws Exception {
		Project pro = new Project();
		pro.setAppName("test");
		pro.setArtifactId("ttt");
		pro.setPath("C:/Users/Administrator/Desktop/ran");
		KoalaProjectCreate kpc = new KoalaProjectCreate();
		pro.initSSJProject();
		Example e = new Example();
		pro.setExample(e);
		kpc.createProject(pro);
	}

}
