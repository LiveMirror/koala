package org.openkoala.organisation.core.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dayatang.domain.CriteriaQuery;
import org.openkoala.organisation.core.NameExistException;
import org.openkoala.organisation.core.TheJobHasPostAccountabilityException;

/**
 * 职务
 * 
 * @author xmfang
 * 
 */
@Entity
@DiscriminatorValue("JOB")
public class Job extends Party {

	private static final long serialVersionUID = -5433410950032866468L;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	public Job() {
		super();
	}

	public Job(String name) {
		super(name);
	}

	public Job(String name, String sn) {
		super(name, sn);
	}

	@Override
	public void terminate(Date date) {
		if (hasPosts(date)) {
			throw new TheJobHasPostAccountabilityException();
		}
		super.terminate(date);
	}

	private boolean hasPosts(Date date) {
		List<Post> posts = Post.findByJob(this, date);
		return !posts.isEmpty();
	}

	@Override
	public void save() {
		if (nameExist()) {
			throw new NameExistException();
		}
		super.save();
	}

	private boolean nameExist() {
		Date now = new Date();
		CriteriaQuery query = getRepository().createCriteriaQuery(Job.class).eq("name", getName()).le("createDate", now).gt("terminateDate", now);
		if (getId() != null) {
			query.notEq("id", getId());
		}
		return query.singleResult() != null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Job)) {
			return false;
		}
		Job that = (Job) other;
		return new EqualsBuilder().append(this.getSn(), that.getSn()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getSn()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getName()).build();
	}

}
