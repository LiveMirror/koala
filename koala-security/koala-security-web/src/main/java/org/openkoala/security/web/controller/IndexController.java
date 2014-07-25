package org.openkoala.security.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class IndexController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/login")
	public String login() {
		LOGGER.info("into login page.");
		return "login";
	}
}
