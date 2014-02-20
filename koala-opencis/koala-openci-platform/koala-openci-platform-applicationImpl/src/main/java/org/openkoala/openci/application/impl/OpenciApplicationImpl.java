package org.openkoala.openci.application.impl;

import javax.inject.Named;

import org.dayatang.domain.AbstractEntity;
import org.openkoala.openci.EntityNullException;
import org.openkoala.openci.application.OpenciApplication;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional("transactionManager_opencis")
public class OpenciApplicationImpl implements OpenciApplication {

	public void saveEntity(AbstractEntity entity) {
		if (entity == null) {
			throw new EntityNullException();
		}
		entity.save();
	}

	public void removeEntity(AbstractEntity entity) {
		if (entity == null) {
			throw new EntityNullException();
		}
		entity.remove();
	}

}
