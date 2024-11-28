package com.serviceapp.elk.transactionService.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.PathVariable;

import com.serviceapp.elk.transactionService.model.TransactionDetails;
import com.serviceapp.elk.transactionService.model.TransferTransaction;
import com.serviceapp.elk.transactionService.request.TransactionReversalRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

    /**
     * @param fromAccount
     * @param toAccount
     * @param amount
     * @return
     */
    CompletableFuture<Object> transfer(UUID fromAccount, UUID toAccount, double amount);

    /**
     * @param accountNumber
     * @return
     */
    List<TransferTransaction> getTransactionHistory(UUID accountNumber);

    /**
     * @param transactionId
     * @return
     */
    TransferTransaction findTransactionById(Long transactionId);

     /**
     * @param reversalRequest
     * @return
     */
    Mono<Void> reverseTransaction(TransactionReversalRequest reversalRequest);

    /**
     * @param transactionDetails
     * @return
     */
    TransactionDetails saveTransaction(TransactionDetails transactionDetails);

    /**
     * @param accountNumber
     * @return
     */
    List<TransactionDetails> statement(UUID accountNumber);
}
