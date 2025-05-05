package com.finebk.api.service;

import com.finebk.api.exception.ResourceNotFoundException;
import com.finebk.api.exception.UnauthorizedException;
import com.finebk.api.model.transaction.Transaction;
import com.finebk.api.model.transaction.TransactionStatus;
import com.finebk.api.model.transaction.TransactionType;
import com.finebk.api.model.user.User;
import com.finebk.api.payload.TransactionRequest;
import com.finebk.api.repository.TransactionRepository;
import com.finebk.api.repository.UserRepository;
import com.finebk.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Transaction createTransaction(TransactionRequest transactionRequest, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        Transaction transaction = new Transaction(
                user,
                transactionRequest.getAmount(),
                transactionRequest.getType(),
                transactionRequest.getDescription(),
                transactionRequest.getCategory(),
                transactionRequest.getDate(),
                transactionRequest.getStatus()
        );

        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long transactionId, UserPrincipal currentUser) {
        return transactionRepository.findByIdAndUserId(transactionId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));
    }

    public Page<Transaction> getAllTransactionsByUserId(UserPrincipal currentUser, Pageable pageable) {
        return transactionRepository.findByUserId(currentUser.getId(), pageable);
    }

    public Page<Transaction> getTransactionsByType(TransactionType type, UserPrincipal currentUser, Pageable pageable) {
        return transactionRepository.findByUserIdAndType(currentUser.getId(), type, pageable);
    }

    public Page<Transaction> getTransactionsByCategory(String category, UserPrincipal currentUser, Pageable pageable) {
        return transactionRepository.findByUserIdAndCategory(currentUser.getId(), category, pageable);
    }

    public Page<Transaction> getTransactionsByStatus(TransactionStatus status, UserPrincipal currentUser, Pageable pageable) {
        return transactionRepository.findByUserIdAndStatus(currentUser.getId(), status, pageable);
    }

    public Page<Transaction> getTransactionsByDateRange(Instant startDate, Instant endDate,
                                                       UserPrincipal currentUser, Pageable pageable) {
        return transactionRepository.findByUserIdAndDateBetween(
                currentUser.getId(), startDate, endDate, pageable);
    }

    public Transaction updateTransaction(Long transactionId, TransactionRequest transactionRequest,
                                       UserPrincipal currentUser) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));

        transaction.setAmount(transactionRequest.getAmount());
        transaction.setType(transactionRequest.getType());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setCategory(transactionRequest.getCategory());
        transaction.setDate(transactionRequest.getDate());
        transaction.setStatus(transactionRequest.getStatus());

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long transactionId, UserPrincipal currentUser) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new ResourceNotFoundException("Transaction", "id", transactionId);
        }

        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, currentUser.getId())
                .orElseThrow(() -> new UnauthorizedException("You don't have permission to delete this transaction"));

        transactionRepository.delete(transaction);
    }
}