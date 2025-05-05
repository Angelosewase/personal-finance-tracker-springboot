package com.finebk.api.controller;

import com.finebk.api.payload.ApiResponse;
import com.finebk.api.payload.ExpenseRequest;
import com.finebk.api.payload.ExpenseResponse;
import com.finebk.api.security.CurrentUser;
import com.finebk.api.security.UserPrincipal;
import com.finebk.api.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest expenseRequest,
            @CurrentUser UserPrincipal currentUser
    ) {
        ExpenseResponse expense = expenseService.addExpense(expenseRequest, currentUser);
        return new ResponseEntity<>(expense, HttpStatus.CREATED);
    }

    @GetMapping("/{expenseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ExpenseResponse> getExpense(
            @PathVariable Long expenseId,
            @CurrentUser UserPrincipal currentUser
    ) {
        ExpenseResponse expense = expenseService.getExpense(expenseId, currentUser);
        return ResponseEntity.ok(expense);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<ExpenseResponse>> getAllExpenses(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<ExpenseResponse> expenses = expenseService.getAllExpenses(currentUser, pageable);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<ExpenseResponse>> getExpensesByCategory(
            @PathVariable String category,
            @CurrentUser UserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<ExpenseResponse> expenses = expenseService.getExpensesByCategory(currentUser, category, pageable);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<ExpenseResponse>> getExpensesByDateRange(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate,
            @CurrentUser UserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(currentUser, startDate, endDate, pageable);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<String>> getUserCategories(@CurrentUser UserPrincipal currentUser) {
        List<String> categories = expenseService.getUserCategories(currentUser);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{expenseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long expenseId,
            @Valid @RequestBody ExpenseRequest expenseRequest,
            @CurrentUser UserPrincipal currentUser
    ) {
        ExpenseResponse updatedExpense = expenseService.updateExpense(expenseId, expenseRequest, currentUser);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{expenseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> deleteExpense(
            @PathVariable Long expenseId,
            @CurrentUser UserPrincipal currentUser
    ) {
        expenseService.deleteExpense(expenseId, currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "Expense deleted successfully"));
    }
}