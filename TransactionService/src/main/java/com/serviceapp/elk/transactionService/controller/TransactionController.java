package com.serviceapp.elk.transactionService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.serviceapp.elk.transactionService.model.TransactionDetails;
import com.serviceapp.elk.transactionService.model.TransferTransaction;
import com.serviceapp.elk.transactionService.request.TransactionRequest;
import com.serviceapp.elk.transactionService.request.TransactionReversalRequest;
import com.serviceapp.elk.transactionService.service.TransactionService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * Controller for handling transaction-related operations. This controller
 * provides endpoints for transferring money, retrieving transaction history,
 * and managing transactions. Circuit Breaker pattern is used to handle failures
 * gracefully.
 */
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	private static final String TRANSACTION_SERVICE = "transactionServiceCircuitBreaker";

	/**
	 * Transfers an amount from one account to another. Uses Circuit Breaker for
	 * fault tolerance.
	 *
	 * @param transferRequestDTO The transfer request containing source account,
	 *                           destination account, and amount to transfer.
	 * @return A ResponseEntity<String> containing a success message.
	 */
	@PutMapping("/transfer")
	@CircuitBreaker(name = TRANSACTION_SERVICE, fallbackMethod = "fallbackTransfer")
	public ResponseEntity<String> transfer(@RequestBody TransactionRequest transferRequestDTO) {
		transactionService.transfer(transferRequestDTO.getFromAccount(), transferRequestDTO.getToAccount(),
				transferRequestDTO.getAmount());
		return ResponseEntity.ok("Transfer completed successfully.");
	}

	/**
	 * Fallback method for transfer operation.
	 *
	 * @param transferRequestDTO The transfer request.
	 * @param throwable          The exception that caused the fallback.
	 * @return A ResponseEntity<String> containing an error message.
	 */
	public ResponseEntity<String> fallbackTransfer(TransactionRequest transferRequestDTO, Throwable throwable) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Transfer failed due to: " + throwable.getMessage());
	}

	/**
	 * Retrieves the transaction history for a specified account. Uses Circuit
	 * Breaker for fault tolerance.
	 *
	 * @param accountNumber The account number for which to fetch the transaction
	 *                      history.
	 * @return A ResponseEntity containing a list of TransferTransaction objects.
	 */
	@GetMapping("/history/{accountNumber}")
	@CircuitBreaker(name = TRANSACTION_SERVICE, fallbackMethod = "fallbackTransactionHistory")
	public ResponseEntity<List<TransferTransaction>> getTransactionHistory(@PathVariable UUID accountNumber) {
		List<TransferTransaction> transactions = transactionService.getTransactionHistory(accountNumber);
		return ResponseEntity.ok(transactions);
	}

	/**
	 * Fallback method for transaction history retrieval.
	 *
	 * @param accountNumber The account number.
	 * @param throwable     The exception that caused the fallback.
	 * @return A ResponseEntity<String> containing an error message.
	 */
	public ResponseEntity<String> fallbackTransactionHistory(UUID accountNumber, Throwable throwable) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Failed to retrieve transaction history: " + throwable.getMessage());
	}

	/**
	 * Retrieves a transaction by its ID. Uses Circuit Breaker for fault tolerance.
	 *
	 * @param id The transaction ID to fetch.
	 * @return A ResponseEntity containing the TransferTransaction object.
	 */
	@GetMapping("/getTransactionById/{id}")
	@CircuitBreaker(name = TRANSACTION_SERVICE, fallbackMethod = "fallbackGetTransactionById")
	public TransferTransaction getTransactionById(@PathVariable Long id) {
		return transactionService.findTransactionById(id);
	}

	/**
	 * Fallback method for retrieving transaction by ID.
	 *
	 * @param id        The transaction ID.
	 * @param throwable The exception that caused the fallback.
	 * @return A ResponseEntity<String> containing an error message.
	 */
	public ResponseEntity<String> fallbackGetTransactionById(Long id, Throwable throwable) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Failed to retrieve transaction by ID: " + throwable.getMessage());
	}

	/**
	 * Reverses a transaction based on its ID.
	 *
	 * @param id The transaction ID to reverse.
	 * @return A ResponseEntity<String> containing a success message.
	 */
	@PostMapping("/reverse")
	public Mono<ResponseEntity<String>> reverseTransaction(@RequestBody TransactionReversalRequest reversalRequest) {
		return transactionService.reverseTransaction(reversalRequest)
				.then(Mono.just(ResponseEntity.ok("Transaction reversal successful."))).onErrorResume(e -> Mono
						.just(ResponseEntity.badRequest().body("Transaction reversal failed: " + e.getMessage())));
	}

	/**
	 * Saves a new transaction.
	 *
	 * @param transactionDetails The details of the transaction to be saved.
	 * @return A ResponseEntity<String> containing a success message with the
	 *         transaction ID.
	 */
	@PostMapping("/saveTransaction")
	public ResponseEntity<String> saveTransaction(@RequestBody TransactionDetails transactionDetails) {
		TransactionDetails savedTransaction = transactionService.saveTransaction(transactionDetails);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Transaction saved successfully with ID: " + savedTransaction.getId());
	}

	/**
	 * Retrieves the account statement for a given account number.
	 *
	 * @param accountNumber The account number for which to fetch the statement.
	 * @return A ResponseEntity containing a list of TransactionDetails objects.
	 */
	@GetMapping("/statement/{accountNumber}")
	public ResponseEntity<List<TransactionDetails>> statement(@PathVariable UUID accountNumber) {
		List<TransactionDetails> statement = transactionService.statement(accountNumber);
		return ResponseEntity.ok(statement);
	}
}
