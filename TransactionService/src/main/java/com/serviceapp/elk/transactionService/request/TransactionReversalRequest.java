package com.serviceapp.elk.transactionService.request;

import lombok.Data;

@Data
public class TransactionReversalRequest {
    private Long transactionId;
    private String reason;


}