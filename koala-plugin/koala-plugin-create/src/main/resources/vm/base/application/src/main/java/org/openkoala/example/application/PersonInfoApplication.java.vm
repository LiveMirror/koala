
package org.openkoala.example.application;

import java.util.List;
import org.dayatang.querychannel.Page;
import org.openkoala.example.application.dto.PersonInfoDTO;

public interface PersonInfoApplication {

	public PersonInfoDTO getPersonInfo(Long id);
	
	public PersonInfoDTO savePersonInfo(PersonInfoDTO personInfo);
	
	public void updatePersonInfo(PersonInfoDTO personInfo);
	
	public void removePersonInfo(Long id);
	
	public void removePersonInfos(Long[] ids);
	
	public List<PersonInfoDTO> findAllPersonInfo();
	
	public Page<PersonInfoDTO> pageQueryPersonInfo(PersonInfoDTO personInfo, int currentPage, int pageSize);
	

}

