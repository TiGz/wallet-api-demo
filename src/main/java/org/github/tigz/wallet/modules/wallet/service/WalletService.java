package org.github.tigz.wallet.modules.wallet.service;

import org.github.tigz.wallet.modules.wallet.config.WalletConfig;
import org.github.tigz.wallet.common.dto.PageDTO;
import org.github.tigz.wallet.modules.wallet.dto.TransactionDTO;
import org.github.tigz.wallet.modules.wallet.dto.WalletDTO;
import org.github.tigz.wallet.modules.wallet.model.Transaction;
import org.github.tigz.wallet.modules.wallet.model.Wallet;
import org.github.tigz.wallet.modules.wallet.repository.TransactionRepository;
import org.github.tigz.wallet.modules.wallet.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing wallet operations.
 * This class handles the business logic for wallet-related functionalities such as
 * adding funds, withdrawing funds, retrieving transactions, and fetching wallet information.
 */
@Service
public class WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletConfig walletConfig;

    /**
     * Constructs a new WalletService with the specified repositories and configuration.
     *
     * @param walletRepository The repository for wallet data
     * @param transactionRepository The repository for transaction data
     * @param walletConfig The configuration for wallet operations
     */
    @Autowired
    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository, WalletConfig walletConfig) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.walletConfig = walletConfig;
    }

    /**
     * Adds funds to a customer's wallet.
     *
     * @param customerId The ID of the customer
     * @param amount The amount to add
     * @return WalletDTO representing the updated wallet
     * @throws IllegalArgumentException if the amount is null or outside the allowed range
     */
    @Transactional
    public WalletDTO addFunds(String customerId, BigDecimal amount) {
        logger.debug("Attempting to add funds: customerId={}, amount={}", customerId, amount);

        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (amount.compareTo(walletConfig.getMinAddAmount()) < 0 || amount.compareTo(walletConfig.getMaxAddAmount()) > 0) {
            throw new IllegalArgumentException(String.format("Amount must be between £%s and £%s",
                    walletConfig.getMinAddAmount(), walletConfig.getMaxAddAmount()));
        }

        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet(customerId, BigDecimal.ZERO);
                    logger.info("Creating new wallet for customer: {}", customerId);
                    return walletRepository.save(newWallet);
                });

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        Transaction transaction = new Transaction(wallet, amount, Transaction.TransactionType.CREDIT);
        transactionRepository.save(transaction);

        logger.info("Funds added successfully: customerId={}, amount={}, newBalance={}", customerId, amount, wallet.getBalance());
        return convertToDTO(wallet);
    }

    /**
     * Withdraws funds from a customer's wallet.
     *
     * @param customerId The ID of the customer
     * @param amount The amount to withdraw
     * @return WalletDTO representing the updated wallet
     * @throws IllegalArgumentException if the amount is null or outside the allowed range
     * @throws RuntimeException if the wallet is not found
     * @throws IllegalStateException if there are insufficient funds
     */
    @Transactional
    public WalletDTO withdrawFunds(String customerId, BigDecimal amount) {
        logger.debug("Attempting to withdraw funds: customerId={}, amount={}", customerId, amount);

        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (amount.compareTo(walletConfig.getMinWithdrawAmount()) < 0 || amount.compareTo(walletConfig.getMaxWithdrawAmount()) > 0) {
            throw new IllegalArgumentException(String.format("Amount must be between £%s and £%s",
                    walletConfig.getMinWithdrawAmount(), walletConfig.getMaxWithdrawAmount()));
        }

        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for customer: " + customerId));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds for withdrawal");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        Transaction transaction = new Transaction(wallet, amount, Transaction.TransactionType.DEBIT);
        transactionRepository.save(transaction);

        logger.info("Funds withdrawn successfully: customerId={}, amount={}, newBalance={}", customerId, amount, wallet.getBalance());
        return convertToDTO(wallet);
    }

    /**
     * Retrieves a paginated list of transactions for a customer's wallet.
     *
     * @param customerId The ID of the customer
     * @param pageable The pagination information
     * @return PageDTO containing TransactionDTO objects
     * @throws RuntimeException if the wallet is not found
     */
    public PageDTO<TransactionDTO> getTransactions(String customerId, Pageable pageable) {
        logger.debug("Retrieving transactions: customerId={}, page={}, size={}", customerId, pageable.getPageNumber(), pageable.getPageSize());

        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for customer: " + customerId));

        Page<Transaction> transactionPage = transactionRepository.findByWallet(wallet, pageable);
        List<TransactionDTO> transactionDTOs = transactionPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageDTO<>(
                transactionDTOs,
                transactionPage.getNumber(),
                transactionPage.getSize(),
                transactionPage.getTotalElements(),
                transactionPage.getTotalPages()
        );
    }

    /**
     * Retrieves the wallet information for a specific customer.
     *
     * @param customerId The ID of the customer
     * @return WalletDTO representing the customer's wallet
     * @throws RuntimeException if the wallet is not found
     */
    public WalletDTO getWallet(String customerId) {
        logger.debug("Retrieving wallet: customerId={}", customerId);

        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for customer: " + customerId));
        return convertToDTO(wallet);
    }

    /**
     * Converts a Wallet entity to a WalletDTO.
     *
     * @param wallet The Wallet entity to convert
     * @return WalletDTO representing the wallet
     */
    private WalletDTO convertToDTO(Wallet wallet) {
        return new WalletDTO(wallet.getCustomerId(), wallet.getBalance());
    }

    /**
     * Converts a Transaction entity to a TransactionDTO.
     *
     * @param transaction The Transaction entity to convert
     * @return TransactionDTO representing the transaction
     */
    private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getWallet().getCustomerId(),
                transaction.getAmount(),
                transaction.getTimestamp()
        );
    }
}