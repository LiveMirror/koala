package org.openkoala.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.koala.auth.core.domain.Role;

import com.dayatang.utils.DateUtils;

public class RoleBeanUtil {

	public static void roleToRoleVO(Role role, RoleVO roleVo) {
		DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		roleVo.setAbolishDate(formater.format(role.getAbolishDate()));
		roleVo.setCreateDate(formater.format(role.getCreateDate()));
		roleVo.setCreateOwner(role.getCreateOwner());
		roleVo.setId(role.getId());
		roleVo.setName(role.getName());
		roleVo.setSerialNumber(role.getSerialNumber());
		roleVo.setSortOrder(role.getSortOrder());
		roleVo.setRoleDesc(role.getRoleDesc());
		roleVo.setValid(role.isValid());
		roleVo.setVersion(role.getVersion());
	}

	public static void roleVOToRole(Role role, RoleVO roleVo) {
		role.setAbolishDate(DateUtils.MAX_DATE);
		role.setCreateDate(new Date());
		role.setCreateOwner(roleVo.getCreateOwner());
		role.setName(roleVo.getName());
		role.setRoleDesc(roleVo.getRoleDesc());
		role.setSerialNumber(roleVo.getSerialNumber());
		role.setSortOrder(roleVo.getSortOrder());
		role.setValid(roleVo.isValid());
	}
}
