package com.user.bankUserService.exception;

public class InsufficientFundsException extends RuntimeException {

	public InsufficientFundsException(String message) {
		super(message);
	}

}
