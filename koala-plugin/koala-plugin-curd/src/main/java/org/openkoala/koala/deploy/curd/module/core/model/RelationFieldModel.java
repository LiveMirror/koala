/*
 * Copyright (c) OpenKoala 2011 All Rights Reserved
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
package org.openkoala.koala.deploy.curd.module.core.model;

import org.openkoala.koala.deploy.curd.module.core.FieldModel;
import org.openkoala.koala.deploy.curd.module.core.RelationModel;

/**
 * 类    名：RelationFieldModel.java
 *   
 * 功能描述：数据库关系型的FieldModel
 *  
 * 创建日期：2013-1-23上午11:32:31     
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
public class RelationFieldModel extends FieldModel {
    
    private RelationModel relationModel;

    /**
     * 
     */
    public RelationFieldModel() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param name
     * @param type
     */
    public RelationFieldModel(String name, String type) {
        super(name, type);
        // TODO Auto-generated constructor stub
    }

    public RelationModel getRelationModel() {
        return relationModel;
    }

    public void setRelationModel(RelationModel relationModel) {
        this.relationModel = relationModel;
    }
    
}
