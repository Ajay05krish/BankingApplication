package com.user.bankUserService.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.user.bankUserService.constants.ServiceConstants;
import com.user.bankUserService.entity.Account;
import com.user.bankUserService.exception.InsufficientFundsException;
import com.user.bankUserService.repository.AccountRepository;
import com.user.bankUserService.request.DepositeRequest;
import com.user.bankUserService.request.TransactionRequest;
import com.user.bankUserService.request.WithdrawRequest;

import java.time.Duration;
import java.util.Date;

import jakarta.annotation.PreDestroy;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * Implementation of {@link AccountService} that handles account-related
 * operations
 * such as saving accounts, fetching account details, updating balance, and
 * withdrawals.
 * It uses asynchronous operations with virtual threads for improved
 * scalability.
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebClient.Builder webClient;

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Saves the provided account.
     *
     * @param account the account to be saved
     * @return the saved account
     */
    @Override
    public Account saveAccount(Account account) {
        logger.info("Saving account for user: {}", account.getAccountHolderName());
        Account savedAccount = accountRepository.save(account);
        logger.info("Account saved successfully: {}", savedAccount.getAccountNumber());
        return savedAccount;
    }

    /**
     * Retrieves account details for the given account ID.
     *
     * @param accountNumber the account ID
     * @return the account details, or null if not found
     */
    @Override
    public Account getAccountById(UUID accountNumber) {
        logger.info("Fetching account details for account ID: {}", accountNumber);
        Account account = accountRepository.findById(accountNumber).orElse(null);
        if (account != null) {
            logger.info("Account found: {}", account.getAccountHolderName());
        } else {
            logger.warn("No account found with ID: {}", accountNumber);
        }
        return account;
    }

    /**
     * Retrieves a list of all accounts.
     *
     * @return a list of all accounts
     */
    @Override
    public List<Account> getAllAccount() {
        logger.info("Fetching all accounts");
        List<Account> accounts = accountRepository.findAll();
        logger.info("Fetched {} accounts from the database", accounts.size());
        return accounts;
    }

    /**
     * Updates the account balance by depositing the specified amount into the
     * account.
     * Also creates a transaction record for the deposit operation.
     *
     * @param accountNumber  the account ID
     * @param updatedAccount the deposit request containing the amount to deposit
     * @return the updated deposit request with the new balance, or null if the
     *         account is not found
     * @throws IllegalArgumentException if the deposit amount is less than or equal
     *                                  to zero
     */
    @Override
    public DepositeRequest updateAccountBalance(UUID accountNumber, DepositeRequest updatedAccount) {
        logger.info("Updating balance for account ID: {}", accountNumber);
        return CompletableFuture.supplyAsync(() -> {
            double depositAmount = updatedAccount.getBalance();
            if (depositAmount <= 0) {
                logger.error("Invalid deposit amount: {}. Amount must be greater than zero", depositAmount);
                throw new IllegalArgumentException("Deposit amount must be greater than zero");
            }

            Account existingAccount = getAccountById(accountNumber);
            if (existingAccount != null) {
                double currentBalance = existingAccount.getBalance();
                
                existingAccount.setBalance(currentBalance + depositAmount);
                accountRepository.save(existingAccount);

                logger.info("Balance updated successfully for account ID: {}", accountNumber);

                // Prepare and save transaction
                TransactionRequest transactionRequest = new TransactionRequest();
                transactionRequest.setAccountNumber(accountNumber);
                transactionRequest.setAmount(depositAmount);
                transactionRequest.setTransactionDate(new Date());
                transactionRequest.setTransactionType("Deposit");
                transactionRequest.setStatus("Success");

                saveTransaction(transactionRequest).subscribe();

                // Prepare deposit response
                DepositeRequest depositeRequest = new DepositeRequest();
                depositeRequest.setAccountNumber(existingAccount.getAccountNumber());
                depositeRequest.setBalance(existingAccount.getBalance());

                return depositeRequest;
            } else {
                logger.warn("Account not found for ID: {}", accountNumber);
                return null;
            }
        }, virtualThreadExecutor).join();
    }

    /**
     * Saves the transaction details asynchronously using a WebClient call.
     *
     * @param transactionRequest the transaction details to be saved
     * @return a Mono containing the saved transaction request
     */
    @Override
    public Mono<TransactionRequest> saveTransaction(TransactionRequest transactionRequest) {
        return webClient.build()
                .post()
                .uri(ServiceConstants.TRANSACTION_SERVICE_URL)
                .bodyValue(transactionRequest)
                .retrieve()
                .bodyToMono(TransactionRequest.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .doOnError(e -> logger.error("Error saving transaction: {}", e.getMessage()));
    }

    /**
     * Withdraws the specified amount from the account if sufficient funds are
     * available.
     * Also creates a transaction record for the withdrawal operation.
     *
     * @param accountNumber  the account ID
     * @param updatedAccount the withdrawal request containing the amount to
     *                       withdraw
     * @return the updated withdrawal request with the new balance, or null if the
     *         account is not found
     * @throws InsufficientFundsException if there are insufficient funds in the
     *                                    account
     */
    @Override
    public WithdrawRequest withdrawFromAccount(UUID accountNumber, WithdrawRequest updatedAccount) {
        logger.info("Initiating withdrawal for account ID: {}", accountNumber);
        return CompletableFuture.supplyAsync(() -> {
            Account existingAccount = getAccountById(accountNumber);
            if (existingAccount != null) {
                double currentBalance = existingAccount.getBalance();
                double withdrawAmount = updatedAccount.getBalance();
                if (currentBalance >= withdrawAmount) {
                    existingAccount.setBalance(currentBalance - withdrawAmount);
                    accountRepository.save(existingAccount);

                    logger.info("Withdrawal successful for account ID: {}", accountNumber);

                    TransactionRequest transactionRequest = new TransactionRequest();
                    transactionRequest.setAccountNumber(accountNumber);
                    transactionRequest.setAmount(withdrawAmount);
                    transactionRequest.setTransactionDate(new Date());
                    transactionRequest.setTransactionType("Withdraw");
                    transactionRequest.setStatus("Success");

                    saveTransaction(transactionRequest).subscribe();

                    WithdrawRequest withdrawRequest = new WithdrawRequest();
                    withdrawRequest.setAccountNumber(existingAccount.getAccountNumber());
                    withdrawRequest.setBalance(existingAccount.getBalance());

                    return withdrawRequest;
                } else {
                    logger.error("Insufficient funds for account ID: {}", accountNumber);
                    throw new InsufficientFundsException("Insufficient balance for withdrawal");
                }
            } else {
                logger.warn("Account not found for ID: {}", accountNumber);
                return null;
            }
        }, virtualThreadExecutor).join();
    }

    /**
     * Shuts down the virtual thread executor during application shutdown.
     */
    @Override
    @PreDestroy
    public void closeVirtualThreadExecutor() {
        logger.info("Shutting down virtual thread executor");
        virtualThreadExecutor.shutdown();
    }
}
