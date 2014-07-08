package org.openkoala.example.application.impl;

import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.example.application.PersonInfoApplication;
import org.openkoala.example.application.dto.PersonInfoDTO;
import org.openkoala.example.domain.PersonInfo;

@Named
@Transactional
public class PersonInfoApplicationImpl implements PersonInfoApplication {


	private QueryChannelService queryChannel;

    private QueryChannelService getQueryChannelService(){
       if(queryChannel==null){
          queryChannel = InstanceFactory.getInstance(QueryChannelService.class,"queryChannel");
       }
     return queryChannel;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersonInfoDTO getPersonInfo(Long id) {
		PersonInfo personInfo = PersonInfo.load(PersonInfo.class, id);
		PersonInfoDTO personInfoDTO = new PersonInfoDTO();
		// 将domain转成VO
		try {
			BeanUtils.copyProperties(personInfoDTO, personInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		personInfoDTO.setId((java.lang.Long)personInfo.getId());
		return personInfoDTO;
	}
	
	public PersonInfoDTO savePersonInfo(PersonInfoDTO personInfoDTO) {
		PersonInfo personInfo = new PersonInfo();
		try {
        	BeanUtils.copyProperties(personInfo, personInfoDTO);
        } catch (Exception e) {
        	e.printStackTrace();
        }
		personInfo.save();
		personInfoDTO.setId((java.lang.Long)personInfo.getId());
		return personInfoDTO;
	}
	
	public void updatePersonInfo(PersonInfoDTO personInfoDTO) {
		PersonInfo personInfo = PersonInfo.get(PersonInfo.class, personInfoDTO.getId());
		// 设置要更新的值
		try {
			BeanUtils.copyProperties(personInfo, personInfoDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public List<PersonInfoDTO> findAllPersonInfo() {
		List<PersonInfoDTO> list = new ArrayList<PersonInfoDTO>();
		List<PersonInfo> all = PersonInfo.findAll(PersonInfo.class);
		for (PersonInfo personInfo : all) {
			PersonInfoDTO personInfoDTO = new PersonInfoDTO();
			// 将domain转成VO
			try {
				BeanUtils.copyProperties(personInfoDTO, personInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(personInfoDTO);
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
            try {
            	BeanUtils.copyProperties(personInfoDTO, personInfo);
            } catch (Exception e) {
            	e.printStackTrace();
            } 
            
                                                                                                            result.add(personInfoDTO);
        }
        return new Page<PersonInfoDTO>(pages.getPageIndex(), pages.getResultCount(), pageSize, result);
	}
	
	
}
