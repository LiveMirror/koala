package org.openkoala.util;

import java.util.Date;

import org.openkoala.auth.application.vo.UserVO;
import org.openkoala.koala.auth.core.domain.User;

import com.dayatang.utils.DateUtils;

public class UserBeanUtil {

	
	public static  void userToUserVO(User user,UserVO userVO) {
		userVO.setId(user.getId());
		userVO.setLastLoginTime(user.getLastLoginTime() == null ? "" : user.getLastLoginTime().toString());
		userVO.setUserAccount(user.getUserAccount());
		userVO.setUserPassword(user.getUserPassword());
		userVO.setUserDesc(user.getUserDesc());
		userVO.setAbolishDate(user.getAbolishDate() == null ? "" : user.getAbolishDate().toString());
		userVO.setCreateDate(user.getCreateDate() == null ? "" : user.getCreateDate().toString());
		userVO.setCreateOwner(user.getCreateOwner());
		userVO.setName(user.getName());
		userVO.setSerialNumber(user.getSerialNumber());
		userVO.setSortOrder(user.getSortOrder());
		userVO.setValid(user.isValid());
	}
	
	public static  void userVOToUser(User user,UserVO userVo) {
		user.setAbolishDate(DateUtils.MAX_DATE);
        user.setCreateDate(new Date());
        user.setUserAccount(userVo.getUserAccount());
        user.setUserPassword(userVo.getUserPassword());
        user.setUserDesc(userVo.getUserDesc());
        user.setCreateOwner(userVo.getCreateOwner());
        user.setName(userVo.getName());
        user.setSerialNumber(userVo.getSerialNumber());
        user.setSortOrder(userVo.getSortOrder());
        user.setValid(userVo.isValid());
	}
}
