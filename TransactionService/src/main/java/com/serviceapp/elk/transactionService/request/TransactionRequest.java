package com.serviceapp.elk.transactionService.request;

import java.util.UUID;

import lombok.Data;

@Data
public class TransactionRequest {
    private UUID fromAccount;
    private UUID toAccount;
    private double amount;
}
