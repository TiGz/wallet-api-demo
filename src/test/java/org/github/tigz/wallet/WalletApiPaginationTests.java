package org.github.tigz.wallet;

import org.github.tigz.wallet.modules.wallet.dto.PageDTO;
import org.github.tigz.wallet.modules.wallet.dto.TransactionDTO;
import org.github.tigz.wallet.modules.wallet.dto.WalletDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletApiPaginationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_CUSTOMER_ID = "pagination-test-customer";
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000");
    private static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal("10");
    private static final int WITHDRAWAL_TRANSACTIONS = 50;
    private static final int TOTAL_TRANSACTIONS = WITHDRAWAL_TRANSACTIONS + 1; // Include initial deposit

    @BeforeEach
    void setUp() {
        // Initialize the wallet and create test transactions
        initializeWallet();
        createTestTransactions();
    }

    @Test
    void testTransactionPagination() {
        // Test first page
        PageDTO<TransactionDTO> firstPage = getTransactionsPage(0, 10);
        assertEquals(10, firstPage.getContent().size());
        assertEquals(0, firstPage.getPageNumber());
        assertEquals(10, firstPage.getPageSize());
        assertEquals(TOTAL_TRANSACTIONS, firstPage.getTotalElements());
        assertEquals(6, firstPage.getTotalPages());

        // Test last page
        PageDTO<TransactionDTO> lastPage = getTransactionsPage(5, 10);
        assertEquals(1, lastPage.getContent().size());
        assertEquals(5, lastPage.getPageNumber());
        assertEquals(10, lastPage.getPageSize());
        assertEquals(TOTAL_TRANSACTIONS, lastPage.getTotalElements());
        assertEquals(6, lastPage.getTotalPages());

        // Test middle page
        PageDTO<TransactionDTO> middlePage = getTransactionsPage(2, 10);
        assertEquals(10, middlePage.getContent().size());
        assertEquals(2, middlePage.getPageNumber());
        assertEquals(10, middlePage.getPageSize());
        assertEquals(TOTAL_TRANSACTIONS, middlePage.getTotalElements());
        assertEquals(6, middlePage.getTotalPages());

        // Test with different page size
        PageDTO<TransactionDTO> differentSizePage = getTransactionsPage(0, 20);
        assertEquals(20, differentSizePage.getContent().size());
        assertEquals(0, differentSizePage.getPageNumber());
        assertEquals(20, differentSizePage.getPageSize());
        assertEquals(TOTAL_TRANSACTIONS, differentSizePage.getTotalElements());
        assertEquals(3, differentSizePage.getTotalPages());
    }

    private void initializeWallet() {
        ResponseEntity<WalletDTO> response = addFunds(TEST_CUSTOMER_ID, INITIAL_BALANCE);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Failed to initialize wallet");
    }

    private void createTestTransactions() {
        for (int i = 0; i < WITHDRAWAL_TRANSACTIONS; i++) {
            ResponseEntity<WalletDTO> response = withdrawFunds(TEST_CUSTOMER_ID, TRANSACTION_AMOUNT);
            assertTrue(response.getStatusCode().is2xxSuccessful(), "Failed to create test transaction");
        }
    }

    private PageDTO<TransactionDTO> getTransactionsPage(int page, int size) {
        String url = String.format("/api/wallet/%s/transactions?page=%d&size=%d", TEST_CUSTOMER_ID, page, size);
        ResponseEntity<PageDTO<TransactionDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageDTO<TransactionDTO>>() {}
        );
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Failed to get transactions page");
        return response.getBody();
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

    private String createJsonBody(BigDecimal amount) {
        return String.format("{\"amount\": %s}", amount.toString());
    }

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}