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
package org.openkoala.koala.deploy.curd.module.pojo;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import forkoala.org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.curd.generator.VelocityContextUtils;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.core.model.RelationFieldModel;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;
import org.openkoala.koala.pojo.MavenProject;

/**
 * 类    名：ImplNewFile.java
 *   
 * 功能描述：具体功能做描述。	
 *  
 * 创建日期：2013-1-25下午2:33:06     
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
public class ImplNewFile extends NewFile {
    
    private EntityModel entityModel;

    private List<RelationFieldModel> reliations;
    
    private EntityViewUI entityViewUI;
    
    private static final String TEMPLATE_PATH = "templates/applicationImplTemplate.vm";

    /**
     * @param name
     * @param projects
     * @param type
     */
    public ImplNewFile(String name, List<MavenProject> projects, NewFileType type,EntityModel entityModel,EntityViewUI entityViewUI) {
        super(name, projects, type);
        this.entityModel = entityModel;
        this.entityViewUI = entityViewUI;
    }
    
    @Override
	public String getPath() {
     /*   String[] temparr = entityModel.getClassName().split("\\.");
      String lastPackageName = temparr[temparr.length - 2];*/ 
    	        String targetPath = MessageFormat.format("{0}/src/main/java/{1}/{2}.java", projectPath, //
                getPackageName().replaceAll("\\.", "/"), getName());

        return targetPath;
	}

    public EntityModel getEntityModel() {
        return entityModel;
    }

    public void setEntityModel(EntityModel entityModel) {
        this.entityModel = entityModel;
    }

    public List<RelationFieldModel> getReliations() {
        return reliations;
    }

    public void setReliations(List<RelationFieldModel> reliations) {
        this.reliations = reliations;
    }

    public EntityViewUI getEntityViewUI() {
        return entityViewUI;
    }

    public void setEntityViewUI(EntityViewUI entityViewUI) {
        this.entityViewUI = entityViewUI;
    }

    /*
     *@see org.openkoala.koala.deploy.curd.module.pojo.NewFile#process()
     */
    @Override
    public void process() {
        VelocityContext context = VelocityContextUtils.getVelocityContext();
        context.put("applicationClass", this);
        try {
            VelocityUtil.vmToFile(context, TEMPLATE_PATH, getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
