package org.openkoala.openci.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.openci.EntityNullException;
import org.openkoala.openci.application.RoleApplication;
import org.openkoala.openci.core.Role;
import org.springframework.transaction.annotation.Transactional;

@Named("roleApplication")
@Transactional("transactionManager_opencis")
public class RoleApplicationImpl implements RoleApplication {

	@Inject
	private QueryChannelService queryChannel;

	public void createRole(Role role) {
		if (role == null) {
			throw new EntityNullException();
		}
		role.save();
	}

	public void updateRole(Role role) {
		if (role == null) {
			throw new EntityNullException();
		}
		role.save();
	}

	public void abolishRole(Role role) {
		if (role == null) {
			throw new EntityNullException();
		}
		role.abolish(new Date());
	}

	public Page<Role> pagingQeuryRoles(int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();
		StringBuilder jpql = new StringBuilder("select _role from Role _role where _role.createDate <= ? and _role.abolishDate > ?");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);
		return queryChannel.createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pagesize).pagedList();
	}

	public void abolishRole(Role[] roles) {
		for (Role role : roles) {
			role.abolish(new Date());
		}
	}

	public List<Role> findAll() {
		return Role.findAll(Role.class);
	}

}
