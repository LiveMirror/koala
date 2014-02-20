package org.openkoala.openci.core;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.dayatang.domain.AbstractEntity;
import org.dayatang.domain.Entity;
import org.dayatang.utils.DateUtils;

@MappedSuperclass
public abstract class TimeIntervalEntity extends AbstractEntity {

	private static final long serialVersionUID = 858481853210607590L;

	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.DATE)
	@NotNull(message = "createDate.is.null")
	private Date createDate = new Date();

	@Column(name = "abolish_date", nullable = false)
	@Temporal(TemporalType.DATE)
	@NotNull(message = "abolishDate.is.null")
	private Date abolishDate = DateUtils.MAX_DATE;

	public TimeIntervalEntity() {
	}

	public TimeIntervalEntity(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getAbolishDate() {
		return abolishDate;
	}

	public void setAbolishDate(Date abolishDate) {
		this.abolishDate = abolishDate;
	}

	public boolean isAbolished(Date queryDate) {
		if (abolishDate == null) {
			return false;
		}
		return abolishDate.before(queryDate) || abolishDate.equals(queryDate);
	}

	/**
	 * 在指定的日期废除HR实体。
	 * @param abolishDate
	 */
	public void abolish(Date abolishDate) {
		if (abolishDate == null) {
			throw new IllegalArgumentException("abolishDate.isNull");
		}
		if (isAbolished(abolishDate)) {
			return;
		}
		setAbolishDate(abolishDate);
		getRepository().save(this);
	}

	public static <T extends Entity> T get(Class<T> clazz, Serializable id, Date queryDate) {
		return getRepository().createCriteriaQuery(clazz)
				.le("createDate", queryDate)
				.gt("abolishDate", queryDate)
				.eq("id", id).singleResult();
	}

	public static <T extends Entity> T get(Class<T> clazz, Serializable id) {
		Date now = new Date();
		return getRepository().createCriteriaQuery(clazz)
				.le("createDate", now)
				.gt("abolishDate", now)
				.eq("id", id).singleResult();
	}
	
	public static <T extends Entity> List<T> findAll(Class<T> clazz, Date queryDate) {
		return getRepository().createCriteriaQuery(clazz)
				.le("createDate", queryDate)
				.gt("abolishDate", queryDate).list();
	}
	
	public static <T extends Entity> List<T> findAll(Class<T> clazz) {
		Date now = new Date();
		return getRepository().createCriteriaQuery(clazz)
				.le("createDate", now)
				.gt("abolishDate", now).list();
	}

}
