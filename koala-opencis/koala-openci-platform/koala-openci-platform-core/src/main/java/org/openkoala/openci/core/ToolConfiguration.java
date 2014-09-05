package org.openkoala.openci.core;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.AbstractEntity;

@Entity
@Table(name = "tool_configurations")
@DiscriminatorColumn(name = "tool_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ToolConfiguration extends AbstractEntity {

	private static final long serialVersionUID = -7992490907551882249L;

	
	private String name;
	
	
	@Column(name = "service_url")
	private String serviceUrl;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	private String password;
	
	private boolean usable = false;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "create_date")
	private Date createDate = new Date();
	
	public ToolConfiguration() {
	}
	
	public ToolConfiguration(String name, String serviceUrl, String username, String password) {
		this.name = name;
		this.serviceUrl = serviceUrl;
		this.username = username;
		this.password = password;
	}

	public static List<ToolConfiguration> findByUsable() {
		return getRepository().createCriteriaQuery(ToolConfiguration.class).eq("usable", true).list();
	}
	
	public void usabled() {
		usable = true;
		save();
	}
	
	public void unusabled() {
		usable = false;
		save();
	}

	public String getName() {
		return name;
	}
	
	public String getServiceUrl() {
		return serviceUrl;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public boolean isUsable() {
		return usable;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	protected void setUsable(boolean usable) {
		this.usable = usable;
	}

	protected void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

    @Override
    public String[] businessKeys() {
        return new String[] {"name", "serviceUrl"};
    }

	@Override
	public String toString() {
		return getName();
	}

}
