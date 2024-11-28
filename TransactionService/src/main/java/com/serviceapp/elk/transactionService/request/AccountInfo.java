package com.serviceapp.elk.transactionService.request;

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
}
