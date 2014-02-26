package org.openkoala.openci.web.controller.toolconfiguration;

import java.util.HashMap;
import java.util.Map;

import org.dayatang.querychannel.Page;
import org.openkoala.openci.core.GitConfiguration;
import org.openkoala.openci.web.dto.ResultDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/gitconfiguration")
public class GitConfigurationController extends ToolConfigurationBaseController {

	@ResponseBody
	@RequestMapping("/create")
	public ResultDto createGitConfiguration(GitConfiguration gitConfiguration) {
		toolConfigurationApplication.createConfiguration(gitConfiguration);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/update")
	public ResultDto updateGitConfiguration(GitConfiguration gitConfiguration) {
		toolConfigurationApplication.updateConfiguration(gitConfiguration);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/pagingquery")
	public Map<String, Object> pagingQuery(int page, int pagesize) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<GitConfiguration> toolConfigurationPage = toolConfigurationApplication.pagingQeuryGitConfigurations(page, pagesize);
		dataMap.put("Rows", toolConfigurationPage.getData());
		dataMap.put("start", toolConfigurationPage.getStart());
		dataMap.put("limit", pagesize);
		dataMap.put("Total", toolConfigurationPage.getResultCount());
		return dataMap;
	}
	
}
