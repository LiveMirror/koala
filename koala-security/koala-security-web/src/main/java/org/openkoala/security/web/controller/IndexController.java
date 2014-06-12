package org.openkoala.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	
<<<<<<< HEAD
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "/login";
=======
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
>>>>>>> origin/master
	}

}
