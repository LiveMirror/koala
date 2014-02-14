package org.openkoala.openci.web.controller.toolconfiguration;

import java.util.HashMap;
import java.util.Map;

import org.openkoala.openci.core.SvnConfiguration;
import org.openkoala.openci.web.dto.ResultDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/svnconfiguration")
public class SvnConfigurationController extends ToolConfigurationBaseController {

	@ResponseBody
	@RequestMapping("/create")
	public ResultDto createSvnConfiguration(SvnConfiguration svnConfiguration) {
		toolConfigurationApplication.createConfiguration(svnConfiguration);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/update")
	public ResultDto updateSvnConfiguration(SvnConfiguration svnConfiguration) {
		toolConfigurationApplication.updateConfiguration(svnConfiguration);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/pagingquery")
	public Map<String, Object> pagingQuery(int page, int pagesize) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<SvnConfiguration> toolConfigurationPage = toolConfigurationApplication.pagingQeurySvnConfigurations(page, pagesize);
		dataMap.put("Rows", toolConfigurationPage.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", toolConfigurationPage.getTotalCount());
		return dataMap;
	}
	
}
