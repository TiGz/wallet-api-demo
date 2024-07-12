package org.github.tigz.wallet.repository;

import org.github.tigz.wallet.model.Transaction;
import org.github.tigz.wallet.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByWallet(Wallet wallet, Pageable pageable);
}