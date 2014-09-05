
package org.openkoala.example.facade.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.example.application.PersonInfoApplication;
import org.openkoala.example.domain.PersonInfo;
import org.openkoala.example.facade.PersonInfoFacade;
import org.openkoala.example.facade.dto.PersonInfoDTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class PersonInfofacadeImpl implements PersonInfoFacade {

	@Inject
	private PersonInfoApplication personInfoApplication;
		
	private QueryChannelService queryChannel;

    private QueryChannelService getQueryChannelService(){
       if(queryChannel==null){
          queryChannel = InstanceFactory.getInstance(QueryChannelService.class,"queryChannel");
       }
     return queryChannel;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersonInfoDTO getPersonInfo(Long id) {
		PersonInfo personInfo = personInfoApplication.getPersonInfo(id);
		PersonInfoDTO dto = new PersonInfoDTO();
		return dto.personInfoEntityToDTO(personInfo);
	}
	
	public PersonInfoDTO savePersonInfo(PersonInfoDTO personInfoDTO) {
		PersonInfo personInfo = personInfoDTO.personInfoDTOToEntity(personInfoDTO);
		personInfo.save();
		personInfoDTO.setId((java.lang.Long)personInfo.getId());
		return personInfoDTO;
	}
	
	public void updatePersonInfo(PersonInfoDTO personInfoDTO) {
		//PersonInfo personInfo = PersonInfo.get(PersonInfo.class, personInfoDTO.getId());
		PersonInfo personInfo = personInfoDTO.personInfoDTOToEntity(personInfoDTO);
		personInfoApplication.updatePersonInfo(personInfo);
	}
	
	public void removePersonInfo(Long id) {
		this.removePersonInfos(new Long[] { id });
	}
	
	public void removePersonInfos(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			personInfoApplication.removePersonInfo(ids[i]);
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PersonInfoDTO> findAllPersonInfo() {
		List<PersonInfoDTO> list = new ArrayList<PersonInfoDTO>();		
		List<PersonInfo> all = personInfoApplication.findAllPersonInfo();
		for (PersonInfo personInfo : all) {
			PersonInfoDTO dto = new PersonInfoDTO();
			list.add(dto.personInfoEntityToDTO(personInfo));
		}
		return list;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Page<PersonInfoDTO> pageQueryPersonInfo(PersonInfoDTO queryVo, int currentPage, int pageSize) {
		List<PersonInfoDTO> result = new ArrayList<PersonInfoDTO>();
		List<Object> conditionVals = new ArrayList<Object>();
	   	StringBuilder jpql = new StringBuilder("select _personInfo from PersonInfo _personInfo   where 1=1 ");
	
	
	   	if (queryVo.getName() != null && !"".equals(queryVo.getName())) {
	   		jpql.append(" and _personInfo.name like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getName()));
	   	}		
	
	
	   	if (queryVo.getSex() != null && !"".equals(queryVo.getSex())) {
	   		jpql.append(" and _personInfo.sex like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getSex()));
	   	}		
	
	   	if (queryVo.getIdentityCardNumber() != null && !"".equals(queryVo.getIdentityCardNumber())) {
	   		jpql.append(" and _personInfo.identityCardNumber like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getIdentityCardNumber()));
	   	}		
	
	   	if (queryVo.getBirthday() != null) {
	   		jpql.append(" and _personInfo.birthday between ? and ? ");
	   		conditionVals.add(queryVo.getBirthday());
	   		conditionVals.add(queryVo.getBirthdayEnd());
	   	}	
	
	   	if (queryVo.getMarried() != null) {
		   	jpql.append(" and _personInfo.married is ?");
		   	conditionVals.add(queryVo.getMarried());
	   	}	
	
        Page<PersonInfo> pages = getQueryChannelService().createJpqlQuery(jpql.toString()).setParameters(conditionVals).setPage(currentPage, pageSize).pagedList();
        for (PersonInfo personInfo : pages.getData()) {
            PersonInfoDTO personInfoDTO = new PersonInfoDTO();
            
             // 将domain转成VO
            /*try {
            	personInfoDTO.personInfoEntityToDTO(personInfo);
            	BeanUtils.copyProperties(personInfoDTO, personInfo);
            } catch (Exception e) {
            	e.printStackTrace();
            } */
            
              result.add(personInfoDTO.personInfoEntityToDTO(personInfo));
        }
        return new Page<PersonInfoDTO>(pages.getPageIndex(), pages.getResultCount(), pageSize, result);
	}	
}
