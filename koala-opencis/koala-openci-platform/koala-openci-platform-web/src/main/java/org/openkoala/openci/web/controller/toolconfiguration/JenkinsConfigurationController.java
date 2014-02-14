package org.openkoala.openci.web.controller.toolconfiguration;

import java.util.HashMap;
import java.util.Map;

import org.openkoala.openci.core.JenkinsConfiguration;
import org.openkoala.openci.web.dto.ResultDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/jenkinsconfiguration")
public class JenkinsConfigurationController extends ToolConfigurationBaseController {

	@ResponseBody
	@RequestMapping("/create")
	public ResultDto createJenkinsConfiguration(JenkinsConfiguration jenkinsConfiguration) {
		toolConfigurationApplication.createConfiguration(jenkinsConfiguration);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/update")
	public ResultDto updateJenkinsConfiguration(JenkinsConfiguration jenkinsConfiguration) {
		toolConfigurationApplication.updateConfiguration(jenkinsConfiguration);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/pagingquery")
	public Map<String, Object> pagingQuery(int page, int pagesize) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<JenkinsConfiguration> toolConfigurationPage = toolConfigurationApplication.pagingQeuryJenkinsConfigurations(page, pagesize);
		dataMap.put("Rows", toolConfigurationPage.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", toolConfigurationPage.getTotalCount());
		return dataMap;
	}
	
}
