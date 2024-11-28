package com.user.bankUserService.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "accounts")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	UUID accountNumber;
	String accountHolderName;
	String accountType;

	@Column(name = "pan_card_number")
	String panCardNumber;
	String address;
	double balance;
	public Account(UUID accountNumber, String accountHolderName, String accountType, String panCardNumber,
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
