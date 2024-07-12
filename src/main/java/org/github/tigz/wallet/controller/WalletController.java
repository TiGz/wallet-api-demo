package org.github.tigz.wallet.controller;

import org.github.tigz.wallet.dto.PageDTO;
import org.github.tigz.wallet.dto.TransactionDTO;
import org.github.tigz.wallet.dto.WalletDTO;
import org.github.tigz.wallet.service.RetryableWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    private final RetryableWalletService retryableWalletService;

    @Autowired
    public WalletController(RetryableWalletService retryableWalletService) {
        this.retryableWalletService = retryableWalletService;
    }

    @PostMapping("/{customerId}/add")
    public ResponseEntity<WalletDTO> addFunds(@PathVariable String customerId, @RequestBody FundsRequest request) {
        logger.debug("Adding funds for customer: {}, amount: {}", customerId, request.getAmount());
        WalletDTO updatedWallet = retryableWalletService.addFunds(customerId, request.getAmount());
        logger.debug("Funds added successfully. Updated balance: {}", updatedWallet.getBalance());
        return ResponseEntity.ok(updatedWallet);
    }

    @PostMapping("/{customerId}/withdraw")
    public ResponseEntity<WalletDTO> withdrawFunds(@PathVariable String customerId, @RequestBody FundsRequest request) {
        logger.debug("Withdrawing funds for customer: {}, amount: {}", customerId, request.getAmount());
        WalletDTO updatedWallet = retryableWalletService.withdrawFunds(customerId, request.getAmount());
        logger.debug("Funds withdrawn successfully. Updated balance: {}", updatedWallet.getBalance());
        return ResponseEntity.ok(updatedWallet);
    }

    @GetMapping("/{customerId}/transactions")
    public ResponseEntity<PageDTO<TransactionDTO>> getTransactions(@PathVariable String customerId, Pageable pageable) {
        logger.debug("Fetching transactions for customer: {}, page: {}, size: {}", customerId, pageable.getPageNumber(), pageable.getPageSize());
        PageDTO<TransactionDTO> transactions = retryableWalletService.getTransactions(customerId, pageable);
        logger.debug("Retrieved {} transactions for customer: {}", transactions.getTotalElements(), customerId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<WalletDTO> getWallet(@PathVariable String customerId) {
        logger.debug("Fetching wallet for customer: {}", customerId);
        WalletDTO wallet = retryableWalletService.getWallet(customerId);
        logger.debug("Retrieved wallet for customer: {}. Current balance: {}", customerId, wallet.getBalance());
        return ResponseEntity.ok(wallet);
    }

    // Inner class to represent the JSON request body
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