package com.example.bankingAppService.model;

import java.util.UUID;

import lombok.Data;

@Data
public class AccountInfo {

	private UUID accountNumber;
	private String accountHolderName;
	private String accountType;
	private String panCardNumber;
	private String address;
	private double balance;
	public AccountInfo(UUID accountNumber, String accountHolderName, String accountType, String panCardNumber,
			String address, double balance) {
		super();
		this.accountNumber = accountNumber;
		this.accountHolderName = accountHolderName;
		this.accountType = accountType;
		this.panCardNumber = panCardNumber;
		this.address = address;
		this.balance = balance;
	}
	public UUID getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(UUID accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountHolderName() {
		return accountHolderName;
	}
	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getPanCardNumber() {
		return panCardNumber;
	}
	public void setPanCardNumber(String panCardNumber) {
		this.panCardNumber = panCardNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	

}
