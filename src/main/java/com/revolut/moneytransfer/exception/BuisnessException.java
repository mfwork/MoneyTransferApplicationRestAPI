package com.revolut.moneytransfer.exception;

public class BuisnessException extends Exception {
	private static final long serialVersionUID = 1L;

	public BuisnessException(String msg) {
		super(msg);
	}

	public BuisnessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
