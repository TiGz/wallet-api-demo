package org.github.tigz.wallet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "wallet")
@EnableRetry
public class WalletConfig {
    private BigDecimal minAddAmount;
    private BigDecimal maxAddAmount;
    private BigDecimal minWithdrawAmount;
    private BigDecimal maxWithdrawAmount;

    @Bean
    public Logger walletOperationsLogger() {
        return LoggerFactory.getLogger("WalletOperations");
    }

    public BigDecimal getMinAddAmount() {
        return minAddAmount;
    }

    public void setMinAddAmount(BigDecimal minAddAmount) {
        this.minAddAmount = minAddAmount;
    }

    public BigDecimal getMaxAddAmount() {
        return maxAddAmount;
    }

    public void setMaxAddAmount(BigDecimal maxAddAmount) {
        this.maxAddAmount = maxAddAmount;
    }

    public BigDecimal getMinWithdrawAmount() {
        return minWithdrawAmount;
    }

    public void setMinWithdrawAmount(BigDecimal minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }

    public BigDecimal getMaxWithdrawAmount() {
        return maxWithdrawAmount;
    }

    public void setMaxWithdrawAmount(BigDecimal maxWithdrawAmount) {
        this.maxWithdrawAmount = maxWithdrawAmount;
    }
}