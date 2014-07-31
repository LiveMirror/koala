package org.openkoala.security.core.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "KS_SCOPES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
public abstract class Scope extends SecurityAbstractEntity {

	private static final long serialVersionUID = -7219997981491797461L;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "LEVEL")
	private int level = 0;

	@Transient
	public abstract Scope getParent();

	@Transient
	public abstract Set<? extends Scope> getChildren();

	protected Scope() {}

	public Scope(String name) {
		this.name = name;
	}

	public abstract void update();
	
	public abstract void addChild(Scope child);

	public abstract void removeChild(Scope child);
	
	public static Scope getBy(Long scopeId) {
		return Scope.get(Scope.class, scopeId);
	}
	
	public boolean contains(Scope scope) {

		if (scope == null) {
			return false;
		}

		if (equals(scope)) {
			return true;
		}

		if (getChildren().contains(scope)) {
			return true;
		}

		return contains(scope.getParent());
	}
	
	@Override
	public String[] businessKeys() {
		return new String[] { "name"};
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(name)//
				.append(description)//
				.build();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}