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
public class TransactionDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;
	private UUID accountNumber;
	private String TransactionType;
	private Date transactionDate;
	private double amount;
	private String status;
}
