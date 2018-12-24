package com.revolut.moneytransfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
	private long id;
	private String firstName;
	private String lastName;
	private String email;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserDTO() {
	}

	@com.fasterxml.jackson.annotation.JsonCreator
	public UserDTO(@JsonProperty(required = false, value = "id") long id,
			@JsonProperty(required = true, value = "firstName") String firstName,
			@JsonProperty(required = true, value = "lastName") String lastName,
			@JsonProperty(required = true, value = "email") String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
}
