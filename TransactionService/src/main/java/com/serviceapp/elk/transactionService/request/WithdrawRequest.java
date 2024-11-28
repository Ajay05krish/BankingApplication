package com.serviceapp.elk.transactionService.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequest {
      
    private UUID accountNumber;
    private double balance;
   
      

    
}