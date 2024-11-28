package com.example.bankingAppService.service;

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

import com.example.bankingAppService.constants.ServiceConstants;
import com.example.bankingAppService.model.AccountInfo;
import com.example.bankingAppService.model.Bank;
import com.example.bankingAppService.repository.BankRepo;
import com.example.bankingAppService.request.DepositeRequest;
import com.example.bankingAppService.request.WithdrawRequest;

import jakarta.annotation.PreDestroy;
import reactor.core.publisher.Mono;

/**
 * Service implementation for handling bank-related operations.
 */
@Service
public class BankServiceImpl implements BankService {

    private static final Logger logger = LoggerFactory.getLogger(BankServiceImpl.class);

    @Autowired
    private BankRepo bankRepo;

    @Autowired
    private WebClient.Builder webClient;

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Adds a new bank record.
     *
     * @param bank the bank to be added
     * @return the added bank
     */
    @Override
    public Bank addBank(Bank bank) {
        logger.info("Adding a new bank record: {}", bank.getBankId());
        Bank savedBank = bankRepo.save(bank);
        logger.info("Bank record added successfully: {}", savedBank.getBankId());
        return savedBank;
    }
    
    /**
     * Retrieves account information by account number.
     *
     * @param accountNumber the account number
     * @return the account information
     */
    @Override
    public AccountInfo getAccountById(UUID accountNumber) {
        logger.info("Fetching account details for ID: {}", accountNumber);
        logger.debug("Sending request to account service to fetch account info for ID: {}", accountNumber);
        List<AccountInfo> accountInfo = webClient.build()
                .get()
                .uri(ServiceConstants.GET_ACCOUNT_BY_ID_URL, accountNumber)
                .retrieve()
                .bodyToFlux(AccountInfo.class)
                .collectList()
                .block(); // Blocking to wait for the result
        logger.info("Fetched account info for ID: {}", accountNumber);
        return accountInfo.isEmpty() ? null : accountInfo.get(0);
    }
    
    /**
     * Retrieves all accounts.
     *
     * @return a list of all accounts
     */
    @Override
    public List<AccountInfo> getAllAccounts() {
        logger.info("Fetching all accounts from account service");
        logger.debug("Sending request to account service to fetch all accounts");
        List<AccountInfo> accounts = webClient.build()
                .get()
                .uri(ServiceConstants.GET_ALL_ACCOUNTS_URL)
                .retrieve()
                .bodyToFlux(AccountInfo.class)
                .collectList()
                .block(); // Blocking the Flux and converting to List
        logger.info("Fetched all accounts from account service");
        return accounts;
    }
    
    /**
     * Calculates the EMI (Equated Monthly Installment).
     *
     * @param principal the principal amount
     * @param rateOfInterest the rate of interest
     * @param tenure the tenure in months
     * @return the calculated EMI
     */
    @Override
    public double calculateEmi(double principal, double rateOfInterest, int tenure) {
        logger.info("Calculating EMI for Principal: {}, Rate: {}, Tenure: {}", principal, rateOfInterest, tenure);
        double emi = (principal * rateOfInterest * Math.pow(1 + rateOfInterest, tenure))
                / (Math.pow(1 + rateOfInterest, tenure) - 1);
        logger.debug("Calculated EMI: {}", emi);
        return emi;
    }
    
    /**
     * Withdraws an amount from an account.
     *
     * @param accountNumber the account number
     * @param balance the amount to withdraw
     * @return the WithdrawRequest object
     */
    public WithdrawRequest withdrawAmount(UUID accountNumber, double balance) {
        logger.info("Initiating withdrawal for account ID: {} with amount: {}", accountNumber, balance);
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAccountNumber(accountNumber);
        withdrawRequest.setBalance(balance);

        // WebClient call to withdraw the amount
        webClient.build()
                .put()
                .uri(ServiceConstants.WITHDRAW_URL, accountNumber)
                .body(Mono.just(withdrawRequest), WithdrawRequest.class)
                .retrieve()
                .bodyToMono(WithdrawRequest.class)
                .block();

        logger.info("Withdrawal successful for account ID: {}", accountNumber);
        return withdrawRequest;
    }

    /**
     * Deposits an amount into an account.
     *
     * @param accountNumber the account number
     * @param balance the amount to deposit
     * @return the DepositeRequest object
     */
    public DepositeRequest depositAmount(UUID accountNumber, double balance) {
        logger.info("Initiating deposit for account ID: {} with amount: {}", accountNumber, balance);
        DepositeRequest depositeRequest = new DepositeRequest();
        depositeRequest.setAccountNumber(accountNumber);
        depositeRequest.setBalance(balance);

        // WebClient call to deposit the amount
        webClient.build()
                .put()
                .uri(ServiceConstants.DEPOSIT_URL, accountNumber)
                .body(Mono.just(depositeRequest), DepositeRequest.class)
                .retrieve()
                .bodyToMono(DepositeRequest.class)
                .block();

        logger.info("Deposit successful for account ID: {}", accountNumber);
        return depositeRequest;
    }

    /**
     * Retrieves all bank branches.
     *
     * @return a list of all bank branches
     */
    @Override
    public List<Bank> getAllBranches() {
        logger.info("Fetching all bank branches");

            List<Bank> banks = bankRepo.findAll();
        return banks;
    }

    /**
     * Shuts down the virtual thread executor.
     */
    @PreDestroy
    public void closeVirtualThreadExecutor() {
        logger.info("Shutting down virtual thread executor");
        virtualThreadExecutor.shutdown();
    }
}
