package com.serviceapp.elk.transactionService.request;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetails {
    
    private String accountNumber;
    private double balance;
}
