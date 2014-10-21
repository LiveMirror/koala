package org.openkoala.koala.deploy.curd.module.ui.model;

import java.util.List;

import org.openkoala.koala.deploy.curd.module.ui.Model;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;
/**
 * 
 * 类    名：AddModel.java
 *   
 * 功能描述：新增视图
 *  
 * 创建日期：2013-1-18下午5:12:21     
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
public class AddModel implements Model{
    
    private List<UIWidget> views;

    /**
     * @param views
     */
    public AddModel(List<UIWidget> views) {
        super();
        this.views = views;
    }

    public List<UIWidget> getViews() {
        return views;
    }

    public void setViews(List<UIWidget> views) {
        this.views = views;
    }
    
}
