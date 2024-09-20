package org.github.tigz.wallet.modules.wallet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a transaction entity in the system.
 * This class is mapped to the "transactions" table in the database.
 */
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime timestamp;

    /**
     * Enum representing the type of transaction.
     */
    public enum TransactionType {
        CREDIT, DEBIT
    }

    /**
     * Default constructor for JPA.
     */
    public Transaction() {
    }

    /**
     * Constructs a new Transaction with the specified wallet, amount, and type.
     *
     * @param wallet The wallet associated with this transaction
     * @param amount The amount of the transaction
     * @param type The type of the transaction (CREDIT or DEBIT)
     */
    public Transaction(Wallet wallet, BigDecimal amount, TransactionType type) {
        this.wallet = wallet;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Gets the unique identifier of the transaction.
     *
     * @return The transaction's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the transaction.
     *
     * @param id The transaction's ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the wallet associated with this transaction.
     *
     * @return The associated wallet
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Sets the wallet associated with this transaction.
     *
     * @param wallet The wallet to associate with this transaction
     */
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    /**
     * Gets the amount of the transaction.
     *
     * @return The transaction amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction.
     *
     * @param amount The transaction amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Gets the type of the transaction.
     *
     * @return The transaction type (CREDIT or DEBIT)
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Sets the type of the transaction.
     *
     * @param type The transaction type (CREDIT or DEBIT)
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

    /**
     * Gets the timestamp of when the transaction occurred.
     *
     * @return The transaction timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of when the transaction occurred.
     *
     * @param timestamp The transaction timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}