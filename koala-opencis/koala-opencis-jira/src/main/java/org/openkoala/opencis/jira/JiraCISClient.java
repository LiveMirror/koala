package org.openkoala.opencis.jira;

import com.atlassian.jira.rpc.soap.client.*;
import com.atlassian.jira.rpc.soap.client.RemoteException;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import javax.xml.rpc.ServiceException;

/**
 * 目前只支持jira 6及以下版本
 */
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

        if (!jiraService.isUserExist(project.getProjectLead())) {
            throw new CISClientBaseRuntimeException("jira.projectLeadNotExists");
        }

        if (jiraService.isProjectExist(project)) {
            return;
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
        if (isDeveloperExist(developer)) {
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
        if (!jiraService.isUserExist(developer.getId())) {
            return;
        }

        try {
            jiraService.deleteUser(developer.getId());
        } catch (RemotePermissionException e) {
            throw new CISClientBaseRuntimeException("jira.remotePermissionException");
        } catch (Exception e) {
            throw new CISClientBaseRuntimeException("jira.remoteException");
        }
    }

    @Override
    public void createRoleIfNecessary(Project project, String roleName) {
        assert roleName != null && !"".equals(roleName.trim());
        //角色存在，则不创建
        if (jiraService.isRoleExist(roleName)) {
            return;
        }
        try {
            jiraService.createRole(roleName);
        } catch (java.rmi.RemoteException e) {
            e.printStackTrace();
            throw new CISClientBaseRuntimeException("createRoleIfNecessary failure", e);
        }
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
            // Invalid username or password
            return false;
        } catch (RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.authenticationRemoteException", e);
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.authenticationRemoteException", e);
        }
        return token != null;
    }


    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        for (Developer each : developers) {
            checkProjectRoleUserAllExist(project, each.getId(), role);
            try {
                jiraService.addActorsToProjectRole(KoalaJiraService.getProjectKey(project), each.getId(), role);
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
    private void checkProjectRoleUserAllExist(Project project, String userName, String roleName) {
        if (!jiraService.isProjectExist(project)) {
            throw new CISClientBaseRuntimeException("jira.projectNotExists");
        }
        if (!jiraService.isUserExist(userName)) {
            throw new CISClientBaseRuntimeException("jira.userNotExists");
        }
    }


    public boolean isRoleExist(String roleName) {
        return jiraService.isRoleExist(roleName);
    }


    /**
     * 仅供单元测试调用
     */
    public void removeProject(Project project) {
        try {
            if (!jiraService.isProjectExist(project)) {
                return;
            }
            jiraService.deleteProject(project);
        } catch (RemotePermissionException e) {
            throw new CISClientBaseRuntimeException("jira.remotePermissionException");
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.remoteException");
        }
    }


    public boolean isDeveloperExist(Developer developer) {
        return jiraService.isUserExist(developer.getId());
    }


}
