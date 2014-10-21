package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.LocalCommand;
import org.openkoala.opencis.support.SvnConfig;

/**
 * SVN本地提交到SVN服务器命令
 * @author zjh
 *
 */
public class SvnLocalCommitCommand extends LocalCommand {

	public SvnLocalCommitCommand() {
		// TODO Auto-generated constructor stub
	}

	public SvnLocalCommitCommand(SvnConfig config, Project project,Developer developer) {
		super(config, project);
//		this.svnUser = developer.getId();
//		this.svnPassword = developer.getPassword();
		this.userName = developer.getId();
		this.password = developer.getPassword();
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		String strCmd = "svn commit " + project.getPhysicalPath() + "/*" 
				+ " --username " + userName + " --password " + password
				+ " -m \"" + "import project " + project.getProjectName() + "\" --non-interactive";
		return strCmd;
	}

}
