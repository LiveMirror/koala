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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dayatang.domain.AbstractEntity;

@Entity
@Table(name = "KS_SCOPES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
public abstract class Scope extends AbstractEntity {

	private static final long serialVersionUID = -7219997981491797461L;

	@Transient
	public abstract Scope getParent();

	@Transient
	public abstract Set<? extends Scope> getChildren();

	protected Scope() {}

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

}