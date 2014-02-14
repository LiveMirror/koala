package org.openkoala.openci.pojo;

import org.openkoala.openci.CISClientNotInstanceException;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.opencis.api.CISClient;


public abstract class ToolConfigurationPojo {

	protected CISClient cisClient;

	protected boolean isInstance = false;

	public abstract void createCISClient(ToolConfiguration toolConfiguration);

	public CISClient getCISClient() {
		if (cisClient == null) {
			throw new CISClientNotInstanceException();
		}
		return cisClient;
	}
	
	public void destory() {
		cisClient = null;
		isInstance = false;
	}

	public boolean isInstance() {
		return isInstance;
	}

}
