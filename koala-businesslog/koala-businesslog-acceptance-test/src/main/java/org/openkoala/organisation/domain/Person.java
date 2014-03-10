package org.openkoala.organisation.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openkoala.organisation.IdNumberIsExistException;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 人
 * @author xmfang
 *
 */
@Entity
@Table(name = "persons")
public class Person extends OrganizationAbstractEntity {

	private static final long serialVersionUID = 4180083929142881138L;

	private String name;

	
	private Gender gender;
	
	
	private String idNumber;
	
	
	private String mobilePhone;
	
	
	private String familyPhone;
	
	private String email;
	
	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "id_number", unique = true)
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	@Enumerated(EnumType.STRING)
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Column(name = "mobile_phone")
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "family_phone")
	public String getFamilyPhone() {
		return familyPhone;
	}

	public void setFamilyPhone(String familyPhone) {
		this.familyPhone = familyPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public void save() {
		if (getId() == null) {
			checkIdNumberExist();
		} else {
			Person person = get(Person.class, getId());
			if (!person.getIdNumber().equals(idNumber)) {
				checkIdNumberExist();
			}
		}
		super.save();
	}
	
	public void checkIdNumberExist() {
		if (StringUtils.isBlank(idNumber)) {
			return;
		}
		if (isExistIdNumber(idNumber, new Date())) {
			throw new IdNumberIsExistException();
		}
	}
	
	public static boolean isExistIdNumber(String sn, Date date) {
		List<Person> parties = getRepository().createCriteriaQuery(Person.class)
				.eq("idNumber", sn).list();
		return parties.isEmpty() ? false : true;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(idNumber).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Person)) {
			return false;
		}
		Person that = (Person) other;
		return new EqualsBuilder().append(this.idNumber, that.idNumber)
				.isEquals();
	}

	@Override
	public String toString() {
		return name.toString();
	}

}
