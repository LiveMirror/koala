package org.openkoala.koala.auth.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.dayatang.domain.Entity;

/**
 * 时刻时段类实体。代表仅在一个确定的时间段内有效的一大类实体。
 * 所有的时刻时段实体类都有创建日期和废除日期。未废除的实体，其废除日期是一个遥远的未来日期。
 * @author yyang
 *
 */
@MappedSuperclass
public abstract class TimeIntervalEntity extends KoalaSecurityEntity {

	private static final long serialVersionUID = 858481853210607590L;

	
	private Date createDate;

	
	private Date abolishDate;

	public TimeIntervalEntity() {
	}

	public TimeIntervalEntity(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "CREATE_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "createDate.is.null")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "ABOLISH_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "abolishDate.is.null")
	public Date getAbolishDate() {
		return abolishDate;
	}

	public void setAbolishDate(Date abolishDate) {
		this.abolishDate = abolishDate;
	}

	/**
	 * 是否已经废除
	 * @param queryDate
	 * @return
	 */
	public boolean isAbolished(Date queryDate) {
		if (abolishDate == null) {
			return false;
		}
		return abolishDate.before(queryDate) || abolishDate.equals(queryDate);
	}

	
	public void validate() {
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
	
	/**
	 * 查找所有未被废除的实体
	 * @param clazz
	 * @param queryDate
	 * @return
	 */
	public static <T extends Entity> List<T> findAll(Class<T> clazz, Date queryDate) {
		return getRepository().createCriteriaQuery(clazz).le("createDate", queryDate)
		.gt("abolishDate", queryDate).list();
	}
	
	/**
	 * 保存实体
	 */
	public void save() {
		getRepository().save(this);
	}

	/**
	 * 删除实体
	 */
	public void remove() {
		getRepository().remove(this);
	}

	/**
	 * 判断实体在数据库中是否存在
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static <T extends Entity> boolean exists(Class<T> clazz,
			Serializable id) {
		return getRepository().exists(clazz, id);
	}

	/**
	 * 获取实体
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static <T extends Entity> T get(Class<T> clazz, Serializable id) {
		return getRepository().get(clazz, id);
	}

	/**
	 * 获取未被修改的实体
	 * @param clazz
	 * @param entity
	 * @return
	 */
	public static <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		return getRepository().getUnmodified(clazz, entity);
	}

	/**
	 * 加载实体
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static <T extends Entity> T load(Class<T> clazz, Serializable id) {
		return getRepository().load(clazz, id);
	}

	/**
	 * 查找实体的所有数据
	 * @param clazz
	 * @return
	 */
	public static <T extends Entity> List<T> findAll(Class<T> clazz) {
		return getRepository().findAll(clazz);
	}

	@Override
	public String toString() {
		return "TimeIntervalEntity [createDate=" + createDate
				+ ", abolishDate=" + abolishDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((abolishDate == null) ? 0 : abolishDate.hashCode());
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof TimeIntervalEntity))
			return false;
		TimeIntervalEntity other = (TimeIntervalEntity) obj;
		if (abolishDate == null) {
			if (other.abolishDate != null)
				return false;
		} else if (!abolishDate.equals(other.abolishDate))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		return true;
	}
	
	
}

