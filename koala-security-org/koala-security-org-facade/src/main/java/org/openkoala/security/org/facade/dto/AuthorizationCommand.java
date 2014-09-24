package org.openkoala.security.org.facade.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by luzhao on 14-9-4.
 */
public class AuthorizationCommand {

    private Long actorId;

    private Long authorityId;

    private Long organizationId;

    private String organizationName;

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
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
                .append(authorityId)//
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
                .append(this.getAuthorityId(), that.getAuthorityId())//
                .append(this.getOrganizationId(), that.getOrganizationId())//
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)//
                .append(getActorId())//
                .append(getAuthorityId())//
                .append(getOrganizationId())//
                .build();//
    }
}
