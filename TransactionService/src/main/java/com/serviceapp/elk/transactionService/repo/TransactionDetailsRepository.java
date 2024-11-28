package com.serviceapp.elk.transactionService.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviceapp.elk.transactionService.model.TransactionDetails;

import java.util.*;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Integer> {

	List<TransactionDetails> findByAccountNumber(UUID accountNumber);

}
