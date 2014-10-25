package org.openkoala.koala.commons.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.Entity;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.utils.BeanUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Map;

public abstract class KoalaMyBatisBaseEntity implements Entity {


	private static final long serialVersionUID = 6157567600611070470L;

	public abstract Serializable getId();

	public abstract boolean existed();

	public abstract boolean notExisted();
	
	private static MybatisEntityRepository repository;

	/**
	 * 获取仓储对象实例。如果尚未拥有仓储实例则通过InstanceFactory向IoC容器获取一个。
	 *
	 * @return 仓储对象实例
	 */
	public static MybatisEntityRepository getRepository() {
		if (repository == null) {
			repository = InstanceFactory
					.getInstance(MybatisEntityRepository.class);
		}
		return repository;
	}
	
	 /**
     * 将实体本身持久化到数据库
     */
    public void save() {
        getRepository().save(this);
    }

    /**
     * 将实体本身从数据库中删除
     */
    public void remove() {
        getRepository().remove(this);
    }

    /**
     * 获取业务主键。业务主键是判断相同类型的两个实体在业务上的等价性的依据。如果相同类型的两个
     * 实体的业务主键相同，则认为两个实体是相同的，代表同一个实体。
     * <p>业务主键由实体的一个或多个属性组成。
     * @return 组成业务主键的属性的数组。
     */
    public abstract String[] businessKeys();

    /**
     * 依据业务主键获取哈希值。用于判定两个实体是否等价。
     * 等价的两个实体的hashCode相同，不等价的两个实体hashCode不同。
     * @return 实体的哈希值
     */
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(13, 37);
        Map<String, Object> propValues = new BeanUtils(this).getPropValues();
        if(businessKeys() == null){
            return builder.toHashCode();
        }
        for (String businessKey : businessKeys()) {
            builder = builder.append(propValues.get(businessKey));
        }
        return builder.toHashCode();
    }

    /**
     * 依据业务主键判断两个实体是否等价。
     * @param other 另一个实体
     * @return 如果本实体和other等价则返回true,否则返回false
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (businessKeys() == null || businessKeys().length == 0) {
            return false;
        }
        if (!(this.getClass().isAssignableFrom(other.getClass()))) {
            return false;
        }
        Map<String, Object> thisPropValues = new BeanUtils(this).getPropValuesExclude(Transient.class);
        Map<String, Object> otherPropValues = new BeanUtils(other).getPropValuesExclude(Transient.class);
        EqualsBuilder builder = new EqualsBuilder();
        for (String businessKey : businessKeys()) {
            builder.append(thisPropValues.get(businessKey), otherPropValues.get(businessKey));
        }
        return builder.isEquals();
    }
}
