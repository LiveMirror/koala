package org.openkoala.openci.application.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.openci.application.DeveloperApplication;
import org.openkoala.openci.core.Developer;
import org.springframework.transaction.annotation.Transactional;

@Named("developerApplication")
@Transactional("transactionManager_opencis")
public class DeveloperApplicationImpl implements DeveloperApplication {

	@Inject
	private QueryChannelService queryChannel;
	
	public Page<Developer> pagingQeuryDevelopers(Developer example, int currentPage, int pagesize) {
		List<Object> conditionVals = new ArrayList<Object>();

		StringBuilder jpql = new StringBuilder("select _developer from Developer _developer where _developer.createDate <= ? and _developer.abolishDate > ?");
		Date now = new Date();
		conditionVals.add(now);
		conditionVals.add(now);

		if (!StringUtils.isBlank(example.getName())) {
			jpql.append(" and _developer.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", example.getName()));
		}
		if (!StringUtils.isBlank(example.getDeveloperId())) {
			jpql.append(" and _developer.developerId like ?");
			conditionVals.add(MessageFormat.format("%{0}%", example.getDeveloperId()));
		}
		if (!StringUtils.isBlank(example.getEmail())) {
			jpql.append(" and _developer.email like ?");
			conditionVals.add(MessageFormat.format("%{0}%", example.getEmail()));
		}

		return queryChannel.createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pagesize).pagedList();
	}

	public void createDeveloper(Developer developer) {
		developer.save();
	}

	public void updateDeveloperInfo(Developer developer) {
		developer.save();
	}

	public void abolishDeveloper(Developer developer) {
		developer.abolish(new Date());
	}

	public void abolishDevelopers(Collection<Developer> developers) {
		Date abolishDate = new Date();
		for (Developer developer : developers) {
			developer.abolish(abolishDate);
		}
	}

}
