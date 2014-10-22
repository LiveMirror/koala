package org.openkoala.example.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.openkoala.koala.commons.domain.KoalaAbstractEntity;

@Entity
@Table(name="Person_Info")
public class PersonInfo extends KoalaAbstractEntity {

	private static final long serialVersionUID = -8117884889431440517L;
	
	//姓名	
	private String name;
	//年龄
	private Integer age;
	//性别
	private String sex;
	//身份证号码
	private String identityCardNumber;
	//出生日期	
	private Date birthday;
	//是否已婚
	private Boolean married;
	//收入
	private Double proceeds;
	
	public PersonInfo() {}

	public PersonInfo(String name, String sex,
			String identityCardNumber, Date birthday, Boolean married,
			Double proceeds) {
		super();
		this.name = name;
		this.sex = sex;
		this.identityCardNumber = identityCardNumber;
		this.birthday = birthday;
		this.married = married;
		this.proceeds = proceeds;
	}

	@Column(nullable=false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Transient
	public Integer getAge() {		
	   return getYear(new Date()) - getYear(birthday);
	}

	private int getYear(Date birthday) {
		return birthday.getYear();
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name="identity_card_number",nullable=false)
	public String getIdentityCardNumber() {
		return identityCardNumber;
	}

	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Boolean getMarried() {
		return married;
	}

	public void setMarried(Boolean married) {
		this.married = married;
	}

	public Double getProceeds() {
		return proceeds;
	}

	public void setProceeds(Double proceeds) {
		this.proceeds = proceeds;
	}

	public String[] businessKeys() {
		// TODO Auto-generated method stub
		return null;
	}	

}
