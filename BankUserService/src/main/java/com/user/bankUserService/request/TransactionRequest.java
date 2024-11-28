package com.user.bankUserService.request;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class TransactionRequest {

	private UUID accountNumber;
	private String TransactionType;
	private Date transactionDate;
	private double amount;
	private String status;

}
