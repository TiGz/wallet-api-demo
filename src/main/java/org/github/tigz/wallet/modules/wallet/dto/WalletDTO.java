package org.github.tigz.wallet.modules.wallet.dto;

import java.math.BigDecimal;

public class WalletDTO {
    private String customerId;
    private BigDecimal balance;

    public WalletDTO() {
    }

    public WalletDTO(String customerId, BigDecimal balance) {
        this.customerId = customerId;
        this.balance = balance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}