package com.revolut.moneytransfer.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "ExchangeRate", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "CURRENCY" }) })
public class ExchangeRate implements Serializable {
	private static final long serialVersionUID = -1798070786993154676L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private long id;
	@Column(name = "CURRENCY", unique = true, nullable = false, length = 3)
	private String curreny;
	@Column(name = "sellingRate", nullable = false)
	private double sellingRate;
	@Column(name = "buyingRate", nullable = false)
	private double buyingRate;

	ExchangeRate() {
	}

	public ExchangeRate(String curreny, double sellingRate, double buyingRate) {
		this.curreny = curreny;
		this.sellingRate = sellingRate;
		this.buyingRate = buyingRate;
	}

	public long getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id.intValue();
	}

	public String getCurreny() {
		return curreny;
	}

	public void setCurreny(String curreny) {
		this.curreny = curreny;
	}

	public double getSellingRate() {
		return sellingRate;
	}

	public void setSellingRate(double sellingRate) {
		this.sellingRate = sellingRate;
	}

	public double getBuyingRate() {
		return buyingRate;
	}

	public void setBuyingRate(double buyingRate) {
		this.buyingRate = buyingRate;
	}
}
