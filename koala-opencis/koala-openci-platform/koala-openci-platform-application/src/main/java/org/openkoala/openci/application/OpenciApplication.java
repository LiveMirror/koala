package org.openkoala.openci.application;

import com.dayatang.domain.AbstractEntity;

public interface OpenciApplication {

	void saveEntity(AbstractEntity entity);

    void removeEntity(AbstractEntity entity);
	
}
