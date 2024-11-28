package com.user.bankUserService.request;

import java.util.UUID;

import lombok.Data;

@Data
public class WithdrawRequest {

	private UUID accountNumber;
	private double balance;

}
