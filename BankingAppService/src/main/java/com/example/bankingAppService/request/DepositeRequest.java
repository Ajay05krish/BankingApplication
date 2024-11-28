package com.example.bankingAppService.request;

import java.util.UUID;
import lombok.Data;


@Data
public class DepositeRequest {
    

   
    private UUID accountNumber;
    private double balance;
	public DepositeRequest(UUID accountNumber, double balance) {
		super();
		this.accountNumber = accountNumber;
		this.balance = balance;
	}
	public UUID getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(UUID accountNumber) {
		this.accountNumber = accountNumber;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
    
    
 
    
}
