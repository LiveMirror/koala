package org.openkoala.opencis.jira;

import com.atlassian.jira.rpc.soap.client.*;
import com.atlassian.jira.rpc.soap.client.RemoteException;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import javax.xml.rpc.ServiceException;

public class JiraCISClient implements CISClient {


    /**
     * jira soap服务接口
     */
    private KoalaJiraService jiraService;

    private String jiraServerAddress;

    private String adminUserName;

    private String adminPassword;

    public JiraCISClient(String jiraServerAddress, String adminUserName, String adminPassword) {
        this.jiraServerAddress = jiraServerAddress;
        this.adminUserName = adminUserName;
        this.adminPassword = adminPassword;
    }

    @Override
    public void close() {
        // do nothing
    }


    @Override
    public void createProject(Project project) {
        project.validate();

        if (jiraService.isProjectExist(project)) {
            return;
        }

        if (!jiraService.isUserExist(project.getProjectLead())) {
            throw new CISClientBaseRuntimeException("jira.projectLeadNotExists");
        }

        try {
            jiraService.createProject(project, jiraServerAddress);
        } catch (RemotePermissionException e) {
            throw new CISClientBaseRuntimeException("jira createProject permission denied", e);
        } catch (RemoteValidationException e) {
            throw new CISClientBaseRuntimeException("jira RemoteValidationException", e);
        } catch (RemoteAuthenticationException e) {
            throw new CISClientBaseRuntimeException("jira RemoteAuthenticationException", e);
        } catch (RemoteException e) {
            throw new CISClientBaseRuntimeException("jira RemoteException", e);
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("jira RemoteException", e);
        }
    }

    public boolean isProjectExist(Project project) {
        return jiraService.isProjectExist(project);
    }


    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        developer.validate();
        //用户存在，则不创建，忽略
        if (jiraService.isUserExist(developer)) {
            return;
        }
        try {
            jiraService.createUser(developer);
        } catch (RemotePermissionException e) {
            throw new CISClientBaseRuntimeException("jira.remotePermissionException");
        } catch (RemoteValidationException e) {
            throw new CISClientBaseRuntimeException("jira.remoteValidationException");
        } catch (RemoteAuthenticationException e) {
            throw new CISClientBaseRuntimeException("jira.remoteAuthenticationException");
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.remoteException");
        }
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        try {
            jiraService.deleteUser(developer.getName());
        } catch (RemotePermissionException e) {
            throw new CISClientBaseRuntimeException("jira.remotePermissionException");
        } catch (Exception e) {
            throw new CISClientBaseRuntimeException("jira.remoteException");
        }
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
        //角色存在，则不创建
        if (jiraService.roleExists(roleName)) {
            return;
        }
        createRole(roleName);
    }


    @Override
    public boolean authenticate() {
        JiraSoapServiceServiceLocator jiraSoapServiceLocator = new JiraSoapServiceServiceLocator();
        jiraSoapServiceLocator.setJirasoapserviceV2EndpointAddress(jiraServerAddress
                + "/rpc/soap/jirasoapservice-v2?wsdl");
        String token = null;
        try {
            JiraSoapService jiraSoapService = jiraSoapServiceLocator.getJirasoapserviceV2();
            token = jiraSoapService.login(adminUserName, adminPassword);
            jiraService = new KoalaJiraService(token, jiraSoapService);
        } catch (ServiceException e) {
            throw new CISClientBaseRuntimeException("jira.authenticationServiceException", e);
        } catch (RemoteAuthenticationException e) {
            throw new CISClientBaseRuntimeException("jira.authenticationRemoteAuthenticationException", e);
        } catch (RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.authenticationRemoteException", e);
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.authenticationRemoteException", e);
        }
        return token != null;
    }


    /**
     * 创建角色到JIRA
     */
    private boolean createRole(String roleName) {
        try {
            jiraService.createRole(roleName);
        } catch (java.rmi.RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        for (Developer each : developers) {
            checkProjectRoleUserAllExist(project, each.getName(), role);
            try {
                jiraService.addActorsToProjectRole(project.getProjectName(), each.getName(), role);
            } catch (java.rmi.RemoteException e) {
                throw new CISClientBaseRuntimeException("jira.assignUsersToRoleRemoteException", e);
            }
        }

    }

    /**
     * 检查项目、用户、角色是否都存在
     *
     * @return
     */
    private boolean checkProjectRoleUserAllExist(Project project, String userName, String roleName) {
        if (!jiraService.isProjectExist(project)) {
            throw new CISClientBaseRuntimeException("jira.projectNotExists");
        }
        if (!jiraService.isUserExist(userName)) {
            throw new CISClientBaseRuntimeException("jira.userNotExists");
        }
        if (!jiraService.roleExists(roleName)) {
            throw new CISClientBaseRuntimeException("jira.roleNotExists");
        }
        return true;
    }


    /**
     * 仅供单元测试调用
     */
    public void removeProject(Project project) {
        try {
            jiraService.deleteProject(project.getProjectName());
        } catch (RemotePermissionException e) {
            throw new CISClientBaseRuntimeException("jira.remotePermissionException");
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.remoteException");
        }
    }


}
