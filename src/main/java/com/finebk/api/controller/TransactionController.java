package com.finebk.api.controller;

import com.finebk.api.model.transaction.Transaction;
import com.finebk.api.model.transaction.TransactionStatus;
import com.finebk.api.model.transaction.TransactionType;
import com.finebk.api.payload.ApiResponse;
import com.finebk.api.payload.PagedResponse;
import com.finebk.api.payload.TransactionRequest;
import com.finebk.api.payload.TransactionResponse;
import com.finebk.api.security.CurrentUser;
import com.finebk.api.security.UserPrincipal;
import com.finebk.api.service.TransactionService;
import com.finebk.api.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest,
                                             @CurrentUser UserPrincipal currentUser) {
        Transaction transaction = transactionService.createTransaction(transactionRequest, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{transactionId}")
                .buildAndExpand(transaction.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Transaction created successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<TransactionResponse> getTransactions(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "type", required = false) TransactionType type,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) TransactionStatus status,
            @RequestParam(value = "startDate", required = false) Instant startDate,
            @RequestParam(value = "endDate", required = false) Instant endDate,
            @CurrentUser UserPrincipal currentUser) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Transaction> transactions;

        if (type != null) {
            transactions = transactionService.getTransactionsByType(type, currentUser, pageable);
        } else if (category != null) {
            transactions = transactionService.getTransactionsByCategory(category, currentUser, pageable);
        } else if (status != null) {
            transactions = transactionService.getTransactionsByStatus(status, currentUser, pageable);
        } else if (startDate != null && endDate != null) {
            transactions = transactionService.getTransactionsByDateRange(startDate, endDate, currentUser, pageable);
        } else {
            transactions = transactionService.getAllTransactionsByUserId(currentUser, pageable);
        }

        if (transactions.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), transactions.getNumber(),
                    transactions.getSize(), transactions.getTotalElements(), transactions.getTotalPages(), transactions.isLast());
        }

        List<TransactionResponse> transactionResponses = transactions.map(transaction -> {
            TransactionResponse response = new TransactionResponse();
            response.setId(transaction.getId());
            response.setUserId(transaction.getUser().getId());
            response.setAmount(transaction.getAmount());
            response.setType(transaction.getType());
            response.setDescription(transaction.getDescription());
            response.setCategory(transaction.getCategory());
            response.setDate(transaction.getDate());
            response.setStatus(transaction.getStatus());
            response.setCreatedAt(transaction.getCreatedAt());
            response.setUpdatedAt(transaction.getUpdatedAt());
            return response;
        }).getContent();

        return new PagedResponse<>(transactionResponses, transactions.getNumber(),
                transactions.getSize(), transactions.getTotalElements(), transactions.getTotalPages(), transactions.isLast());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public TransactionResponse getTransactionById(@PathVariable(value = "id") Long id,
                                                @CurrentUser UserPrincipal currentUser) {
        Transaction transaction = transactionService.getTransactionById(id, currentUser);
        
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setUserId(transaction.getUser().getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setDescription(transaction.getDescription());
        response.setCategory(transaction.getCategory());
        response.setDate(transaction.getDate());
        response.setStatus(transaction.getStatus());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        
        return response;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public TransactionResponse updateTransaction(@PathVariable(value = "id") Long id,
                                               @Valid @RequestBody TransactionRequest transactionRequest,
                                               @CurrentUser UserPrincipal currentUser) {
        Transaction transaction = transactionService.updateTransaction(id, transactionRequest, currentUser);

        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setUserId(transaction.getUser().getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setDescription(transaction.getDescription());
        response.setCategory(transaction.getCategory());
        response.setDate(transaction.getDate());
        response.setStatus(transaction.getStatus());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());

        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteTransaction(@PathVariable(value = "id") Long id,
                                            @CurrentUser UserPrincipal currentUser) {
        transactionService.deleteTransaction(id, currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "Transaction deleted successfully"));
    }
}