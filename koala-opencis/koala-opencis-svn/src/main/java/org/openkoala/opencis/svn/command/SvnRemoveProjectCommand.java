package org.openkoala.opencis.svn.command;

import java.io.IOException;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.exception.RemoveProjectException;
import org.openkoala.opencis.support.SvnConfig;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn创建项目命令类
 */
public class SvnRemoveProjectCommand extends SvnCommand {

    public SvnRemoveProjectCommand() {

    }

    public SvnRemoveProjectCommand(SvnConfig configuration, Project project) {
        super(configuration, project);
    }

    @Override
    public String getCommand() {
        String removeProjectCommand = "rm -rf " + storePath + project.getProjectName();
        return removeProjectCommand;
    }

    @Override
    public void doWork(Connection connection, Session session) {
    	try {
			String stderr = readOutput(session.getStderr());
			if( !"".equals(stderr)){
				throw new RemoveProjectException("删除项目失败！");
			}
		} catch (IOException e) {
			throw new RuntimeException("删除项目" + project.getPhysicalPath() + project.getProjectName()
					+ "发生异常，原因：" + e.getMessage(),e);
		}
    }
}
