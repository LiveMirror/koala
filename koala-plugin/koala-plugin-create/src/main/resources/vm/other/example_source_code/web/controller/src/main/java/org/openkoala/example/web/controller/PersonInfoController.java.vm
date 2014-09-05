
package org.openkoala.example.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.openkoala.example.facade.PersonInfoFacade;
import org.openkoala.example.facade.dto.PersonInfoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.dayatang.querychannel.Page;



@Controller
@RequestMapping("/PersonInfo")
public class PersonInfoController {
		
	@Inject
	private PersonInfoFacade personInfoFacade;
	
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(PersonInfoDTO personInfoDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		personInfoFacade.savePersonInfo(personInfoDTO);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Map<String, Object> update(PersonInfoDTO personInfoDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		personInfoFacade.updatePersonInfo(personInfoDTO);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/pageJson")
	public Page pageJson(PersonInfoDTO personInfoDTO, @RequestParam int page, @RequestParam int pagesize) {
		Page<PersonInfoDTO> all = personInfoFacade.pageQueryPersonInfo(personInfoDTO, page, pagesize);
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
        personInfoFacade.removePersonInfos(idArrs);
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/get/{id}")
	public Map<String, Object> get(@PathVariable Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", personInfoFacade.getPersonInfo(id));
		return result;
	}
	
		
}
