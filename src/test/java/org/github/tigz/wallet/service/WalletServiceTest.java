package org.github.tigz.wallet.modules.wallet.service;

import org.github.tigz.wallet.modules.wallet.config.WalletConfig;
import org.github.tigz.wallet.modules.wallet.dto.PageDTO;
import org.github.tigz.wallet.modules.wallet.dto.TransactionDTO;
import org.github.tigz.wallet.modules.wallet.dto.WalletDTO;
import org.github.tigz.wallet.modules.wallet.model.Transaction;
import org.github.tigz.wallet.modules.wallet.model.Wallet;
import org.github.tigz.wallet.modules.wallet.repository.TransactionRepository;
import org.github.tigz.wallet.modules.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletConfig walletConfig;

    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletService = new WalletService(walletRepository, transactionRepository, walletConfig);

        when(walletConfig.getMinAddAmount()).thenReturn(new BigDecimal("1.00"));
        when(walletConfig.getMaxAddAmount()).thenReturn(new BigDecimal("1000.00"));
        when(walletConfig.getMinWithdrawAmount()).thenReturn(new BigDecimal("1.00"));
        when(walletConfig.getMaxWithdrawAmount()).thenReturn(new BigDecimal("500.00"));
    }

    @Test
    void addFunds_newWallet_success() {
        String customerId = "customer1";
        BigDecimal amount = new BigDecimal("100.00");
        Wallet newWallet = new Wallet(customerId, BigDecimal.ZERO);
        Wallet updatedWallet = new Wallet(customerId, amount);

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(walletRepository.save(any(Wallet.class))).thenReturn(newWallet).thenReturn(updatedWallet);

        WalletDTO result = walletService.addFunds(customerId, amount);

        assertEquals(customerId, result.getCustomerId());
        assertEquals(amount, result.getBalance());
        verify(walletRepository, times(2)).save(any(Wallet.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void addFunds_existingWallet_success() {
        String customerId = "customer1";
        BigDecimal initialBalance = new BigDecimal("50.00");
        BigDecimal amount = new BigDecimal("100.00");
        Wallet existingWallet = new Wallet(customerId, initialBalance);

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(existingWallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(existingWallet);

        WalletDTO result = walletService.addFunds(customerId, amount);

        assertEquals(customerId, result.getCustomerId());
        assertEquals(initialBalance.add(amount), result.getBalance());
        verify(walletRepository).save(any(Wallet.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void addFunds_invalidAmount_throwsException() {
        String customerId = "customer1";
        BigDecimal invalidAmount = new BigDecimal("0.50");

        assertThrows(IllegalArgumentException.class, () -> walletService.addFunds(customerId, invalidAmount));
    }

    @Test
    void withdrawFunds_success() {
        String customerId = "customer1";
        BigDecimal initialBalance = new BigDecimal("200.00");
        BigDecimal amount = new BigDecimal("50.00");
        Wallet existingWallet = new Wallet(customerId, initialBalance);

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(existingWallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(existingWallet);

        WalletDTO result = walletService.withdrawFunds(customerId, amount);

        assertEquals(customerId, result.getCustomerId());
        assertEquals(initialBalance.subtract(amount), result.getBalance());
        verify(walletRepository).save(any(Wallet.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void withdrawFunds_insufficientFunds_throwsException() {
        String customerId = "customer1";
        BigDecimal initialBalance = new BigDecimal("50.00");
        BigDecimal amount = new BigDecimal("100.00");
        Wallet existingWallet = new Wallet(customerId, initialBalance);

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(existingWallet));

        assertThrows(IllegalStateException.class, () -> walletService.withdrawFunds(customerId, amount));
    }

    @Test
    void withdrawFunds_walletNotFound_throwsException() {
        String customerId = "customer1";
        BigDecimal amount = new BigDecimal("50.00");

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> walletService.withdrawFunds(customerId, amount));
    }

    @Test
    void getTransactions_success() {
        String customerId = "customer1";
        Wallet wallet = new Wallet(customerId, new BigDecimal("100.00"));
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction1 = new Transaction(wallet, new BigDecimal("50.00"), Transaction.TransactionType.CREDIT);
        transaction1.setId(1L);
        transaction1.setTimestamp(now.minusDays(1));

        Transaction transaction2 = new Transaction(wallet, new BigDecimal("25.00"), Transaction.TransactionType.DEBIT);
        transaction2.setId(2L);
        transaction2.setTimestamp(now);

        Page<Transaction> transactionPage = new PageImpl<>(Arrays.asList(transaction1, transaction2));

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findByWallet(wallet, pageable)).thenReturn(transactionPage);

        PageDTO<TransactionDTO> result = walletService.getTransactions(customerId, pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPageNumber());
        assertEquals(2, result.getPageSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        TransactionDTO firstTransaction = result.getContent().get(0);
        assertEquals(1L, firstTransaction.getId());
        assertEquals(customerId, firstTransaction.getCustomerId());
        assertEquals(new BigDecimal("50.00"), firstTransaction.getAmount());
        assertEquals(now.minusDays(1), firstTransaction.getTimestamp());

        TransactionDTO secondTransaction = result.getContent().get(1);
        assertEquals(2L, secondTransaction.getId());
        assertEquals(customerId, secondTransaction.getCustomerId());
        assertEquals(new BigDecimal("25.00"), secondTransaction.getAmount());
        assertEquals(now, secondTransaction.getTimestamp());
    }

    @Test
    void getWallet_success() {
        String customerId = "customer1";
        BigDecimal balance = new BigDecimal("100.00");
        Wallet wallet = new Wallet(customerId, balance);

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wallet));

        WalletDTO result = walletService.getWallet(customerId);

        assertEquals(customerId, result.getCustomerId());
        assertEquals(balance, result.getBalance());
    }

    @Test
    void getWallet_notFound_throwsException() {
        String customerId = "customer1";

        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> walletService.getWallet(customerId));
    }
}