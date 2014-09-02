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
            throw new CISClientBaseRuntimeException(e.getMessage(), e);
        } catch (RemoteValidationException e) {
            throw new CISClientBaseRuntimeException(e.getMessage(), e);
        } catch (RemoteAuthenticationException e) {
            throw new CISClientBaseRuntimeException(e.getMessage(), e);
        } catch (RemoteException e) {
            throw new CISClientBaseRuntimeException(e.getMessage(), e);
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException(e.getMessage(), e);
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
            throw new CISClientBaseRuntimeException("jira.remotePermissionException", e);
        } catch (RemoteValidationException e) {
            throw new CISClientBaseRuntimeException("jira.remoteValidationException", e);
        } catch (RemoteAuthenticationException e) {
            throw new CISClientBaseRuntimeException("jira.remoteAuthenticationException", e);
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("jira.remoteException", e);
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
            throw new CISClientBaseRuntimeException("jira.remotePermissionException", e);
        } catch (Exception e) {
            throw new CISClientBaseRuntimeException("jira.remoteException", e);
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
            return false;
        } catch (java.rmi.RemoteException e) {
            return false;
        }
        return token != null;
    }

    public boolean isUserAtProjectDevelopRole(Project project, Developer developer) {
        try {
            return jiraService.isUserAtProjectDevelopRole(KoalaJiraService.getProjectKey(project), developer.getId());
        } catch (java.rmi.RemoteException e) {
            throw new CISClientBaseRuntimeException("isUserAtProjectDevelopRole", e);
        }

    }


    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        for (Developer each : developers) {
            checkProjectRoleUserAllExist(project, each.getId());
            try {
                if (jiraService.isUserAtProjectDevelopRole(project, each)) continue;

                jiraService.addActorsToProjectRole(KoalaJiraService.getProjectKey(project),
                        each.getId(),
                        KoalaJiraService.DEFAULT_PROJECT_ROLE_DEVELOP);
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
    private void checkProjectRoleUserAllExist(Project project, String userName) {
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
