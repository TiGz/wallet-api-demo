package org.github.tigz.wallet.modules.wallet.service;

import org.github.tigz.wallet.common.dto.PageDTO;
import org.github.tigz.wallet.modules.wallet.dto.TransactionDTO;
import org.github.tigz.wallet.modules.wallet.dto.WalletDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Service
public class RetryableWalletService {

    private static final Logger logger = LoggerFactory.getLogger(RetryableWalletService.class);

    private final WalletService walletService;

    @Autowired
    public RetryableWalletService(WalletService walletService) {
        this.walletService = walletService;
    }

    @Retryable(maxAttempts = 3)
    public WalletDTO addFunds(String customerId, BigDecimal amount) {
        logger.debug("Attempting to add funds (creating wallet if not exists): customerId={}, amount={}", customerId, amount);
        WalletDTO result = walletService.addFunds(customerId, amount);
        logger.debug("Funds added successfully: customerId={}, newBalance={}", customerId, result.getBalance());
        return result;
    }

    @Retryable(maxAttempts = 3)
    public WalletDTO withdrawFunds(String customerId, BigDecimal amount) {
        logger.debug("Attempting to withdraw funds: customerId={}, amount={}", customerId, amount);
        WalletDTO result = walletService.withdrawFunds(customerId, amount);
        logger.debug("Funds withdrawn successfully: customerId={}, newBalance={}", customerId, result.getBalance());
        return result;
    }

    @Retryable(maxAttempts = 3)
    public PageDTO<TransactionDTO> getTransactions(String customerId, Pageable pageable) {
        logger.debug("Fetching transactions: customerId={}, page={}, size={}", customerId, pageable.getPageNumber(), pageable.getPageSize());
        PageDTO<TransactionDTO> result = walletService.getTransactions(customerId, pageable);
        logger.debug("Transactions fetched: customerId={}, totalElements={}", customerId, result.getTotalElements());
        return result;
    }

    @Retryable(maxAttempts = 3)
    public WalletDTO getWallet(String customerId) {
        logger.debug("Fetching wallet: customerId={}", customerId);
        WalletDTO result = walletService.getWallet(customerId);
        logger.debug("Wallet fetched: customerId={}, balance={}", customerId, result.getBalance());
        return result;
    }
}