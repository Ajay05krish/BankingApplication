package com.example.bankingAppService.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingAppService.model.AccountInfo;
import com.example.bankingAppService.model.Bank;
import com.example.bankingAppService.request.DepositeRequest;
import com.example.bankingAppService.request.EmiRequest;
import com.example.bankingAppService.request.WithdrawRequest;
import com.example.bankingAppService.response.EmiResponse;
import com.example.bankingAppService.service.BankServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for handling bank-related operations.
 */
@RestController
@RequestMapping("/api/bank")
public class BankController {

    private static final Logger logger = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private BankServiceImpl bankService;

    /**
     * Adds a new bank with circuit breaker support.
     *
     * @param bank the bank to be added
     * @return the added bank
     */
    @PostMapping("/addBank")
    @CircuitBreaker(name = "bankService", fallbackMethod = "fallbackAddBank")
    public Bank addBank(@RequestBody Bank bank) {
        logger.info("Adding a new bank: {}", bank);
        return bankService.addBank(bank);
    }

    /**
     * Fallback method for adding a bank.
     *
     * @param bank the bank to be added
     * @param t the throwable that triggered the fallback
     * @return a default or empty bank object
     */
    public Bank fallbackAddBank(Bank bank, Throwable t) {
        logger.error("Fallback for addBank due to: {}", t.getMessage(), t);
        return new Bank(); // Return a default or empty bank object
    }

    /**
     * Retrieves all bank branches.
     *
     * @return a list of all bank branches
     */
    @GetMapping("/getAllBranches")
    public List<Bank> getAllBranches() {
        logger.info("Retrieving all bank branches");
        return bankService.getAllBranches();
    }

    /**
     * Retrieves account information by account number with circuit breaker support.
     *
     * @param accountNumber the account number
     * @return the account information
     */
    @GetMapping("/getAccountById/{accountNumber}")
    @CircuitBreaker(name = "bankService", fallbackMethod = "fallbackGetAccountById")
    public AccountInfo getAccountById(@PathVariable UUID accountNumber) {
        logger.info("Retrieving account information for account number: {}", accountNumber);
        return bankService.getAccountById(accountNumber);
    }

    /**
     * Fallback method for retrieving account information by account number.
     *
     * @param accountNumber the account number
     * @param t the throwable that triggered the fallback
     * @return null or a default account information object
     */
    public AccountInfo fallbackGetAccountById(UUID accountNumber, Throwable t) {
        logger.error("Fallback for getAccountById for account number: {} due to: {}", accountNumber, t.getMessage(), t);
        return null; // Return null or a default object
    }

    /**
     * Retrieves all accounts with circuit breaker support.
     *
     * @return a list of all accounts
     */
    @GetMapping("/getAllAccount")
    @CircuitBreaker(name = "bankService", fallbackMethod = "fallbackGetAllAccounts")
    public List<AccountInfo> getAllAccounts() {
        logger.info("Retrieving all account information");
        return bankService.getAllAccounts();
    }

    /**
     * Fallback method for retrieving all accounts.
     *
     * @param t the throwable that triggered the fallback
     * @return an empty list of accounts
     */
    public List<AccountInfo> fallbackGetAllAccounts(Throwable t) {
        logger.error("Fallback for getAllAccounts due to: {}", t.getMessage(), t);
        return List.of(); // Return an empty list
    }

    /**
     * Calculates EMI with circuit breaker support.
     *
     * @param emiRequest the EMI request containing principal, rate of interest, and tenure
     * @return the EMI response containing the calculated EMI
     */
    @PostMapping("/calculate-emi")
    @CircuitBreaker(name = "bankService", fallbackMethod = "fallbackCalculateEmi")
    public EmiResponse calculateEmi(@RequestBody EmiRequest emiRequest) {
        logger.info("Calculating EMI for principal: {}, rateOfInterest: {}, tenure: {}", 
                     emiRequest.principal(), emiRequest.rateOfInterest(), emiRequest.tenure());
        double emi = bankService.calculateEmi(emiRequest.principal(), emiRequest.rateOfInterest(), emiRequest.tenure());
        return new EmiResponse(emi);
    }

    /**
     * Fallback method for calculating EMI.
     *
     * @param emiRequest the EMI request
     * @param t the throwable that triggered the fallback
     * @return an EMI response with a default EMI value
     */
    public EmiResponse fallbackCalculateEmi(EmiRequest emiRequest, Throwable t) {
        logger.error("Fallback for calculateEmi due to: {}", t.getMessage(), t);
        return new EmiResponse(0.0); // Return a default EMI value
    }

    /**
     * Deposits an amount into an account with circuit breaker support.
     *
     * @param depositRequest the deposit request containing account number and deposit amount
     * @return the deposit request object
     */
    @PutMapping("/depositAmount")
    @CircuitBreaker(name = "bankService", fallbackMethod = "fallbackDepositAmount")
    public DepositeRequest depositAmount(@RequestBody DepositeRequest depositRequest) {
        logger.info("Depositing amount: {} into account number: {}", depositRequest.getBalance(), depositRequest.getAccountNumber());
        return bankService.depositAmount(depositRequest.getAccountNumber(), depositRequest.getBalance());
    }

    /**
     * Withdraws an amount from an account with circuit breaker support.
     *
     * @param withdrawRequest the withdrawal request containing account number and withdrawal amount
     * @return the withdrawal request object
     */
    @PutMapping("/withdrawAmount")
    @CircuitBreaker(name = "bankService", fallbackMethod = "fallbackWithdrawAmount")
    public WithdrawRequest withdrawAmount(@RequestBody WithdrawRequest withdrawRequest) {
        logger.info("Withdrawing amount: {} from account number: {}", withdrawRequest.getBalance(), withdrawRequest.getAccountNumber());
        return bankService.withdrawAmount(withdrawRequest.getAccountNumber(), withdrawRequest.getBalance());
    }

    /**
     * Fallback method for depositing an amount into an account.
     *
     * @param depositRequest the deposit request
     * @param t the throwable that triggered the fallback
     * @return the deposit request object with fallback information
     */
    public DepositeRequest fallbackDepositAmount(DepositeRequest depositRequest, Throwable t) {
        logger.error("Fallback for depositAmount for account number: {} due to: {}", depositRequest.getAccountNumber(), t.getMessage(), t);
        return depositRequest;
    }

    /**
     * Fallback method for withdrawing an amount from an account.
     *
     * @param withdrawRequest the withdrawal request
     * @param t the throwable that triggered the fallback
     * @return the withdrawal request object with fallback information
     */
    public WithdrawRequest fallbackWithdrawAmount(WithdrawRequest withdrawRequest, Throwable t) {
        logger.error("Fallback for withdrawAmount for account number: {} due to: {}", withdrawRequest.getAccountNumber(), t.getMessage(), t);
        return withdrawRequest;
    }
}
