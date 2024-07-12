package org.github.tigz.wallet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a wallet entity in the system.
 * This class is mapped to the "wallets" table in the database.
 */
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String customerId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Version
    private Long version;

    /**
     * Default constructor for JPA.
     */
    public Wallet() {
    }

    /**
     * Constructs a new Wallet with the specified customer ID and balance.
     *
     * @param customerId The unique identifier for the customer
     * @param balance The initial balance of the wallet
     */
    public Wallet(String customerId, BigDecimal balance) {
        this.customerId = customerId;
        this.balance = balance;
    }

    /**
     * Gets the unique identifier of the wallet.
     *
     * @return The wallet's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the wallet.
     *
     * @param id The wallet's ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the customer ID associated with this wallet.
     *
     * @return The customer ID
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID associated with this wallet.
     *
     * @param customerId The customer ID
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the current balance of the wallet.
     *
     * @return The wallet's balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the wallet.
     *
     * @param balance The new balance
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Gets the version of the wallet entity.
     * This is used for optimistic locking in JPA.
     *
     * @return The version number
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version of the wallet entity.
     *
     * @param version The new version number
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}