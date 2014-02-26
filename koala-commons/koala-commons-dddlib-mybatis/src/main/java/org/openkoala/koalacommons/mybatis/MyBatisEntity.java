package org.openkoala.koalacommons.mybatis;

import java.io.Serializable;

import org.dayatang.domain.Entity;
import org.dayatang.domain.InstanceFactory;

public abstract class MyBatisEntity implements Entity {


	private static final long serialVersionUID = 6157567600611070470L;

	public abstract Serializable getId();

	public abstract boolean existed();

	public abstract boolean notExisted();

	public abstract String[] businessKeys();
	
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
}
