package com.finebk.api.repository;

import com.finebk.api.model.expense.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByIdAndUserId(Long id, Long userId);
    
    Page<Expense> findByUserId(Long userId, Pageable pageable);
    
    Page<Expense> findByUserIdAndDateBetween(
        Long userId,
        Instant startDate,
        Instant endDate,
        Pageable pageable
    );
    
    Page<Expense> findByUserIdAndCategory(
        Long userId,
        String category,
        Pageable pageable
    );
    
    List<String> findDistinctCategoriesByUserId(Long userId);
    
    boolean existsByIdAndUserId(Long id, Long userId);
}