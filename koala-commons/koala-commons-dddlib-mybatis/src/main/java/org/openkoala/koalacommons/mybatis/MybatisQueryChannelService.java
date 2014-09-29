package org.openkoala.koalacommons.mybatis;

import java.util.List;

import org.dayatang.utils.Page;
import org.openkoala.koalacommons.mybatis.querychannel.MybatisChannelQuery;

public interface MybatisQueryChannelService {

    /**
     * 创建命名查询
     *
     * @param queryName 命名查询的名字
     * @return 一个命名查询
     */
	MybatisChannelQuery createNamedQuery(Class namespace,String queryName);

    /**
     * 执行查询，返回符合条件的结果列表
     *
     * @param query 要执行的查询
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果列表
     */
    <T> List<T> list(MybatisChannelQuery query);

    /**
     * 执行查询，分页返回符合条件的结果列表
     *
     * @param query 要执行的查询
     * @param <T> 返回结果元素类型
     * @return 符合查询条件的结果页
     */
    <T> Page<T> pagedList(MybatisChannelQuery query);

    /**
     * 执行查询，返回符合条件的单个实体
     *
     * @param query 要执行的查询
     * @param <T> 返回结果类型
     * @return 符合查询条件的单个结果
     */
    <T> T getSingleResult(MybatisChannelQuery query);

    /**
     * 获取查询结果的总数。
     *
     * @param query 要执行的查询
     * @return 符合查询条件的结果的总数
     */
    long getResultCount(MybatisChannelQuery query);
}
