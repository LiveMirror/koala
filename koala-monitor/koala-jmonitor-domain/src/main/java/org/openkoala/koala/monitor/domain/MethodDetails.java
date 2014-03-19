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
package org.openkoala.koala.monitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 功能描述：<br />
 *  
 * 创建日期：2013-6-7 上午9:56:29  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Entity
@Table(name = "KM_METHOD_DETAILS")
public class MethodDetails extends BaseMonitorDetails {

	private static final long serialVersionUID = -337605838333057656L;
	
	
	private String method;
	
	
	private boolean successed;
	
	
	protected String stackTracesDetails;

	
	@Column(name="METHOD")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name = "IS_SUCCESSED")
	public boolean isSuccessed() {
		return successed;
	}

	public void setSuccessed(boolean successed) {
		this.successed = successed;
	}

	@Lob
	@Column(name = "STACK_DETAILS")
	public String getStackTracesDetails() {
		return stackTracesDetails;
	}

	public void setStackTracesDetails(String stackTracesDetails) {
		this.stackTracesDetails = stackTracesDetails;
	}
}
