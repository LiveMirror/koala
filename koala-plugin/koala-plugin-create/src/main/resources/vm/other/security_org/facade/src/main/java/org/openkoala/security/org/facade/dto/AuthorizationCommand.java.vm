package org.openkoala.security.org.facade.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by luzhao on 14-9-4.
 */
public class AuthorizationCommand {

    private Long actorId;

    private Long[] authorityIds;

    private Long organizationId;

    private String organizationName;

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public Long[] getAuthorityIds() {
        return authorityIds;
    }

    public void setAuthorityIds(Long[] authorityIds) {
        this.authorityIds = authorityIds;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()//
                .append(actorId)//
                .append(authorityIds)//
                .append(organizationId)//
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AuthorizationCommand)) {
            return false;
        }
        AuthorizationCommand that = (AuthorizationCommand) other;
        return new EqualsBuilder()//
                .append(this.getActorId(), that.getActorId())//
                .append(this.getAuthorityIds(), that.getAuthorityIds())//
                .append(this.getOrganizationId(), that.getOrganizationId())//
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)//
                .append(getActorId())//
                .append(getAuthorityIds())//
                .append(getOrganizationId())//
                .build();//
    }
}
