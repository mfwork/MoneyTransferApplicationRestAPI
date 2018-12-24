package com.revolut.moneytransfer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDTO {
	private long id;
	private String accountNumber;
	private String accountCurrency;
	private long userId;
	private double accountBalance;

	public AccountDTO() {
	}

	@JsonCreator
	public AccountDTO(@JsonProperty(required = false, value = "id") long id,
			@JsonProperty(required = true, value = "userId") long userId,
			@JsonProperty(required = true, value = "accountCurrency") String accountCurrency,
			@JsonProperty(required = false, value = "accountNumber") String accountNumber,
			@JsonProperty(required = false, value = "accountBalance") double accountBalance) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.userId = userId;
		this.accountCurrency = accountCurrency;
		this.accountBalance = accountBalance;
	}

	public AccountDTO(long userId, String accountCurrency, String accountNumber, Double accountBalance) {
		this.accountNumber = accountNumber;
		this.userId = userId;
		this.accountCurrency = accountCurrency;
		this.accountBalance = accountBalance;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String toString() {
		return "Account{id=" + id + "accountNumber" + accountNumber + ", accountCurrency='" + accountCurrency + '\''
				+ ", accountBalance=" + accountBalance + ", ownerID='" + userId + '\'' + '}';
	}
}
