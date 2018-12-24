package com.revolut.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USER", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "ID" }),
		@javax.persistence.UniqueConstraint(columnNames = { "EMAIL" }) })
public class User implements Serializable {
	private static final long serialVersionUID = -1798070786993154676L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private long userId;
	@Column(name = "EMAIL", unique = true, nullable = false, length = 100)
	private String email;
	@Column(name = "FIRST_NAME", unique = false, nullable = false, length = 100)
	private String firstName;
	@Column(name = "LAST_NAME", unique = false, nullable = false, length = 100)
	private String lastName;
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	Set<Account> accounts;
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	Set<Beneficiary> beneficiaries;
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	Set<OwnTransfer> ownTransfers;

	public User() {
	}

	public User(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public User(String firstName, String lastName, String email, Set<Account> accounts, Set<Beneficiary> beneficiaries,
			Set<OwnTransfer> ownTransfers) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.accounts = accounts;
		this.beneficiaries = beneficiaries;
		this.ownTransfers = ownTransfers;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Set<Beneficiary> getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(Set<Beneficiary> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

	public Set<OwnTransfer> getOwnTransfers() {
		return ownTransfers;
	}

	public void setOwnTransfers(Set<OwnTransfer> ownTransfers) {
		this.ownTransfers = ownTransfers;
	}
}
