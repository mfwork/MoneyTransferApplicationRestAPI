package com.revolut.moneytransfer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionDTO {
	private long transactionId;
	private long userId;
	private String fromAccount;
	private String toAccount;
	private Double amount;
	private String transferCurrency;
	private String toAccountCurrency;
	private String fromAccountCurrency;
	private String benficaryName;
	private String comment;

	public TransactionDTO() {
	}

	@JsonCreator
	public TransactionDTO(@JsonProperty(required = false, value = "transactionId") long transactionId,
			@JsonProperty(required = true, value = "userId") long userId,
			@JsonProperty(required = true, value = "fromAccount") String fromAccount,
			@JsonProperty(required = true, value = "toAccount") String toAccount,
			@JsonProperty(required = true, value = "amount") double amount,
			@JsonProperty(required = true, value = "transferCurrency") String transferCurrency,
			@JsonProperty(required = false, value = "toAccountCurrency") String toAccountCurrency,
			@JsonProperty(required = false, value = "fromAccountCurrency") String fromAccountCurrency,
			@JsonProperty(required = false, value = "benficaryName") String benficaryName,
			@JsonProperty(required = false, value = "comment") String comment) {
		this.transactionId = transactionId;
		this.userId = userId;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = Double.valueOf(amount);
		this.transferCurrency = transferCurrency;
		this.toAccountCurrency = toAccountCurrency;
		this.benficaryName = benficaryName;
		this.comment = comment;
	}

	public TransactionDTO(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getToAccountCurrency() {
		return toAccountCurrency;
	}

	public void setToAccountCurrency(String toAccountCurrency) {
		this.toAccountCurrency = toAccountCurrency;
	}

	public String getFromAccountCurrency() {
		return fromAccountCurrency;
	}

	public void setFromAccountCurrency(String fromAccountCurrency) {
		this.fromAccountCurrency = fromAccountCurrency;
	}

	public String getBenficaryName() {
		return benficaryName;
	}

	public void setBenficaryName(String benficaryName) {
		this.benficaryName = benficaryName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTransferCurrency() {
		return transferCurrency;
	}

	public void setTransferCurrency(String transferCurrency) {
		this.transferCurrency = transferCurrency;
	}
}
