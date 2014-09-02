package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SvnConfig;

/**
 * svn创建用户验证密码类
 */
public class SvnClearProjectPasswdFileContentCommand extends SvnCommand {

	public SvnClearProjectPasswdFileContentCommand() {
		
	}
	
	public SvnClearProjectPasswdFileContentCommand(SvnConfig config, Project project) {
		super(config, project);
	}

	@Override
	public String getCommand() {
		String strCommand = "touch " + storePath + "passwd"; 
		return strCommand;
	}
}
