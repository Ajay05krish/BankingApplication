package com.serviceapp.elk.transactionService.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.serviceapp.elk.transactionService.constants.ServiceConstants;
import com.serviceapp.elk.transactionService.model.TransactionDetails;
import com.serviceapp.elk.transactionService.model.TransferTransaction;
import com.serviceapp.elk.transactionService.repo.TransactionDetailsRepository;
import com.serviceapp.elk.transactionService.repo.TransactionSQLRepo;
import com.serviceapp.elk.transactionService.request.AccountDetails;
import com.serviceapp.elk.transactionService.request.DepositeRequest;
import com.serviceapp.elk.transactionService.request.TransactionReversalRequest;
import com.serviceapp.elk.transactionService.request.WithdrawRequest;


import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionService interface.
 * This service handles operations such as transfer, transaction history retrieval,
 * and transaction reversal.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionSQLRepo transactionSQLRepo;

    @Autowired
    private TransactionDetailsRepository transactionDetailRepo;

    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * Transfers an amount from one account to another.
     * This operation includes a withdrawal from the source account and a deposit to the destination account.
     *
     * @param fromAccount The account number from which the amount will be withdrawn.
     * @param toAccount The account number to which the amount will be deposited.
     * @param amount The amount to transfer.
     * @return A CompletableFuture that signifies the completion of the transfer.
     */
    @Override
    public CompletableFuture transfer(UUID fromAccount, UUID toAccount, double amount) {
        WithdrawRequest withdrawRequest = new WithdrawRequest(fromAccount, amount);
        DepositeRequest depositRequest = new DepositeRequest(toAccount, amount);

        TransferTransaction transaction = new TransferTransaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionType("account-to-account");
        transaction.setStatus("pending");

        logger.info("Initiating transfer of {} from {} to {}", amount, fromAccount, toAccount);

        CompletableFuture<Void> withdrawFuture = CompletableFuture.runAsync(() -> {
            try {
                webClientBuilder.build()
                        .put()
                        .uri(ServiceConstants.WITHDRAW_ENDPOINT, fromAccount)
                        .bodyValue(withdrawRequest)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
                logger.info("Withdrawal from account {} successful", fromAccount);
            } catch (Exception e) {
                logger.error("Withdrawal failed for account {}: {}", fromAccount, e.getMessage());
                throw new RuntimeException("Withdrawal failed", e);
            }
        });

        CompletableFuture<Void> depositFuture = CompletableFuture.runAsync(() -> {
            try {
                webClientBuilder.build()
                        .put()
                        .uri(ServiceConstants.DEPOSIT_ENDPOINT, toAccount)
                        .bodyValue(depositRequest)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
                logger.info("Deposit to account {} successful", toAccount);
            } catch (Exception e) {
                logger.error("Deposit failed for account {}: {}", toAccount, e.getMessage());
                throw new RuntimeException("Deposit failed", e);
            }
        });

        return withdrawFuture.thenCombine(depositFuture, (withdrawResult, depositResult) -> {
            transaction.setStatus("success");
            transactionSQLRepo.save(transaction);
            logger.info("Transfer of {} from account {} to account {} successful", amount, fromAccount, toAccount);
            return null;
        }).exceptionally(ex -> {
            transaction.setStatus("failed");
            transactionSQLRepo.save(transaction);
            logger.error("Transfer failed for transaction from {} to {}: {}", fromAccount, toAccount, ex.getMessage());
            throw new RuntimeException("Transfer failed", ex);
        });
    }

    /**
     * Retrieves the transaction history for a specific account.
     *
     * @param accountNumber The account number for which to fetch the transaction history.
     * @return A list of TransferTransaction objects representing the transaction history.
     */
    @Override
    public List<TransferTransaction> getTransactionHistory(UUID accountNumber) {
        return transactionSQLRepo.findByFromAccountOrToAccount(accountNumber, accountNumber);
    }

    /**
     * Finds a transaction by its ID.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @return The TransferTransaction object for the given transaction ID.
     */
    @Override
    public TransferTransaction findTransactionById(Long transactionId) {
        return transactionSQLRepo.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    /**
     * Reverses a transaction based on a given request.
     *
     * @param reversalRequest The request object containing the transaction ID and reversal details.
     * @return A Mono representing the completion of the reversal operation.
     */
    @Transactional
    @Override
    public Mono<Void> reverseTransaction(TransactionReversalRequest reversalRequest) {
        return Mono.fromRunnable(() -> {
            try {
                TransferTransaction originalTransaction = transactionSQLRepo.findById(reversalRequest.getTransactionId())
                        .orElseThrow(() -> new RuntimeException("Transaction not found"));

                if (originalTransaction.isReversed()) {
                    throw new RuntimeException("Transaction already reversed");
                }

                CompletableFuture<AccountDetails> fromAccountFuture = CompletableFuture.supplyAsync(() -> {
                    return webClientBuilder.build()
                            .get()
                            .uri(ServiceConstants.GET_ACCOUNT_DETAILS_ENDPOINT, originalTransaction.getFromAccount())
                            .retrieve()
                            .bodyToMono(AccountDetails.class)
                            .block();
                });

                CompletableFuture<AccountDetails> toAccountFuture = CompletableFuture.supplyAsync(() -> {
                    return webClientBuilder.build()
                            .get()
                            .uri(ServiceConstants.GET_ACCOUNT_DETAILS_ENDPOINT, originalTransaction.getToAccount())
                            .retrieve()
                            .bodyToMono(AccountDetails.class)
                            .block();
                });

                AccountDetails fromAccount = fromAccountFuture.join();
                AccountDetails toAccount = toAccountFuture.join();

                CompletableFuture<Void> debitToAccountFuture = CompletableFuture.runAsync(() -> {
                    webClientBuilder.build()
                            .put()
                            .uri(ServiceConstants.WITHDRAW_ENDPOINT, originalTransaction.getToAccount())
                            .bodyValue(new WithdrawRequest(UUID.fromString(toAccount.getAccountNumber()), originalTransaction.getAmount()))
                            .retrieve()
                            .bodyToMono(Void.class)
                            .block();
                });

                CompletableFuture<Void> creditFromAccountFuture = CompletableFuture.runAsync(() -> {
                    webClientBuilder.build()
                            .put()
                            .uri(ServiceConstants.DEPOSIT_ENDPOINT, originalTransaction.getFromAccount())
                            .bodyValue(new DepositeRequest(UUID.fromString(fromAccount.getAccountNumber()), originalTransaction.getAmount()))
                            .retrieve()
                            .bodyToMono(Void.class)
                            .block();
                });

                CompletableFuture.allOf(debitToAccountFuture, creditFromAccountFuture).join();

                originalTransaction.setReversed(true);
                transactionSQLRepo.save(originalTransaction);

                logger.info("Transaction reversal completed for ID: {}", reversalRequest.getTransactionId());

            } catch (Exception e) {
                logger.error("Error reversing transaction: {}", e.getMessage(), e);
                throw new RuntimeException("Error reversing transaction", e);
            }
        });
    }

    /**
     * Saves a new transaction.
     *
     * @param transactionDetails The details of the transaction to save.
     * @return The saved TransactionDetails object.
     */
    @Override
    public TransactionDetails saveTransaction(TransactionDetails transactionDetails) {
        logger.info("Saving transaction: {}", transactionDetails);
        return transactionDetailRepo.save(transactionDetails);
    }

    /**
     * Retrieves the account statement for a given account number.
     *
     * @param accountNumber The account number for which to fetch the statement.
     * @return A list of TransactionDetails objects representing the account statement.
     */
    @Override
    public List<TransactionDetails> statement(UUID accountNumber) {
        return transactionDetailRepo.findByAccountNumber(accountNumber);
    }
}
