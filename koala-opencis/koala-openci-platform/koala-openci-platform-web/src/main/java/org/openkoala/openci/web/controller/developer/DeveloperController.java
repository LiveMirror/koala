package org.openkoala.openci.web.controller.developer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.openci.application.DeveloperApplication;
import org.openkoala.openci.core.Developer;
import org.openkoala.openci.web.controller.BaseController;
import org.openkoala.openci.web.dto.ResultDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/developer")
public class DeveloperController extends BaseController {

	@Inject
	private DeveloperApplication developerApplication;
	
	@ResponseBody
    @RequestMapping("/pagingquery")
	public Map<String, Object> pagingQuery(int page, int pagesize, Developer example) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<Developer> developerPage = developerApplication.pagingQeuryDevelopers(example, page, pagesize);
		
		dataMap.put("Rows", developerPage.getData());
		dataMap.put("start", developerPage.getStart());
		dataMap.put("limit", pagesize);
		dataMap.put("Total", developerPage.getResultCount());
		return dataMap;
	}
	
	@ResponseBody
    @RequestMapping("/create")
	public ResultDto createDeveloper(Developer developer) {
		developerApplication.createDeveloper(developer);
		return ResultDto.createSuccess();
	}
	
	@ResponseBody
    @RequestMapping("/update")
	public ResultDto updateDeveloper(Developer developer) {
		developerApplication.updateDeveloperInfo(developer);
		return ResultDto.createSuccess();
	}
	
	@ResponseBody
    @RequestMapping("/abolish")
	public ResultDto abolishDeveloper(Developer developer) {
		developerApplication.abolishDeveloper(developer);
		return ResultDto.createSuccess();
	}
	
	@ResponseBody
    @RequestMapping(value = "/abolish_developers", method = RequestMethod.POST, consumes = "application/json")
	public ResultDto abolishDevelopers(@RequestBody Developer[] developers) {
		developerApplication.abolishDevelopers(Arrays.asList(developers));
		return ResultDto.createSuccess();
	}
	
}
