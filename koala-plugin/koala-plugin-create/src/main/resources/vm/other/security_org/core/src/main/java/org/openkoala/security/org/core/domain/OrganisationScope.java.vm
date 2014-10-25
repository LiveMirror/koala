package org.openkoala.security.org.core.domain;

import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.security.core.domain.Scope;

import javax.persistence.*;

import java.util.Set;

/**
 * 组织机构范围。
 * 支持数据权限。
 *
 * @author lucas
 */
@Entity
@DiscriminatorValue("ORGANIZATION_SCOPE")
@NamedQueries(@NamedQuery(name = "OrganisationScope.hasOrganizationOfScope", query = "FROM OrganisationScope _scope WHERE _scope.organization = :organization"))
public class OrganisationScope extends Scope {

    private static final long serialVersionUID = 4668728765272500424L;

    @OneToOne
    @JoinColumn(name = "ORGANIZATION_ID")
    private Organization organization;

    @Transient
    @Override
    public Scope getParent() {
        return null;
    }

    @Transient
    @Override
    public Set<? extends Scope> getChildren() {
        return null;
    }

    protected OrganisationScope() {
    }

    public OrganisationScope(String name, Organization organization) {
        super(name);
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String[] businessKeys() {
        return new String[0];
    }
}
