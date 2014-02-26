package org.openkoala.openci.web.controller.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.openci.application.RoleApplication;
import org.openkoala.openci.core.Role;
import org.openkoala.openci.web.controller.BaseController;
import org.openkoala.openci.web.dto.ResultDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Inject
	private RoleApplication roleApplication;

	@ResponseBody
	@RequestMapping("/create")
	public ResultDto createRole(Role role) {
		roleApplication.createRole(role);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/update")
	public ResultDto updateRole(Role role) {
		roleApplication.updateRole(role);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/abolish")
	public ResultDto abolishRole(Role role) {
		roleApplication.abolishRole(role);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/abolishs", method = RequestMethod.POST, consumes = "application/json")
	public ResultDto abolishRole(@RequestBody Role[] roles) {
		roleApplication.abolishRole(roles);
		return ResultDto.createSuccess();
	}

	@ResponseBody
	@RequestMapping("/pagingquery")
	public Map<String, Object> pagingQuery(int page, int pagesize) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<Role> rolePage = roleApplication.pagingQeuryRoles(page, pagesize);

		dataMap.put("Rows", rolePage.getData());
		dataMap.put("start", rolePage.getStart());
		dataMap.put("limit", pagesize);
		dataMap.put("Total", rolePage.getResultCount());
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/findall")
	public List<Role> findAll() {
		return roleApplication.findAll();
	}

}
