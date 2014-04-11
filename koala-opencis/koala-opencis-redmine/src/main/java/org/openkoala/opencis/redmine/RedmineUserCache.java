package org.openkoala.opencis.redmine;

import com.taskadapter.redmineapi.bean.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Redmine服务器上的用户在本地的副本
 * User: zjzhai
 * Date: 4/10/14
 */
public class RedmineUserCache {

    private static Map<String, User> redmineUsers = new HashMap<String, User>();


    protected static boolean isExist(String developerId) {
        return redmineUsers.containsKey(developerId);
    }

    protected static void cacheRedmineUser(String developerId, User user) {
        redmineUsers.put(developerId, user);
    }


    protected static User getUserByDeveloperId(String developerId) {
        return redmineUsers.get(developerId);
    }


    protected static void removeUser(String developerId) {
        redmineUsers.remove(developerId);
    }
}
