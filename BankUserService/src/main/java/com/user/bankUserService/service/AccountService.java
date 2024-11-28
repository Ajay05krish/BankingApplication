package com.user.bankUserService.service;

import java.util.List;
import java.util.UUID;

import com.user.bankUserService.entity.Account;
import com.user.bankUserService.request.DepositeRequest;
import com.user.bankUserService.request.TransactionRequest;
import com.user.bankUserService.request.WithdrawRequest;

import reactor.core.publisher.Mono;

/**
 * 
 */
public interface AccountService {

    /**
     * @param account
     * @return
     */
    Account saveAccount(Account account);

    /**
     * @param accountNumber
     * @return
     */
    Account getAccountById(UUID accountNumber);

    /**
     * @return
     */
    List<Account> getAllAccount();

    /**
     * @param accountNumber
     * @param updatedAccount
     * @return
     */
    DepositeRequest updateAccountBalance(UUID accountNumber, DepositeRequest updatedAccount);

    /**
     * @param transactionRequest
     * @return
     */
    Mono<TransactionRequest> saveTransaction(TransactionRequest transactionRequest);

    /**
     * @param accountNumber
     * @param updatedAccount
     * @return
     */
    WithdrawRequest withdrawFromAccount(UUID accountNumber, WithdrawRequest updatedAccount);

    /**
     * 
     */
    void closeVirtualThreadExecutor();
}
