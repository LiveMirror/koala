package org.openkoala.security.org.facade.command;

import java.util.Set;

public class GrantRoleToUserInScopeCommand {

	private Long roleId;
	
	private Set<Long> scopeIds;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

    public Set<Long> getScopeIds() {
        return scopeIds;
    }

    public void setScopeIds(Set<Long> scopeIds) {
        this.scopeIds = scopeIds;
    }
}
