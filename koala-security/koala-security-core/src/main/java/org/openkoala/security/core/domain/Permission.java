package org.openkoala.security.core.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.security.core.IdentifierIsExistedException;

/**
 * 权限。代表系统的一项操作或功能。
 * 
 * @author luzhao
 * 
 */
@Entity
@DiscriminatorValue("PERMISSION")
public class Permission extends Authority {

	private static final long serialVersionUID = 4631351008490511334L;

	/**
	 * 权限标识符 例如：user:create
	 */
	@Column(name = "IDENTIFIER")
	private String identifier;

	@ManyToMany(mappedBy = "permissions")
	private Set<Role> roles = new HashSet<Role>();

	protected Permission() {}

	public Permission(String name, String identifier) {
		super(name);
		this.identifier = identifier;
	}

	@Override
	public void save() {
		isNameExisted();
		isIdentifierExisted(this.identifier);
		super.save();
	}

	public Permission getPermissionBy(String identifier) {
		return getRepository()//
				.createCriteriaQuery(Permission.class)//
				.eq("identifier", identifier)//
				.singleResult();
	}

	@Override
	public void update() {
		Permission permission = getBy(this.getId());
		
		if(!StringUtils.isBlank(this.getName()) && !permission.getName().equals(this.getName())){
			isNameExisted();
			permission.name = this.getName();
		}
		
		if(!StringUtils.isBlank(this.getIdentifier()) && !permission.getIdentifier().equals(this.getIdentifier()))
		{
			isIdentifierExisted(this.getIdentifier());
			permission.identifier = this.getIdentifier();
		}
		permission.setDescription(this.getDescription());
	}

	@Override
	public Authority getAuthorityBy(String name) {
		return getRepository()//
				.createNamedQuery("Authority.getAuthorityByName")//
				.addParameter("authorityType", Permission.class)//
				.addParameter("name", name)//
				.singleResult();

	}
	
	public static Permission getBy(Long id){
		return Permission.get(Permission.class, id);
	}
	
	private void isIdentifierExisted(String identifier) {
		Permission permission = getPermissionBy(identifier);
		if(permission != null){
			throw new IdentifierIsExistedException("permission.identifier.existed");
		}
		
	}
	
	public String[] businessKeys() {
		return new String[] { "name", "identifier" };
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(name)//
				.append(identifier)//
				.append(getDescription())//
				.build();
	}
	
	public Set<Role> getRoles() {
		return Collections.unmodifiableSet(roles);
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getIdentifier() {
		return identifier;
	}

}