package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SvnConfig;

public class SvnAuthUserToProjectCommand extends SvnCommand {
	
	/**
	 * @property
	 */
	private String userName;
	
	public SvnAuthUserToProjectCommand(String userName,SvnConfig svnConfig,Project project) {
		// TODO Auto-generated constructor stub
		super(svnConfig, project);
		this.userName = userName;
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
        StringBuilder authzCommand = new StringBuilder();
        authzCommand.append("grep -q '\\[")
                .append(project.getProjectName()).append(":/\\]' ")
                .append(storePath).append("authz ")
                .append("&& sed -i '/\\[").append(project.getProjectName()).append(":\\/\\]/a ")
                .append(userName).append("=rw\n' ").append(storePath).append("/authz")
                .append("|| echo -ne '\n[").append(project.getProjectName()).append(":/]\n")
                .append(userName).append("=rw\n' >> ")
                .append(storePath).append("/authz");
        return authzCommand.toString();
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
