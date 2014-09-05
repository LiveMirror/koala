package org.openkoala.example.facade.dto;

import java.util.Date;
import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openkoala.example.domain.PersonInfo;
import org.openkoala.koala.springmvc.JsonDateSerializer;


public class PersonInfoDTO implements Serializable {

	private static final long serialVersionUID = 1428864474382580128L;


	private Long id;

		
	private Double proceeds;
	
		
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthdayEnd;
		
	private String sex;
	
	private Boolean married;
	
    private String name;
	
		
	private String identityCardNumber;
	

	public void setProceeds(Double proceeds) { 
		this.proceeds = proceeds;
	}

	public Double getProceeds() {
		return this.proceeds;
	}
	
			
		

	public void setBirthday(Date birthday) { 
		this.birthday = birthday;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getBirthday() {
		return this.birthday;
	}
	
	public void setBirthdayEnd(Date birthdayEnd) { 
		this.birthdayEnd = birthdayEnd;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getBirthdayEnd() {
		return this.birthdayEnd;
	}
			
		

	public void setSex(String sex) { 
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
	}
	
			
		

	public void setMarried(Boolean married) { 
		this.married = married;
	}

	public Boolean getMarried() {
		return this.married;
	}
	
			
		

	public void setName(String name) { 
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
			
		

	public void setIdentityCardNumber(String identityCardNumber) { 
		this.identityCardNumber = identityCardNumber;
	}

	public String getIdentityCardNumber() {
		return this.identityCardNumber;
	}
	

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public  PersonInfoDTO personInfoEntityToDTO(PersonInfo personInfo){
			PersonInfoDTO result = new PersonInfoDTO();
			result.setBirthday(personInfo.getBirthday());
			result.setId(personInfo.getId());
			result.setIdentityCardNumber(personInfo.getIdentityCardNumber());
			result.setMarried(personInfo.getMarried());
			result.setName(personInfo.getName());
			result.setProceeds(personInfo.getProceeds());
			result.setSex(personInfo.getSex());		
			return result;
		}
		
	    public PersonInfo personInfoDTOToEntity(PersonInfoDTO personInfoDTO){
			PersonInfo result = new PersonInfo();
			result.setBirthday(personInfoDTO.getBirthday());
			result.setId(personInfoDTO.getId());
			result.setIdentityCardNumber(personInfoDTO.getIdentityCardNumber());
			result.setMarried(personInfoDTO.getMarried());
			result.setName(personInfoDTO.getName());
			result.setProceeds(personInfoDTO.getProceeds());
			result.setSex(personInfoDTO.getSex());		
			return result;
		}		

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonInfoDTO other = (PersonInfoDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}