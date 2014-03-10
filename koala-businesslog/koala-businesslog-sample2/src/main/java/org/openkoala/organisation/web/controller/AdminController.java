package org.openkoala.organisation.web.controller;

import org.dayatang.domain.InstanceFactory;
import org.openkoala.businesslog.application.BusinessLogApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User: zjzhai
 * Date: 12/11/13
 * Time: 5:08 PM
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        BusinessLogApplication businessLogApplication = InstanceFactory.getInstance(BusinessLogApplication.class, "businessLogApplication");
        model.put("logs",
                businessLogApplication.findAllDefaultBusinessLog());

        return "admin";

    }
}
