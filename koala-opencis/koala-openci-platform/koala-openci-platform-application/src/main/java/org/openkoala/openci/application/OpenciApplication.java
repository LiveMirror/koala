package org.openkoala.openci.application;

import org.dayatang.domain.AbstractEntity;


public interface OpenciApplication {

	void saveEntity(AbstractEntity entity);

    void removeEntity(AbstractEntity entity);
	
}
