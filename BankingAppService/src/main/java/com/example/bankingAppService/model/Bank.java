package com.example.bankingAppService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Bank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bankId;
	private String name;
	private String location;
	private String bankIFSCCode;
	public Bank(int bankId, String name, String location, String bankIFSCCode) {
		super();
		this.bankId = bankId;
		this.name = name;
		this.location = location;
		this.bankIFSCCode = bankIFSCCode;
	}
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBankIFSCCode() {
		return bankIFSCCode;
	}
	public void setBankIFSCCode(String bankIFSCCode) {
		this.bankIFSCCode = bankIFSCCode;
	}
	
	

}
