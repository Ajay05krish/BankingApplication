package com.example.bankingAppService.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.bankingAppService.model.AccountInfo;
import com.example.bankingAppService.model.Bank;
import com.example.bankingAppService.request.DepositeRequest;
import com.example.bankingAppService.request.WithdrawRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankService {

    /**
     * @param bank
     * @return
     */
    Bank addBank(Bank bank);

    /**
     * @param accountNumber
     * @return
     */
    AccountInfo getAccountById(UUID accountNumber);

    /**
     * @return
     */
    List<AccountInfo> getAllAccounts();

    /**
     * @param principal
     * @param rateOfInterest
     * @param tenure
     * @return
     */
    double calculateEmi(double principal, double rateOfInterest, int tenure);

    /**
     * @param accountNumber
     * @param balance
     * @return
     */
    WithdrawRequest withdrawAmount(UUID accountNumber, double balance);

    /**
     * @param accountNumber
     * @param balance
     * @return
     */
    DepositeRequest depositAmount(UUID accountNumber, double balance);

    /**
     * @return
     */
    List<Bank> getAllBranches();
}
