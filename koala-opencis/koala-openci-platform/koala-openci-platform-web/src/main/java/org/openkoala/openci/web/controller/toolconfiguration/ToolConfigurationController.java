package org.openkoala.openci.web.controller.toolconfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.openci.core.ScmType;
import org.openkoala.openci.core.ToolConfiguration;
import org.openkoala.openci.core.ToolType;
import org.openkoala.openci.web.dto.ResultDto;
import org.openkoala.openci.web.dto.ToolConfigurationDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/toolconfiguration")
public class ToolConfigurationController extends ToolConfigurationBaseController {

	@ResponseBody
	@RequestMapping("/unusable/{toolConfigurationId}")
	public ResultDto setToolUnUsable(@PathVariable long toolConfigurationId) {
		ToolConfiguration toolConfiguration = ToolConfiguration.get(ToolConfiguration.class, toolConfigurationId);
		if (toolConfiguration == null) {
			return ResultDto.createFailure();
		}
		toolConfigurationApplication.setToolUnusabled(toolConfiguration);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/get-all-usable")
	public List<ToolConfigurationDto> getAllUsable() {
		return ToolConfigurationDto.transform(toolConfigurationApplication.getAllUsable());
	}

	@ResponseBody
	@RequestMapping("/can-connect/{toolConfigurationId}")
	public ResultDto canConnect(@PathVariable long toolConfigurationId) {
		ToolConfiguration toolConfiguration = ToolConfiguration.get(ToolConfiguration.class, toolConfigurationId);
		if (toolConfiguration == null) {
			return ResultDto.createFailure();
		}
		boolean result = toolConfigurationApplication.canConnect(toolConfiguration);
		return new ResultDto(result);
	}

	@ResponseBody
    @RequestMapping("/get-tool-type")
	public Map<String, Object> getToolType() {
		Map<String, Object> toolTypes = new HashMap<String, Object>();
		for (ToolType each : ToolType.values()) {
			toolTypes.put(each.toString(), each);
		}
		return toolTypes;
	}
	
	@ResponseBody
    @RequestMapping("/get-scm-type")
	public Map<String, Object> getScmType() {
		Map<String, Object> scmTypes = new HashMap<String, Object>();
		for (ScmType each : ScmType.values()) {
			scmTypes.put(each.toString(), each);
		}
		return scmTypes;
	}
	
}
