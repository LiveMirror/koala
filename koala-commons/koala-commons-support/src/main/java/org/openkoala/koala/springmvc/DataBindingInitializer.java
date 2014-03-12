package org.openkoala.koala.springmvc;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class DataBindingInitializer implements WebBindingInitializer {

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		 binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}

}
