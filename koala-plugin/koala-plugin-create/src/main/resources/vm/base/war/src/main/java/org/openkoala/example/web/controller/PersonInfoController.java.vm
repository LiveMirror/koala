package org.openkoala.example.web.controller;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.dayatang.querychannel.Page;
import org.openkoala.example.application.PersonInfoApplication;
import org.openkoala.example.application.dto.PersonInfoDTO;

@Controller
@RequestMapping("/PersonInfo")
public class PersonInfoController {
		
	@Inject
	private PersonInfoApplication personInfoApplication;
	
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(PersonInfoDTO personInfoDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		personInfoApplication.savePersonInfo(personInfoDTO);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(PersonInfoDTO personInfoDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		personInfoApplication.updatePersonInfo(personInfoDTO);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/pageJson")
	public Page pageJson(PersonInfoDTO personInfoDTO, @RequestParam int page, @RequestParam int pagesize) {
		Page<PersonInfoDTO> all = personInfoApplication.pageQueryPersonInfo(personInfoDTO, page, pagesize);
		return all;
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public Map<String, Object> delete(@RequestParam String ids) {
		Map<String, Object> result = new HashMap<String, Object>();
		String[] value = ids.split(",");
        Long[] idArrs = new Long[value.length];
        for (int i = 0; i < value.length; i ++) {
        	
        	        					idArrs[i] = Long.parseLong(value[i]);
						        	
        }
        personInfoApplication.removePersonInfos(idArrs);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/get/{id}")
	public Map<String, Object> get(@PathVariable Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", personInfoApplication.getPersonInfo(id));
		return result;
	}
	
		
}
