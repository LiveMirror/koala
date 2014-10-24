package org.openkoala.organisation.application.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.core.domain.OrganizationAbstractEntity;
import org.openkoala.organisation.core.domain.Party;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional(value="transactionManager_org")
public class BaseApplicationImpl implements BaseApplication {

	
	public void saveParty(Party party) {
		party.save();
	}

	
	public void updateParty(Party party) {
		party.save();
	}

	
	public void terminateParty(Party party) {
		party.terminate(new Date());
	}

	
	public <T extends OrganizationAbstractEntity> T getEntity(Class<T> clazz, Long id) {
		return OrganizationAbstractEntity.get(clazz, id);
	}

	
	public <T extends Party> void terminateParties(Set<T> parties) {
		for (T party : parties) {
			party.terminate(new Date());
		}
	}

	
	public <T extends Party> List<T> findAll(Class<T> clazz) {
		return T.findAll(clazz, new Date());
	}
	
}
