package org.openkoala.openci.web.controller.toolconfiguration;

import javax.inject.Inject;

import org.openkoala.openci.application.ToolConfigurationApplication;
import org.openkoala.openci.web.controller.BaseController;

public class ToolConfigurationBaseController extends BaseController {

	@Inject
	protected ToolConfigurationApplication toolConfigurationApplication;
	
}
