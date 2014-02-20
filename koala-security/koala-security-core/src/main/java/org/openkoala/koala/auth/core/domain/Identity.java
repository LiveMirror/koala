package org.openkoala.koala.auth.core.domain;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author lingen
 * 
 */
@Entity
@Table(name = "KS_IDENTITY")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "IDENTITY_TYPE", discriminatorType = DiscriminatorType.STRING)
@Cacheable
public abstract class Identity extends Party {

	private static final long serialVersionUID = -3878339448106527391L;

	private boolean isValid;

	
	private String createOwner;

	
	private List<IdentityResourceAuthorization> authorizations;

	@Column(name = "ISVALID")
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	
	@Column(name = "CREATE_OWNER")
	public String getCreateOwner() {
		return createOwner;
	}

	public void setCreateOwner(String createOwner) {
		this.createOwner = createOwner;
	}

	public void disableIdentity() {
		this.isValid = false;
		this.save();
	}

	public void enableIdentity() {
		this.isValid = true;
		this.save();
	}

	public List<Identity> findByCreateOwner(String createOwner) {
		return null;
	}

	
	public List<IdentityResourceAuthorization> findIdentityResourceAuthorizations() {
		return authorizations;
	}
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "identity")
	public List<IdentityResourceAuthorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(List<IdentityResourceAuthorization> authorizations) {
		this.authorizations = authorizations;
	}

	public void deleteByCreateOwner() {
		return;
	}

	@Override
	public String toString() {
		return "Identity [isValid=" + isValid + ", createOwner=" + createOwner
				+ ", authorizations=" + authorizations + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((authorizations == null) ? 0 : authorizations.hashCode());
		result = prime * result
				+ ((createOwner == null) ? 0 : createOwner.hashCode());
		result = prime * result + (isValid ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Identity))
			return false;
		Identity other = (Identity) obj;
		if (authorizations == null) {
			if (other.authorizations != null)
				return false;
		} else if (!authorizations.equals(other.authorizations))
			return false;
		if (createOwner == null) {
			if (other.createOwner != null)
				return false;
		} else if (!createOwner.equals(other.createOwner))
			return false;
		if (isValid != other.isValid)
			return false;
		return true;
	}
	
}
