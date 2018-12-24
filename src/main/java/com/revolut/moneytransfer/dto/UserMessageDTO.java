package com.revolut.moneytransfer.dto;

public class UserMessageDTO {
	private String message;

	public UserMessageDTO() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserMessageDTO(String message) {
		this.message = message;
	}
}
