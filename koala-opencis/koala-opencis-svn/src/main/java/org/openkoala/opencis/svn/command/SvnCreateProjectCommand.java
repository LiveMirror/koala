package org.openkoala.opencis.svn.command;


import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.ProjectExistenceException;
import org.openkoala.opencis.support.OpencisConstant;
import org.openkoala.opencis.support.SSHConnectConfig;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn创建项目命令类
 */
public class SvnCreateProjectCommand extends SvnCommand {

	public SvnCreateProjectCommand() {
		
	}
	
	public SvnCreateProjectCommand(SSHConnectConfig configuration, Project project) {
		super(configuration, project);
	}

	@Override
	public String getCommand() {
		String createProjectCommand = "svnadmin create " + storePath + project.getProjectName();
		System.out.println("SVN 远程创建项目命令：" + createProjectCommand);
		return createProjectCommand;
	}
	
	@Override
	public void doWork(Connection connection, Session session) {
		try {
			String stderr = readOutput(session.getStderr());
			if( !StringUtils.isBlank(stderr) ){
				throw new ProjectExistenceException("项目已经存在！");
			}
		} catch (IOException e) {
			throw new RuntimeException("执行创建项目" + storePath + "命令，返回结果时异常！");
		}
	}
	
}
