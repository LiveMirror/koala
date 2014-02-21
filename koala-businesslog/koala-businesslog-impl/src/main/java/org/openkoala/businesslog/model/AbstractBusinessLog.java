package org.openkoala.businesslog.model;



import org.dayatang.domain.Entity;
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.InstanceFactory;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * User: zjzhai
 * Date: 12/5/13
 * Time: 4:54 PM
 */
@MappedSuperclass
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractBusinessLog implements Entity {

    private static final String ENTITY_REPOSITORY = "repository_businessLog";

   
    private Long id;

    
    private int version;

   
    private String category;

    
    private String log;

    /**
     * 获得实体的标识
     *
     * @return 实体的标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    /**
     * 设置实体的标识
     *
     * @param id 要设置的实体标识
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "LOG_CONTENT")
    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Column(name = "LOG_CATEGORY")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 获得实体的版本号。持久化框架以此实现乐观锁。
     *
     * @return 实体的版本号
     */
    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    /**
     * 设置实体的版本号。持久化框架以此实现乐观锁。
     *
     * @param version 要设置的版本号
     */
    public void setVersion(int version) {
        this.version = version;
    }

 
    @Transient
    public boolean isNew() {
        return id == null || id.intValue() == 0;
    }

  
    public boolean existed() {
        if (isNew()) {
            return false;
        }
        return getRepository().exists(getClass(), id);
    }

    
    public boolean notExisted() {
        return !existed();
    }

   
    public boolean existed(String propertyName, Object propertyValue) {
    	
        List<?> entities = getRepository().createCriteriaQuery(getClass()).eq(propertyName, propertyValue).list();
        return !(entities.isEmpty());
    }

    private static EntityRepository repository;

    public static EntityRepository getRepository() {
        if (repository == null) {
            repository = InstanceFactory.getInstance(EntityRepository.class, ENTITY_REPOSITORY);
        }
        return repository;
    }

    public static void setRepository(EntityRepository repository) {
        AbstractBusinessLog.repository = repository;
    }

    public void save() {
        getRepository().save(this);
    }

    public void remove() {
        getRepository().remove(this);
    }

    /**
     * 请改用每个实体对象的实例方法的existed()方法。
     *
     * @param clazz
     * @param id
     * @return
     */
    @Deprecated
    public static <T extends org.dayatang.domain.Entity> boolean exists(Class<T> clazz, Serializable id) {
        return getRepository().exists(clazz, id);
    }

    public static <T extends org.dayatang.domain.Entity> T get(Class<T> clazz, Serializable id) {
        return getRepository().get(clazz, id);
    }

    public static <T extends org.dayatang.domain.Entity> T getUnmodified(Class<T> clazz, T entity) {
        return getRepository().getUnmodified(clazz, entity);
    }

    public static <T extends org.dayatang.domain.Entity> T load(Class<T> clazz, Serializable id) {
        return getRepository().load(clazz, id);
    }

    public static <T extends org.dayatang.domain.Entity> List<T> findAll(Class<T> clazz) {
        return getRepository().findAll(clazz);
    }


    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract String toString();
}
