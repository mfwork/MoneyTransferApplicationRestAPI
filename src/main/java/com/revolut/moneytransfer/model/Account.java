package com.revolut.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "ID" }) })
public class Account implements Serializable {
	private static final long serialVersionUID = -1798070786993154676L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private long id;
	@Column(name = "ACCOUNT_NUMBER", unique = true, nullable = false, length = 100)
	private String accountNumber;
	@Column(name = "ACCOUNT_CURRENCY", unique = false, nullable = false, length = 3)
	private String accountCurrency;
	@Column(name = "ACCOUNT_BALANCE", unique = false, nullable = false, length = 3)
	private double accountBalance;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	public Account() {
	}

	public Account(String accountNumber, String accountCurrency, double accountBalance) {
		this.accountNumber = accountNumber;
		this.accountCurrency = accountCurrency;
		this.accountBalance = accountBalance;
	}

	public Account(String accountNumber, String accountCurrency, double accountBalance, User user) {
		this.accountNumber = accountNumber;
		this.accountCurrency = accountCurrency;
		this.accountBalance = accountBalance;
		this.user = user;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountCurrency() {
		return accountCurrency;
	}

	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
}
