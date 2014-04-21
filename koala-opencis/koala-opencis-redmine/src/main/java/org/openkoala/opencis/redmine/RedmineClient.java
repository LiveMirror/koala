package org.openkoala.opencis.redmine;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.Role;
import com.taskadapter.redmineapi.bean.User;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjzhai on 3/18/14.
 */
public class RedmineClient implements CISClient {

    /**
     * 开发者角色
     */
    public final static String DEVELOPER_ROLE = "developer";

    /**
     * 项目管理角色
     */
    public final static String PROJECT_MANAGER_ROLE = "pm";

    private RedmineManager manager;


    public RedmineClient(String url, String username, String password) {
        manager = new RedmineManager(url, username, password);
    }

    @Override
    public void createProject(Project project) {
        try {
            com.taskadapter.redmineapi.bean.Project redmineProject = manager.createProject(createRedmineProject(project));
            manager.addMembership(createRedmineMembership(redmineProject, project.getProjectLead(), PROJECT_MANAGER_ROLE));
        } catch (RedmineException e) {
            throw new CISClientBaseRuntimeException(e);
        }
    }

    @Override
    public void removeProject(Project project) {
        try {

            manager.deleteProject(redmineProjectKeyOf(project));
        } catch (RedmineException e) {
            throw new CISClientBaseRuntimeException(e);
        }

    }

    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        try {
            manager.createUser(createRedmineUser(developer));
        } catch (RedmineException e) {
            //失败表示用户已经存在
        }

    }

    @Override
    public void removeUser(Project project, Developer developer) {
        User user = getUserByDeveloperId(developer.getId());
        if (user == null) return;

        try {
            manager.deleteUser(user.getId());
            RedmineUserCache.removeUser(developer.getId());
        } catch (RedmineException e) {
            throw new CISClientBaseRuntimeException(e);
        }
    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        for (Developer developer : developers) {
            try {
                if (isMemberOfProject(project, developer.getId(), DEVELOPER_ROLE)
                        || isMemberOfProject(project, developer.getId(), PROJECT_MANAGER_ROLE)) {
                    continue;
                }

                manager.addMembership(createRedmineMembership(project, developer.getId(), DEVELOPER_ROLE));

            } catch (RedmineException e) {
                throw new CISClientBaseRuntimeException(e);
            }
        }

    }

    @Override
    public boolean authenticate() {
        // do nothing
        return true;
    }

    public boolean isMemberOfProject(Project project, String developerId, String roleName) {


        try {
            User user = getUserByDeveloperId(developerId);

            if (null == user) return false;

            for (Membership each : manager.getMemberships(findRedmineProjectByProject(project))) {
                for (Role role : each.getRoles()) {
                    if (each != null
                            && each.getUser() != null
                            && each.getUser().getId().equals(user.getId())
                            && role.getName().equals(roleName))
                        return true;
                }
            }
            return false;
        } catch (RedmineException e) {
            return false;
        }

    }

    @Override
    public void close() {
        manager.shutdown();
    }

    public boolean isExist(Project project) {
        try {
            return manager.getProjectByKey(redmineProjectKeyOf(project)) != null;
        } catch (RedmineException e) {
            return false;
        }
    }

    /**
     * 性能有些低！  TODO
     *
     * @param developer
     * @return
     */
    public boolean isExist(Developer developer) {
        if (RedmineUserCache.isExist(developer.getId())) {
            return true;
        }

        try {
            for (User user : manager.getUsers()) {
                if (user.getLogin().equals(developer.getId())) {
                    RedmineUserCache.cacheRedmineUser(developer.getId(), user);
                    return true;
                }
            }
            return false;
        } catch (RedmineException e) {
            throw new CISClientBaseRuntimeException(e);
        }

    }

    private User getUserByDeveloperId(String developerId) {
        if (RedmineUserCache.isExist(developerId)) {
            return RedmineUserCache.getUserByDeveloperId(developerId);
        }

        try {
            for (User user : manager.getUsers()) {
                if (user.getLogin().equals(developerId)) {
                    RedmineUserCache.cacheRedmineUser(developerId, user);
                    return user;
                }
            }
            return null;
        } catch (RedmineException e) {
            throw new CISClientBaseRuntimeException(e);
        }
    }


    private com.taskadapter.redmineapi.bean.Project createRedmineProject(Project project) {
        com.taskadapter.redmineapi.bean.Project result = new com.taskadapter.redmineapi.bean.Project();
        result.setName(project.getProjectName());
        result.setDescription(project.getDescription());
        result.setIdentifier(project.getProjectName().toLowerCase());
        result.setCreatedOn(new Date());
        return result;
    }

    private com.taskadapter.redmineapi.bean.Project findRedmineProjectByProject(Project project) {
        try {
            return manager.getProjectByKey(redmineProjectKeyOf(project));
        } catch (RedmineException e) {
            return null;
        }
    }


    private String redmineProjectKeyOf(Project project) {
        assert project != null;
        return project.getProjectName().toLowerCase();
    }

    private User createRedmineUser(Developer developer) {
        User user = new User();
        user.setFullName(developer.getFullName());
        user.setFirstName(developer.getFullName());
        user.setLastName(developer.getFullName());
        user.setMail(developer.getEmail());
        user.setPassword(developer.getPassword());
        user.setCreatedOn(new Date());
        user.setLogin(developer.getId());
        return user;
    }

    private Membership createRedmineMembership(com.taskadapter.redmineapi.bean.Project project, String developerId, String roleName) {
        Membership result = new Membership();
        result.setProject(project);
        result.setUser(getUserByDeveloperId(developerId));
        result.setRoles(Arrays.asList(findByRoleName(roleName)));
        return result;
    }

    private Membership createRedmineMembership(Project project, String developerId, String roleName) {
        return createRedmineMembership(findRedmineProjectByProject(project), developerId, roleName);
    }


    private Role findByRoleName(String name) {
        try {
            for (Role role : manager.getRoles()) {
                if (role.getName().equals(name)) return role;
            }
            return null;
        } catch (RedmineException e) {
            throw new CISClientBaseRuntimeException(e);
        }

    }

}
