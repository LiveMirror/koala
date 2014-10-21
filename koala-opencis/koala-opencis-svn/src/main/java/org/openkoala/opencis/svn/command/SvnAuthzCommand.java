package org.openkoala.opencis.svn.command;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.SvnConfig;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn分配某个角色到该项目下并拥有rw操作权限
 */
public class SvnAuthzCommand extends SvnCommand {

    private String role;

    public SvnAuthzCommand() {

    }

    public SvnAuthzCommand(String role, SvnConfig configuration, Project project) {
        super(configuration, project);
        this.role = role;
    }

    // TODO SVN工作路径问题
    @Override
    public String getCommand() {
        String groupName = project.getProjectName() + "_" + role;
        StringBuilder authzCommand = new StringBuilder();
        authzCommand.append("grep -q '\\[")
                .append(project.getProjectName()).append(":/\\]' ")
                .append(storePath).append("authz ")
                .append("&& sed -i '/\\[").append(project.getProjectName()).append(":\\/\\]/a ")
                .append("@").append(groupName).append("=rw' ")
                .append(storePath).append("authz ")
                .append("|| echo -ne '\n[").append(project.getProjectName()).append(":/]\n@")
                .append(groupName).append("=rw\n' >> ")
                .append(storePath).append("/authz");
        System.out.println("authzCommand = " + authzCommand.toString());
        return authzCommand.toString();
    }

}
