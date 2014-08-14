package org.openkoala.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class IndexController {

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/unauthorized")
	public String unauthorized() {
		return "/errors/unauthorized";
	}
}
