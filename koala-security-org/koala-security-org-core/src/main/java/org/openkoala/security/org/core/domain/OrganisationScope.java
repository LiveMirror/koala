package org.openkoala.security.org.core.domain;

import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.InstanceFactory;
import org.openkoala.organisation.domain.Organization;
import org.openkoala.security.core.domain.Scope;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 组织机构范围。
 * 
 * @author luzhao
 * 
 */
@Entity
@DiscriminatorValue("ORGANIZATION_SCOPE")
public class OrganisationScope extends Scope {

	private static final long serialVersionUID = 4668728765272500424L;

//	@ManyToOne
//	@JoinTable(name = "KS_OS__RELATION", //
//	joinColumns = @JoinColumn(name = "CHILD_ID"), //
//	inverseJoinColumns = @JoinColumn(name = "PARENT_ID"))
//	private OrganisationScope parent;
//
//	@OneToMany(mappedBy = "parent")
//	private Set<OrganisationScope> children = new HashSet<OrganisationScope>();

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

    protected OrganisationScope() {}

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
