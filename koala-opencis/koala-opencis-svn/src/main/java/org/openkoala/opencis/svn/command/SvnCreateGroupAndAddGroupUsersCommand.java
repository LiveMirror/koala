package org.openkoala.opencis.svn.command;

import java.util.List;

import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.support.CommonUtil;
import org.openkoala.opencis.support.SvnConfig;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * svn分配用户到某个角色
 */
public class SvnCreateGroupAndAddGroupUsersCommand extends SvnCommand {

    private List<String> userNames;

    private String role;

    public SvnCreateGroupAndAddGroupUsersCommand() {

    }

    public SvnCreateGroupAndAddGroupUsersCommand(List<String> userNames, String role, SvnConfig config, Project project) {
        super(config, project);
        this.userNames = userNames;
        this.role = role;
    }

    @Override
    public String getCommand() {
        String groupName = project.getProjectName() + "_" + role;
        String groupUsers = CommonUtil.ConvertGroupUserListToString(userNames);
        StringBuilder assignUserToRoleCommand = new StringBuilder();
        assignUserToRoleCommand.append("grep -q '^\\[groups\\]' ")
                .append(storePath).append("authz ")
                .append("&& sed -i '/\\[groups\\]/a ").append(groupName).append("=").append(groupUsers).append("' ")
                .append(storePath).append("authz ")
                .append("|| echo -ne '\n[groups]\n").append(groupName).append("=").append(groupUsers).append("' >>  ")
                .append(storePath).append("authz ");
        System.out.println("assignUserToRoleCommand = " + assignUserToRoleCommand.toString());
        return assignUserToRoleCommand.toString();
    }

}
