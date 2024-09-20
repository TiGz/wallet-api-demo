package org.github.tigz.wallet.modules.wallet;

import org.github.tigz.wallet.modules.wallet.dto.WalletDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletApiApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_CUSTOMER_ID = "test-customer";
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1500");
    private static final BigDecimal WITHDRAWAL_AMOUNT = new BigDecimal("100");
    private static final int NUM_THREADS = 3;
    private static final int WITHDRAWALS_PER_THREAD = 5;

    @Test
    void testConcurrentWithdrawals() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(NUM_THREADS);
        AtomicInteger successfulWithdrawals = new AtomicInteger(0);

        // Initialize the wallet with 1500 credit
        addFunds(TEST_CUSTOMER_ID, INITIAL_BALANCE);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await(); // Wait for all threads to be ready
                    for (int j = 0; j < WITHDRAWALS_PER_THREAD; j++) {
                        try {
                            ResponseEntity<WalletDTO> response = withdrawFunds(TEST_CUSTOMER_ID, WITHDRAWAL_AMOUNT);
                            if (response.getStatusCode().is2xxSuccessful()) {
                                successfulWithdrawals.incrementAndGet();
                            }
                        } catch (Exception e) {
                            // If a withdrawal fails, we don't increment the successfulWithdrawals counter
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // Start all threads
        endLatch.await(); // Wait for all threads to finish
        executorService.shutdown();

        // Verify the final balance
        ResponseEntity<WalletDTO> finalWalletResponse = getWallet(TEST_CUSTOMER_ID);
        Assertions.assertTrue(finalWalletResponse.getStatusCode().is2xxSuccessful(), "Failed to get final wallet balance");
        WalletDTO finalWallet = finalWalletResponse.getBody();
        BigDecimal expectedFinalBalance = INITIAL_BALANCE.subtract(WITHDRAWAL_AMOUNT.multiply(BigDecimal.valueOf(successfulWithdrawals.get())));
        Assertions.assertTrue(finalWallet.getBalance().compareTo(expectedFinalBalance) == 0,
                "The final balance should match the expected balance after all withdrawals");

        // Verify that the correct number of withdrawals were successful
        int expectedSuccessfulWithdrawals = 15; // 1500 / 100 = 15 successful withdrawals
        Assertions.assertEquals(expectedSuccessfulWithdrawals, successfulWithdrawals.get(),
                "The number of successful withdrawals should match the expected value");
    }

    private ResponseEntity<WalletDTO> addFunds(String customerId, BigDecimal amount) {
        String url = "/api/wallet/" + customerId + "/add";
        HttpEntity<String> request = new HttpEntity<>(createJsonBody(amount), createJsonHeaders());
        return restTemplate.postForEntity(url, request, WalletDTO.class);
    }

    private ResponseEntity<WalletDTO> withdrawFunds(String customerId, BigDecimal amount) {
        String url = "/api/wallet/" + customerId + "/withdraw";
        HttpEntity<String> request = new HttpEntity<>(createJsonBody(amount), createJsonHeaders());
        return restTemplate.postForEntity(url, request, WalletDTO.class);
    }

    private ResponseEntity<WalletDTO> getWallet(String customerId) {
        String url = "/api/wallet/" + customerId;
        return restTemplate.getForEntity(url, WalletDTO.class);
    }

    private String createJsonBody(BigDecimal amount) {
        return String.format("{\"amount\": %s}", amount.toString());
    }

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}