package org.openkoala.security.org.facade.command;

import java.util.Set;

public class GrantPermissionToUserInScopeCommand {

	private Long permissionId;

	private Set<Long> scopeIds;

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

    public Set<Long> getScopeIds() {
        return scopeIds;
    }

    public void setScopeIds(Set<Long> scopeIds) {
        this.scopeIds = scopeIds;
    }
}
