/*
 * Copyright (c) openkoala 2011 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.bpm.processdyna.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.dayatang.domain.Entity;
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.domain.NamedParameters;

@MappedSuperclass
public abstract class BpmFormEntity implements Entity {

	private static final long serialVersionUID = 1342711951865077906L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Version
	@Column(name = "VERSION")
	private int version;

	/**
	 * 获得实体的标识
	 * 
	 * @return 实体的标识
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * 设置实体的标识
	 * 
	 * @param id
	 *            要设置的实体标识
	 */
	public void setId(Long id) {
		this.id = id;
	}

	private static EntityRepository repository;

	public static EntityRepository getRepository() {
		if (repository == null) {
			repository = InstanceFactory.getInstance(EntityRepository.class,
					"repository");
		}
		return repository;
	}

	public static void setRepository(EntityRepository repository) {
		BpmFormEntity.repository = repository;
	}

	/**
	 * 获得实体的版本号。持久化框架以此实现乐观锁。
	 * 
	 * @return 实体的版本号
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * 设置实体的版本号。持久化框架以此实现乐观锁。
	 * 
	 * @param version
	 *            要设置的版本号
	 */
	public void setVersion(int version) {
		this.version = version;
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
	 * 根据实体类型和ID从仓储中获取实体
	 * 
	 * @param <T>
	 *            实体类型
	 * @param clazz
	 *            实体的类
	 * @param id
	 *            实体的ID
	 * @return 类型为T或T的子类型，ID为id的实体。
	 */
	public static <T extends Entity> T get(Class<T> clazz, Serializable id) {
		return getRepository().get(clazz, id);
	}

	/**
	 * 查找实体在数据库中的未修改版本
	 * 
	 * @param <T>
	 *            实体类型
	 * @param clazz
	 *            实体的类
	 * @param entity
	 *            实体
	 * @return 实体的未修改版本。
	 */
	public static <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		return getRepository().getUnmodified(clazz, entity);
	}

	/**
	 * 根据实体类型和ID从仓储中加载实体(与get()方法的区别在于除id外所有的属性值都未填充)
	 * 
	 * @param <T>
	 *            实体类型
	 * @param clazz
	 *            实体的类
	 * @param id
	 *            实体的ID
	 * @return 类型为T或T的子类型，ID为id的实体。
	 */
	public static <T extends Entity> T load(Class<T> clazz, Serializable id) {
		return getRepository().load(clazz, id);
	}

	/**
	 * 查找指定类型的所有实体
	 * 
	 * @param <T>
	 *            实体所属的类型
	 * @param clazz
	 *            实体所属的类
	 * @return 符合条件的实体列表
	 */
	public static <T extends Entity> List<T> findAll(Class<T> clazz) {
		return getRepository().createCriteriaQuery(clazz).list();
	}

	/**
	 * 根据单个属性值以“属性=属性值”的方式查找实体
	 * 
	 * @param <T>
	 *            实体所属的类型
	 * @param clazz
	 *            实体所属的类
	 * @param propName
	 *            属性名
	 * @param value
	 *            匹配的属性值
	 * @return 符合条件的实体列表
	 */
	public static <T extends Entity> List<T> findByProperty(Class<T> clazz,
			String propName, Object value) {
		return getRepository().findByProperty(clazz, propName, value);
	}

	/**
	 * 根据多个属性值以“属性=属性值”的方式查找实体，例如查找name="张三", age=35的员工。
	 * 
	 * @param <T>
	 *            实体所属的类型
	 * @param clazz
	 *            实体所属的类
	 * @param propValues
	 *            属性值匹配条件
	 * @return 符合条件的实体列表
	 */
	public static <T extends Entity> List<T> findByProperties(Class<T> clazz,
			Map<String, Object> propValues) {
		return getRepository().findByProperties(clazz,
				NamedParameters.create(propValues));
	}

	@Override
	public String toString() {
		return " [id=" + id + ", version=" + version + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 0;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof BpmFormEntity))
			return false;
		BpmFormEntity other = (BpmFormEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	/**
	 * 判断该实体是否已经存在于数据库中。
	 * 
	 * @return 如果数据库中已经存在拥有该id的实体则返回true，否则返回false。
	 */
	@Override
	public boolean existed() {
		Object id = getId();
		if (id == null) {
			return false;
		}
		if (id instanceof Number && ((Number) id).intValue() == 0) {
			return false;
		}
		return getRepository().exists(getClass(), getId());
	}

	/**
	 * 判断该实体是否不存在于数据库中。
	 * 
	 * @return 如果数据库中已经存在拥有该id的实体则返回false，否则返回true。
	 */
	@Override
	public boolean notExisted() {
		return !existed();
	}
	
	/**
     * 获取业务主键。业务主键是判断相同类型的两个实体在业务上的等价性的依据。如果相同类型的两个
     * 实体的业务主键相同，则认为两个实体是相同的，代表同一个实体。
     * <p>业务主键由实体的一个或多个属性组成。
     * @return 组成业务主键的属性的数组。
     */
    public abstract String[] businessKeys();

}
