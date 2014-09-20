package org.openkoala.security.org.facade.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by luzhao on 14-9-4.
 */
public class AuthorizationDTO {

    private Long actorId;

    private Long authorityId;

    private Long scopeId;

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

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()//
                .append(actorId)//
                .append(authorityId)//
                .append(scopeId)//
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AuthorizationDTO)) {
            return false;
        }
        AuthorizationDTO that = (AuthorizationDTO) other;
        return new EqualsBuilder()//
                .append(this.getActorId(), that.getActorId())//
                .append(this.getAuthorityId(), that.getAuthorityId())//
                .append(this.getScopeId(), that.getScopeId())//
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)//
                .append(getActorId())//
                .append(getAuthorityId())//
                .append(getScopeId())//
                .build();//
    }
}
