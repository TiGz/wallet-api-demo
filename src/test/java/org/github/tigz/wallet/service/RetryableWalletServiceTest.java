package org.github.tigz.wallet.service;

import org.github.tigz.wallet.dto.PageDTO;
import org.github.tigz.wallet.dto.TransactionDTO;
import org.github.tigz.wallet.dto.WalletDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RetryableWalletServiceTest {

    @Mock
    private WalletService walletService;

    private RetryableWalletService retryableWalletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        retryableWalletService = new RetryableWalletService(walletService);
    }

    @Test
    void addFunds_success() {
        String customerId = "customer1";
        BigDecimal amount = new BigDecimal("100.00");
        WalletDTO expectedWalletDTO = new WalletDTO(customerId, amount);

        when(walletService.addFunds(customerId, amount)).thenReturn(expectedWalletDTO);

        WalletDTO result = retryableWalletService.addFunds(customerId, amount);

        assertEquals(expectedWalletDTO, result);
        verify(walletService, times(1)).addFunds(customerId, amount);
    }

    @Test
    void withdrawFunds_success() {
        String customerId = "customer1";
        BigDecimal amount = new BigDecimal("50.00");
        WalletDTO expectedWalletDTO = new WalletDTO(customerId, new BigDecimal("150.00"));

        when(walletService.withdrawFunds(customerId, amount)).thenReturn(expectedWalletDTO);

        WalletDTO result = retryableWalletService.withdrawFunds(customerId, amount);

        assertEquals(expectedWalletDTO, result);
        verify(walletService, times(1)).withdrawFunds(customerId, amount);
    }

    @Test
    void getTransactions_success() {
        String customerId = "customer1";
        Pageable pageable = PageRequest.of(0, 10);
        PageDTO<TransactionDTO> expectedPageDTO = new PageDTO<>(new ArrayList<>(), 0, 10, 0, 0);

        when(walletService.getTransactions(customerId, pageable)).thenReturn(expectedPageDTO);

        PageDTO<TransactionDTO> result = retryableWalletService.getTransactions(customerId, pageable);

        assertEquals(expectedPageDTO, result);
        verify(walletService, times(1)).getTransactions(customerId, pageable);
    }

    @Test
    void getWallet_success() {
        String customerId = "customer1";
        WalletDTO expectedWalletDTO = new WalletDTO(customerId, new BigDecimal("200.00"));

        when(walletService.getWallet(customerId)).thenReturn(expectedWalletDTO);

        WalletDTO result = retryableWalletService.getWallet(customerId);

        assertEquals(expectedWalletDTO, result);
        verify(walletService, times(1)).getWallet(customerId);
    }
}