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
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "OwnTransfer", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "ID" }) })
public class OwnTransfer implements Serializable {
	private static final long serialVersionUID = -1798070786993154676L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private long id;
	@Column(name = "FROM_ACCOUNT", unique = false, nullable = false, length = 100)
	private String fromAccount;
	@Column(name = "TO_ACCOUNT", unique = false, nullable = false, length = 100)
	private String toAccount;
	@Column(name = "AMOUNT", unique = false, nullable = false, length = 100)
	private Double amount;
	@Column(name = "TRANSFER_CURRENCY", unique = false, nullable = false, length = 3)
	private String transferCurrency;
	@Column(name = "FFROM_ACCOUNT_CURRENCY", unique = false, nullable = false, length = 3)
	private String fromAccountCurrency;
	@Column(name = "T0_ACCOUNT_CURRENCY", unique = false, nullable = false, length = 3)
	private String toAccountCurrency;
	@Column(name = "BENFICARY_NAME", unique = false, nullable = true, length = 100)
	private String benficaryName;
	@Column(name = "COMMENT", unique = false, nullable = true, length = 100)
	private String comment;
	@Column(name = "EXCHANGE_RATE", unique = false, nullable = false, length = 100)
	private Double exchangeRate;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	public OwnTransfer() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getFromAccountCurrency() {
		return fromAccountCurrency;
	}

	public void setFromAccountCurrency(String fromAccountCurrency) {
		this.fromAccountCurrency = fromAccountCurrency;
	}

	public String getToAccountCurrency() {
		return toAccountCurrency;
	}

	public void setToAccountCurrency(String toAccountCurrency) {
		this.toAccountCurrency = toAccountCurrency;
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

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTransferCurrency() {
		return transferCurrency;
	}

	public void setTransferCurrency(String transferCurrency) {
		this.transferCurrency = transferCurrency;
	}
}
