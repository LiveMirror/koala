package org.openkoala.security.core.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.security.core.CorrelationException;
import org.openkoala.security.core.NameIsExistedException;
import org.openkoala.security.core.NullArgumentException;

@Entity
@Table(name = "KS_SECURITYRESOURCES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
        @NamedQuery(name = "SecurityResource.findAllByType", query = "SELECT _securityResource  FROM SecurityResource _securityResource WHERE TYPE(_securityResource) = :securityResourceType"),
        @NamedQuery(name = "SecurityResource.findByName", query = "SELECT _securityResource  FROM SecurityResource _securityResource WHERE TYPE(_securityResource) = :securityResourceType AND _securityResource.name = :name")})
public abstract class SecurityResource extends SecurityAbstractEntity {

    private static final long serialVersionUID = 6064565786784560656L;

    /**
     * 名称
     */
    @NotNull
    @Column(name = "NAME")
    private String name;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    protected SecurityResource() {}

    public SecurityResource(String name) {
        checkArgumentIsNull("name", name);
        isNameExisted(name);
        this.name = name;
    }

    @Override
    public void remove() {
        if (!ResourceAssignment.findByResource(this).isEmpty()) {
            throw new CorrelationException("securityResource has authority, cannot remove it.");
        }
        super.remove();
    }

    /**
     * 批量保存。
     *
     * @param securityResources
     */
    public static void batchSave(List<? extends SecurityResource> securityResources) {
        for (SecurityResource securityResource : securityResources) {
            securityResource.save();
        }
    }

    /**
     * @param name name of the SecurityResource, can't be null.
     * @return
     */
    public abstract SecurityResource findByName(String name);

    public void changeName(String name) {
        checkArgumentIsNull("name", name);
        if (!name.equals(this.getName())) {
            isNameExisted(name);
            this.name = name;
            this.save();
        }
    }

    protected static void checkArgumentIsNull(String nullMessage, String argument) {
        if (StringUtils.isBlank(argument)) {
            throw new NullArgumentException(nullMessage);
        }
    }

    protected void isNameExisted(String name) {
        if (findByName(name) != null) {
            throw new NameIsExistedException("securityResource.name.exist");
        }
    }

    @Override
    public String[] businessKeys() {
        return new String[]{"name"};
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}