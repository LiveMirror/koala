package org.openkoala.koala.deploy.curd.module.core.relation;

import org.openkoala.koala.deploy.curd.module.core.RelationModel;
import org.openkoala.koala.deploy.curd.module.core.RelationType;

/**
 * 
 * 类    名：ManyToManyRelationModel.java
 *   
 * 功能描述：多对多关联	
 *  
 * 创建日期：2013-1-18下午4:20:17     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class ManyToManyRelationModel extends RelationModel {
    @Override
    public RelationType getType() {
        return RelationType.ManyToMany;
    }
}
