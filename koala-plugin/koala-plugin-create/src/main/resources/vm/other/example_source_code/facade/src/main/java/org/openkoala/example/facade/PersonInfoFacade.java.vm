
package org.openkoala.example.facade;

import java.util.List;

import org.dayatang.querychannel.Page;
import org.openkoala.example.facade.dto.PersonInfoDTO;

public interface PersonInfoFacade {

	public PersonInfoDTO getPersonInfo(Long id);
	
	public PersonInfoDTO savePersonInfo(PersonInfoDTO personInfo);
	
	public void updatePersonInfo(PersonInfoDTO personInfo);
	
	public void removePersonInfo(Long id);
	
	public void removePersonInfos(Long[] ids);
	
	public List<PersonInfoDTO> findAllPersonInfo();
	
	public Page<PersonInfoDTO> pageQueryPersonInfo(PersonInfoDTO personInfo, int currentPage, int pageSize);
	

}

