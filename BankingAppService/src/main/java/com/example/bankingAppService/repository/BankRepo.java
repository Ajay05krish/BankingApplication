package com.example.bankingAppService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bankingAppService.model.Bank;



@Repository
public interface BankRepo extends JpaRepository<Bank,Integer> {

}
