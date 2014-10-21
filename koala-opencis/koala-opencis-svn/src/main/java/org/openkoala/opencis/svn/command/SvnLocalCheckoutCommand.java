package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.LocalCommand;
import org.openkoala.opencis.support.SvnConfig;

/**
 * SVN本地Checkout
 * @author zjh
 *
 */
public class SvnLocalCheckoutCommand extends LocalCommand {

	public SvnLocalCheckoutCommand() {
		// TODO Auto-generated constructor stub
	}

	public SvnLocalCheckoutCommand(SvnConfig config, Project project) {
		super(config, project);
		// TODO Auto-generated constructor stub
	}
	
	public SvnLocalCheckoutCommand(SvnConfig config, Project project,Developer developer) {
		this(config, project);
//		this.svnUser = developer.getId();
//		this.svnPassword = developer.getPassword();
		this.userName = developer.getId();
		this.password = developer.getPassword();
	}


	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		// http://10.108.1.138/svn/ddd    test/test
		String strCmd = "svn checkout " + svnAddress 
				+ "/" + project.getProjectName()
				+ " " + project.getPhysicalPath() 
				+ " --username " + userName + " --password " + password + " --non-interactive";
		return strCmd;
	}

}
