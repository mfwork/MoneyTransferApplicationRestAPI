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
@Table(name = "LocalTransfer", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "ID" }) })
public class LocalTransfer implements Serializable {
	private static final long serialVersionUID = -1798070786993154676L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	public LocalTransfer() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
