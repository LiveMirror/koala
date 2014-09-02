package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.LocalCommand;
import org.openkoala.opencis.support.SvnConfig;

/**
 * SVN本地添加项目文件到版本库
 * @author zjh
 *
 */
public class SvnLocalAddCommand extends LocalCommand {

	public SvnLocalAddCommand() {
		// TODO Auto-generated constructor stub
	}

	public SvnLocalAddCommand(SvnConfig config, Project project,Developer developer) {
		super(config, project);
//		this.svnUser = developer.getId();
//		this.svnPassword = developer.getPassword();
		this.userName = developer.getId();
		this.password = developer.getPassword();
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String strCmd = "svn add " + project.getPhysicalPath() + "/*"
				+ " --username " + userName + " --password " + password;
		return strCmd;
	}

}
