package com.serviceapp.elk.transactionService.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TransferTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private UUID fromAccount;
	private UUID toAccount;
	private double amount;
	private Date transactionDate;
	private String transactionType; // e.g., "account-to-account"
	private String status;
	private boolean isReversed;
	private String reversalReason;

}
