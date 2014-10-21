
package org.openkoala.example.application.impl;

import java.util.List;

import javax.inject.Named;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.openkoala.example.application.PersonInfoApplication;
import org.openkoala.example.domain.PersonInfo;

@Named
@Transactional
public class PersonInfoApplicationImpl implements PersonInfoApplication {

	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersonInfo getPersonInfo(Long id) {
		return PersonInfo.load(PersonInfo.class, id);
	}
	
	public void updatePersonInfo(PersonInfo personInfo) {
		personInfo.save();
	}
	
	public void removePersonInfo(Long id) {
		this.removePersonInfos(new Long[] { id });
	}
	
	public void removePersonInfos(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			PersonInfo personInfo = PersonInfo.load(PersonInfo.class, ids[i]);
			personInfo.remove();
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PersonInfo> findAllPersonInfo() {
		return PersonInfo.findAll(PersonInfo.class);
	}

	public PersonInfo savePersonInfo(PersonInfo personInfo) {
		personInfo.save();
		return personInfo;
	}
	
	
	
}
