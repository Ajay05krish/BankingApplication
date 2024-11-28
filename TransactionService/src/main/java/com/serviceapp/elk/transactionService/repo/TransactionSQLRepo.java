package com.serviceapp.elk.transactionService.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviceapp.elk.transactionService.model.TransactionDetails;
import com.serviceapp.elk.transactionService.model.TransferTransaction;

import reactor.core.publisher.Flux;

@Repository
public interface TransactionSQLRepo extends JpaRepository<TransferTransaction, Long> {

	List<TransferTransaction> findByFromAccountOrToAccount(UUID accountNumber, UUID accountNumber2);

}
