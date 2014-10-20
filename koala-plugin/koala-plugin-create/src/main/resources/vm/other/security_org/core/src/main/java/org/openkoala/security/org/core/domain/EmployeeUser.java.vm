package org.openkoala.security.org.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.security.core.domain.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("EMPLOYEE_USER")
public class EmployeeUser extends User {

	private static final long serialVersionUID = 969976616338089081L;

	@OneToOne
	@JoinColumn(name = "EMPLOYEE_ID")
	private Employee employee;

	protected EmployeeUser() {
    }

	public EmployeeUser(String name,String userAccount) {
		super(name,userAccount);
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

    @Override
    public String[] businessKeys() {
        return super.businessKeys();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)//
                .append(getId())//
                .append(getUserAccount())//
                .append(getEmail())//
                .append(getTelePhone())//
                .append(getName())//
                .append(getEmployee())//
                .build();
    }
}
