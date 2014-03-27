package org.openkoala.koala.commons.domain;

import org.dayatang.domain.Entity;
import org.dayatang.domain.NamedParameters;
import org.openkoala.koala.commons.domain.mybatis.MybatisNamedQuery;

import java.io.Serializable;
import java.util.List;

public interface MybatisEntityRepository {
	  /**
     * 将实体（无论是新的还是修改了的）保存到仓储中。
     *
     * @param <T> 实体的类型
     * @param entity 要存储的实体实例。
     * @return 持久化后的当前实体
     */
    <T extends Entity> T save(T entity);

    /**
     * 将实体从仓储中删除。如果仓储中不存在此实例将抛出EntityNotExistedException异常。
     *
     * @param entity 要删除的实体实例。
     */
    void remove(Entity entity);

    /**
     * 判断仓储中是否存在指定ID的实体实例。
     *
     * @param <T> 实体类型
     * @param clazz 实体的类
     * @param id 实体标识
     * @return 如果实体实例存在，返回true，否则返回false
     */
    <T extends Entity> boolean exists(Class<T> clazz, Serializable id);

    /**
     * 从仓储中获取指定类型、指定ID的实体
     *
     * @param <T> 实体类型
     * @param clazz 实体的类
     * @param id 实体标识
     * @return 一个实体实例。
     */
    <T extends Entity> T get(Class<T> clazz, Serializable id);

    /**
     * 从仓储中装载指定类型、指定ID的实体
     *
     * @param <T> 实体类型
     * @param clazz 实体的类
     * @param id 实体标识
     * @return 一个实体实例。
     */
    <T extends Entity> T load(Class<T> clazz, Serializable id);
    
    /**
     * 根据业务主键从仓储中获取指定类型的实体
     *
     * @param <T> 实体类型
     * @param clazz 实体的类
     * @param keyValues 代表业务主键值的命名参数。key为主键属性名，value为主键属性值
     * @return 一个实体实例。
     */
    <T extends Entity> T getByBusinessKeys(Class<T> clazz, NamedParameters keyValues);

    /**
     * 查找指定类型的所有实体
     *
     * @param <T> 实体类型
     * @param clazz 实体的类
     * @return 符合条件的实体集合
     */
    <T extends Entity> List<T> findAll(Class<T> clazz);

    /**
     * 创建命名查询
     *
     * @param queryName 命名查询的名字
     * @return 一个命名查询
     */
    MybatisNamedQuery createNamedQuery(Class namespace, String queryName);

    /**
     * 执行命名查询，返回符合条件的实体列表
     *
     * @param namedQuery 要执行的命名查询
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果列表
     */
    <T> List<T> find(MybatisNamedQuery namedQuery);

    /**
     * 执行命名查询，返回符合条件的单个实体
     *
     * @param namedQuery 要执行的命名查询
     * @param <T> 返回结果类型
     * @return 符合查询条件的单个结果
     */
    <T> T getSingleResult(MybatisNamedQuery namedQuery);

    /**
     * 使用命名查询执行更新仓储的操作。
     *
     * @param namedQuery 要执行的命名查询。
     * @return 被更新或删除的实体的数量
     */
    int executeUpdate(MybatisNamedQuery namedQuery);

}
