package org.openkoala.bpm.processdyna.web.controller.processform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.openkoala.bpm.application.DynaProcessApplication;
import org.openkoala.bpm.application.ProcessFormOperApplication;
import org.openkoala.bpm.application.dto.DynaProcessFormDTO;
import org.openkoala.bpm.application.dto.DynaProcessTemplateDTO;
import org.openkoala.bpm.application.dto.ProcessDTO;
import org.openkoala.bpm.application.dto.SelectOptions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/processform")
public class FormMgrController {

	@Inject
	private ProcessFormOperApplication processFormOperApplication;
	
	@Inject 
	private DynaProcessApplication dynaProcessApplication;

	@RequestMapping("/list")
	public String listForms(DynaProcessFormDTO search, ModelMap modelMap) {
		// 初始化表单条件
		List<ProcessDTO> processes = processFormOperApplication.getActiveProcesses();
		modelMap.put("processes", processes);
		List<DynaProcessTemplateDTO> templates = processFormOperApplication.getActiveProcessTemplates();
		modelMap.put("templates", templates);
		List<SelectOptions> dataTypeList = processFormOperApplication.getDataTypeList();
		modelMap.put("dataTypes", dataTypeList);
		List<SelectOptions> validateRules = processFormOperApplication.getValidateRules();
		modelMap.put("validateRules", validateRules);

		return "processform/list";
	}
	
	
	@ResponseBody
	@RequestMapping("/getDataList")
	public Map<String, Object> getFormList(HttpServletRequest request,Integer page,int pagesize) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<DynaProcessFormDTO> pageObj = processFormOperApplication.queryDynaProcessFormsByPage(null, page, pagesize);
		dataMap.put("Rows", pageObj.getResult());
		dataMap.put("start", pageObj.getStart());
		dataMap.put("limit", pagesize);
		dataMap.put("Total", pageObj.getTotalCount());
		return dataMap;
	}


	@ResponseBody
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public Map<String, Object> createForm(HttpServletRequest request,String jsondata) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		DynaProcessFormDTO form = JSON.parseObject(jsondata, DynaProcessFormDTO.class);
		processFormOperApplication.createDynaProcessForm(form);
		dataMap.put("result", "OK");
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/get", method = { RequestMethod.GET })
	public Map<String, Object> getForm(HttpServletRequest request,long id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		DynaProcessFormDTO form = processFormOperApplication.getDynaProcessFormById(id);
		dataMap.put("result", form);
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public Map<String, Object> deleteForm(HttpServletRequest request,String id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String[] idStrings = id.split(",");
		Long[] ids = new Long[idStrings.length];
		for (int i = 0; i < idStrings.length; i++) {
			ids[i] = Long.parseLong(idStrings[i]);
		}
		processFormOperApplication.deleteDynaProcessFormById(ids);
		dataMap.put("result", "OK");
		return dataMap;
	}
	
	@ResponseBody
	@RequestMapping("/templatePreview")
	public Map<String, Object> templatePreview(Long formId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", dynaProcessApplication.packagingHtml(formId));
		return dataMap;
	}

}
