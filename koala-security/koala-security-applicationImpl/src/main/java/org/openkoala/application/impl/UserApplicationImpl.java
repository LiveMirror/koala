package org.openkoala.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.openkoala.exception.extend.ApplicationException;
import org.openkoala.auth.application.UserApplication;
import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.core.domain.Role;
import org.openkoala.koala.auth.core.domain.RoleUserAuthorization;
import org.openkoala.koala.auth.core.domain.User;
import org.openkoala.util.RoleBeanUtil;
import org.openkoala.util.UserBeanUtil;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.querychannel.support.Page;
import com.dayatang.utils.DateUtils;

@Named("userApplication")
@Transactional(value="transactionManager_security")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "UserApplication")
@Remote
public class UserApplicationImpl extends BaseImpl implements UserApplication {

    public UserVO getUser(Long userId) {
        User user = User.get(User.class, userId);
        UserVO userVO = new UserVO();
        UserBeanUtil.userToUserVO(user, userVO);
        return userVO;
    }

    public UserVO saveUser(UserVO userVO) {
		User user = new User();
		UserBeanUtil.userVOToUser(user, userVO);
        // 检查用户账号是否已经存在
        if (user.isAccountExist()) {
        	throw new ApplicationException("userAccount.exist", null);
        }
        user.save();
        userVO.setId(user.getId());
        return userVO;
    }

    public void updateUser(UserVO userVO) {
        User user = User.load(User.class, userVO.getId());
        user.setName(userVO.getName());
        user.setUserDesc(userVO.getUserDesc());
        user.setUserAccount(userVO.getUserAccount());
        user.setValid(userVO.isValid());
    }

    public boolean updatePassword(UserVO userVO, String oldPass) {
        User user = User.findByUserAccount(userVO.getUserAccount());
        if (oldPass.equals(user.getUserPassword())) {
            user.setUserPassword(userVO.getUserPassword());
            return true;
        }
        return false;
    }

    public void removeUser(Long userId) {
        User user = User.load(User.class, userId);
        user.setAbolishDate(new Date());
        for (RoleUserAuthorization each : user.findRoles()) {
        	each.setAbolishDate(new Date());
        }
    }

    public List<UserVO> findAllUser() {
        List<UserVO> results = new ArrayList<UserVO>();
        List<User> users = User.findAll(User.class);
        for (User each : users) {
            UserVO userVO = new UserVO();
            UserBeanUtil.userToUserVO(each, userVO);
            results.add(userVO);
        }
        return results;
    }

    public Page<UserVO> pageQueryUser(int currentPage, int pageSize) {
        List<UserVO> results = new ArrayList<UserVO>();
        Page<User> pages = queryChannel().queryPagedResultByPageNo("select m from User m where m.abolishDate>?", //
        		new Object[] { new Date() }, currentPage, pageSize);
        for (User each : pages.getResult()) {
            UserVO userVO = new UserVO();
            UserBeanUtil.userToUserVO(each, userVO);
            results.add(userVO);
        }
        return new Page<UserVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), results);
    }

    public UserVO findByUserAccount(String userAccount) {
        User user = User.findByUserAccount(userAccount);
        if (user == null) {
        	return null;
        }
        UserVO userVO = new UserVO();
        UserBeanUtil.userToUserVO(user, userVO);
        return userVO;
    }

    public void assignRole(UserVO userVO, RoleVO roleVO) {
        User us = new User();
        us.setId(Long.valueOf(userVO.getId()));
        Role role = new Role();
        role.setId(Long.valueOf(roleVO.getId()));
        us.assignRole(role);
    }

    public void assignRole(UserVO userVO, List<RoleVO> roleVOs) {
        for (RoleVO each : roleVOs) {
            assignRole(userVO, each);
        }
    }

    public void resetPassword() {
        User us = new User();
        us.resetPassword();
    }

    public void abolishRole(UserVO userVO, List<RoleVO> roles) {
        for (RoleVO each : roles) {
            User user = User.get(User.class, userVO.getId());
            Role role = Role.get(Role.class, each.getId());
            user.abolishRole(role);
        }
    }

    public Page<RoleVO> pageQueryNotAssignRoleByUser(int currentPage, int pageSize, UserVO userVO) {
        List<RoleVO> results = new ArrayList<RoleVO>();
        
        Page<Role> pages = queryChannel().queryPagedResultByPageNo( //
	        "select role from Role role where role.id not in" + //
	        "(select role from RoleUserAuthorization rau join " + //
	        "rau.role role join rau.user user where user.id=? " + //
	        "and rau.abolishDate>?) and role.abolishDate>?",  //
	        new Object[] { userVO.getId(), new Date(), new Date() }, 
	        currentPage, pageSize);
        
        for (Role each : pages.getResult()) {
            RoleVO roleVO = new RoleVO();
            RoleBeanUtil.roleToRoleVO(each, roleVO);
            results.add(roleVO);
        }
        
        return new Page<RoleVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), results);
    }

    public Page<UserVO> pageQueryUserCustom(int currentPage, int pageSize, QueryConditionVO query) {
        List<UserVO> results = new ArrayList<UserVO>();
        Page<User> pages = queryChannel().queryPagedResultByPageNo(genQueryCondition(query), //
        		new Object[] { new Date() }, currentPage, pageSize);
        for (User each : pages.getResult()) {
            UserVO userVO = new UserVO();
            UserBeanUtil.userToUserVO(each, userVO);
            results.add(userVO);
        }
        return new Page<UserVO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), results);
    }
    


}
