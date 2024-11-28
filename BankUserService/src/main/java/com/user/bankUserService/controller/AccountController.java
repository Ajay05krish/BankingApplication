package com.user.bankUserService.controller;

import java.util.List;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.bankUserService.entity.Account;
import com.user.bankUserService.request.DepositeRequest;
import com.user.bankUserService.request.WithdrawRequest;
import com.user.bankUserService.service.AccountService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/**
 * AccountController handles all the operations related to account management,
 * such as
 * creating an account, fetching accounts, depositing money, and withdrawing
 * money.
 * It uses {@link AccountService} to perform business logic.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * Creates a new account with the given account details.
     *
     * @param account the account details to be created
     * @return ResponseEntity containing the created Account object
     */
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        logger.info("Creating new account with details: {}", account);
        Account savedAccount = accountService.saveAccount(account);
        logger.info("Account created successfully: {}", savedAccount);
        return ResponseEntity.ok(savedAccount);
    }

    /**
     * Fetches the account details for a specific account ID.
     *
     * @param accountNumber the unique ID of the account to retrieve
     * @return the Account object corresponding to the given account ID
     */
    @GetMapping("/getById/{accountNumber}")
    public Account getAccountById(@PathVariable UUID accountNumber) {
        logger.info("Fetching account with ID: {}", accountNumber);
        Account account = accountService.getAccountById(accountNumber);
        logger.info("Fetched account: {}", account);
        return account;
    }

    /**
     * Retrieves all existing accounts.
     *
     * @return List of all Account objects
     */
    @GetMapping("/getAllAccount")
    public List<Account> getAllAccount() {
        logger.info("Fetching all accounts");
        List<Account> accounts = accountService.getAllAccount();
        logger.info("Total accounts fetched: {}", accounts.size());
        return accounts;
    }

    /**
     * Deposits the specified amount into the account with the given account ID.
     * Implements a CircuitBreaker for fault tolerance.
     *
     * @param accountNumber  the ID of the account to deposit into
     * @param updatedAccount the details of the deposit request
     * @return ResponseEntity containing the updated DepositeRequest object
     */
    @PutMapping("/depositAmount/{accountNumber}")
    @CircuitBreaker(name = "accountServiceCircuitBreaker", fallbackMethod = "fallbackDeposit")
    public ResponseEntity<DepositeRequest> updateAccountBalance(@PathVariable UUID accountNumber,
            @RequestBody DepositeRequest updatedAccount) {
        logger.info("Updating balance for account ID: {}", accountNumber);
        DepositeRequest account = accountService.updateAccountBalance(accountNumber, updatedAccount);
        logger.info("Account balance updated successfully for ID: {}", accountNumber);
        return ResponseEntity.ok(account);
    }

    /**
     * Withdraws the specified amount from the account with the given account ID.
     * Implements a CircuitBreaker for fault tolerance.
     *
     * @param accountNumber  the ID of the account to withdraw from
     * @param updatedAccount the details of the withdrawal request
     * @return ResponseEntity containing the updated WithdrawRequest object
     */
    @PutMapping("/withdraw/{accountNumber}")
    @CircuitBreaker(name = "accountServiceCircuitBreaker", fallbackMethod = "fallbackWithdraw")
    public ResponseEntity<WithdrawRequest> withdrawFromAccount(@PathVariable UUID accountNumber,
            @RequestBody WithdrawRequest updatedAccount) {
        logger.info("Withdrawing from account ID: {}", accountNumber);
        WithdrawRequest account = accountService.withdrawFromAccount(accountNumber, updatedAccount);
        logger.info("Withdrawal successful for account ID: {}", accountNumber);
        return ResponseEntity.ok(account);
    }

    /**
     * Fallback method for deposit operations when the service is unavailable.
     *
     * @param accountNumber  the ID of the account
     * @param updatedAccount the deposit request details
     * @param t              the throwable error that caused the fallback
     * @return ResponseEntity with an error message and SERVICE_UNAVAILABLE status
     */
    public ResponseEntity<String> fallbackDeposit(UUID accountNumber, DepositeRequest updatedAccount, Throwable t) {
        logger.error("Deposit service failed for account ID: {}. Reason: {}", accountNumber, t.getMessage());
        return ResponseEntity.status(HttpStatus.SC_SERVICE_UNAVAILABLE)
                .body("Deposit service is currently unavailable. Please try again later.");
    }

    /**
     * Fallback method for withdrawal operations when the service is unavailable.
     *
     * @param accountNumber  the ID of the account
     * @param updatedAccount the withdrawal request details
     * @param t              the throwable error that caused the fallback
     * @return ResponseEntity with an error message and SERVICE_UNAVAILABLE status
     */
    public ResponseEntity<String> fallbackWithdraw(UUID accountNumber, WithdrawRequest updatedAccount, Throwable t) {
        logger.error("Withdrawal service failed for account ID: {}. Reason: {}", accountNumber, t.getMessage());
        return ResponseEntity.status(HttpStatus.SC_SERVICE_UNAVAILABLE)
                .body("Withdrawal service is currently unavailable. Please try again later.");
    }
}
