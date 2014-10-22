package org.openkoala.gqc.controller.generalquery;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.dayatang.utils.Page;
import org.openkoala.gqc.facade.GqcFacade;
import org.openkoala.gqc.facade.dto.GeneralQueryDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 查询控制器
 * 
 * @author zyb
 * @since 2013-7-11 下午8:00:25
 */
@Controller
public class QueryController {

	/**
	 * 查询通道应用层接口实例
	 */
	@Inject
	private GqcFacade gqcFacade;

	@RequestMapping("/previewTemplate/{id}")
	public ModelAndView previewTemplate(@PathVariable Long id, Map<String, Object> map) {
		map.put("id", id);
		return new ModelAndView("gqc/previewTemplate");
	}

	/**
	 * 生成查询页面
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/query/{id}")
	public String queryPage(@PathVariable Long id, ModelMap modelMap) {
		GeneralQueryDTO generalQuery = gqcFacade.getQuerier(id);
		modelMap.addAttribute("gq", generalQuery);
		modelMap.addAttribute("gqId", id);
		return "query";
	}

	/**
	 * 生成查询页面
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/preview/{id}")
	public GeneralQueryDTO preview(@PathVariable Long id) {
		return gqcFacade.getQuerier(id);
/*		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("generalQuery", gqcFacade.getQuerier(id));
		return dataMap;*/
	}

	/**
	 * 分页查询数据
	 * 
	 * @param id
	 * @param page
	 * @param pagesize
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/search/{id}")
	public Page<Map<String, Object>> search(@PathVariable Long id, @RequestParam int page, @RequestParam int pagesize, HttpServletRequest request) {
		Map<String, Object[]> params = request.getParameterMap();
		return gqcFacade.queryWithQuerier(id, params, page, pagesize);
	}

}
