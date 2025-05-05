package com.finebk.api.repository;

import com.finebk.api.model.transaction.Transaction;
import com.finebk.api.model.transaction.TransactionStatus;
import com.finebk.api.model.transaction.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUserId(Long userId, Pageable pageable);
    
    Page<Transaction> findByUserIdAndType(Long userId, TransactionType type, Pageable pageable);
    
    Page<Transaction> findByUserIdAndCategory(Long userId, String category, Pageable pageable);
    
    Page<Transaction> findByUserIdAndStatus(Long userId, TransactionStatus status, Pageable pageable);
    
    Page<Transaction> findByUserIdAndDateBetween(
        Long userId, 
        Instant startDate, 
        Instant endDate, 
        Pageable pageable
    );
    
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
    
    void deleteByIdAndUserId(Long id, Long userId);
}