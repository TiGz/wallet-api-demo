package org.github.tigz.wallet.modules.wallet.controller;

import org.github.tigz.wallet.modules.wallet.dto.PageDTO;
import org.github.tigz.wallet.modules.wallet.dto.TransactionDTO;
import org.github.tigz.wallet.modules.wallet.dto.WalletDTO;
import org.github.tigz.wallet.modules.wallet.service.RetryableWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * REST controller for managing wallet operations.
 * This class handles HTTP requests related to wallet functionalities such as
 * adding funds, withdrawing funds, fetching transactions, and retrieving wallet information.
 */
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    private final RetryableWalletService retryableWalletService;

    /**
     * Constructs a new WalletController with the specified RetryableWalletService.
     *
     * @param retryableWalletService The service to handle wallet operations with retry capability
     */
    @Autowired
    public WalletController(RetryableWalletService retryableWalletService) {
        this.retryableWalletService = retryableWalletService;
    }

    /**
     * Handles the request to add funds to a customer's wallet.
     *
     * @param customerId The ID of the customer
     * @param request The request containing the amount to add
     * @return ResponseEntity containing the updated WalletDTO
     */
    @PostMapping("/{customerId}/add")
    public ResponseEntity<WalletDTO> addFunds(@PathVariable String customerId, @RequestBody FundsRequest request) {
        logger.debug("Adding funds for customer: {}, amount: {}", customerId, request.getAmount());
        WalletDTO updatedWallet = retryableWalletService.addFunds(customerId, request.getAmount());
        logger.debug("Funds added successfully. Updated balance: {}", updatedWallet.getBalance());
        return ResponseEntity.ok(updatedWallet);
    }

    /**
     * Handles the request to withdraw funds from a customer's wallet.
     *
     * @param customerId The ID of the customer
     * @param request The request containing the amount to withdraw
     * @return ResponseEntity containing the updated WalletDTO
     */
    @PostMapping("/{customerId}/withdraw")
    public ResponseEntity<WalletDTO> withdrawFunds(@PathVariable String customerId, @RequestBody FundsRequest request) {
        logger.debug("Withdrawing funds for customer: {}, amount: {}", customerId, request.getAmount());
        WalletDTO updatedWallet = retryableWalletService.withdrawFunds(customerId, request.getAmount());
        logger.debug("Funds withdrawn successfully. Updated balance: {}", updatedWallet.getBalance());
        return ResponseEntity.ok(updatedWallet);
    }

    /**
     * Retrieves a paginated list of transactions for a customer's wallet.
     *
     * @param customerId The ID of the customer
     * @param pageable The pagination information
     * @return ResponseEntity containing a PageDTO of TransactionDTO objects
     */
    @GetMapping("/{customerId}/transactions")
    public ResponseEntity<PageDTO<TransactionDTO>> getTransactions(@PathVariable String customerId, Pageable pageable) {
        logger.debug("Fetching transactions for customer: {}, page: {}, size: {}", customerId, pageable.getPageNumber(), pageable.getPageSize());
        PageDTO<TransactionDTO> transactions = retryableWalletService.getTransactions(customerId, pageable);
        logger.debug("Retrieved {} transactions for customer: {}", transactions.getTotalElements(), customerId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Retrieves the wallet information for a specific customer.
     *
     * @param customerId The ID of the customer
     * @return ResponseEntity containing the WalletDTO
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<WalletDTO> getWallet(@PathVariable String customerId) {
        logger.debug("Fetching wallet for customer: {}", customerId);
        WalletDTO wallet = retryableWalletService.getWallet(customerId);
        logger.debug("Retrieved wallet for customer: {}. Current balance: {}", customerId, wallet.getBalance());
        return ResponseEntity.ok(wallet);
    }

    /**
     * Inner class to represent the JSON request body for fund operations.
     */
    private static class FundsRequest {
        private BigDecimal amount;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}