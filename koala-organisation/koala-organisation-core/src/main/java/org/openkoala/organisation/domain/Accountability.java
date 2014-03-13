package org.openkoala.organisation.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.dayatang.utils.DateUtils;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({@NamedQuery(name = "Accountability.findAccountabilitiesByParty", 
	query = "select o from Accountability o where o.commissioner = :party or o.responsible = :party and o.fromDate <= :date and o.toDate > :date")})
public abstract class Accountability<C extends Party, R extends Party> extends OrganizationAbstractEntity {

	private static final long serialVersionUID = 3456398163374995470L;

	
	private C commissioner;

	
	private R responsible;

	
	private Date fromDate;

	
	private Date toDate;

	Accountability() {
	}

	public Accountability(C commissioner, R responsible, Date fromDate) {
		this.commissioner = commissioner;
		this.responsible = responsible;
		this.fromDate = fromDate;
		this.toDate = DateUtils.MAX_DATE;
	}

	@ManyToOne(targetEntity = Party.class, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "commissioner_id")
	public C getCommissioner() {
		return commissioner;
	}

	public void setCommissioner(C commissioner) {
		this.commissioner = commissioner;
	}

	@ManyToOne(targetEntity = Party.class, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "responsible_id")
	public R getResponsible() {
		return responsible;
	}

	public void setResponsible(R responsible) {
		this.responsible = responsible;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "from_date")
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "to_date")
	public Date getToDate() {
		return toDate;
	}

	public void terminate(Date date) {
		this.toDate = date;
		save();
	}

	@SuppressWarnings("rawtypes")
	public static <T extends Accountability> T getByCommissionerAndResponsible(Class<T> accountabilityClass, Party commissioner, Party responsible, Date date) {
		return getRepository().createCriteriaQuery(accountabilityClass)
				.eq("commissioner", commissioner)
				.eq("responsible", responsible)
				.le("fromDate", date)
				.gt("toDate", date).singleResult();
	}

	@SuppressWarnings("rawtypes")
	public static List<Accountability> findAccountabilitiesByParty(Party party, Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("party", party);
		params.put("date", date);
		return getRepository().createNamedQuery("Accountability.findAccountabilitiesByParty").addParameter("party", party).addParameter("date", date).list();
	}

}
