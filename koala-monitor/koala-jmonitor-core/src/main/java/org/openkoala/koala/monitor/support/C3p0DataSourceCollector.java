/*
 * Copyright (c) Koala 2012-2014 All Rights Reserved
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
package org.openkoala.koala.monitor.support;

import java.util.Date;

import javax.sql.DataSource;

import org.openkoala.koala.monitor.model.JdbcPoolStatusVo;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-7-4 下午3:15:37  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class C3p0DataSourceCollector implements Collector {

	
	private DataSource dataSoure;
	
	/**
	 * @param dataSoure
	 */
	public C3p0DataSourceCollector() {}



	public synchronized JdbcPoolStatusVo currentPoolStatus() {

		JdbcPoolStatusVo poolStatus = new JdbcPoolStatusVo();
		try {
			ComboPooledDataSource realDS = (ComboPooledDataSource)dataSoure;
			poolStatus.setDriverName(realDS.getDriverClass());
			poolStatus.setProvider(dataSoure.getClass().getName());
			poolStatus.setInitConnectionCount(realDS.getInitialPoolSize());
			poolStatus.setMaxConnectionCount(realDS.getMaxPoolSize());
			poolStatus.setMaxActiveTime(realDS.getMaxIdleTime());
		
			poolStatus.setErrorTip(null);
			poolStatus.setMaxOpenStatements(realDS.getMaxStatements());
			poolStatus.setSnapshotTime(new Date());
			poolStatus.setIdleConnectionCount(realDS.getNumIdleConnections());
			poolStatus.setActiveConnectionCount(realDS.getNumBusyConnections());
		} catch (Exception e) {
			poolStatus.setErrorTip("获取实时连接池信息失败");
		}
		
		return poolStatus;
	
	}

	@Override
	public void assignDataSource(DataSource dataSoure) {
		this.dataSoure = dataSoure;
	}
}
