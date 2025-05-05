package com.finebk.api.service;

import com.finebk.api.model.expense.Expense;
import com.finebk.api.payload.ExpenseRequest;
import com.finebk.api.payload.ExpenseResponse;
import com.finebk.api.payload.PagedResponse;
import com.finebk.api.security.UserPrincipal;

import java.time.Instant;

public interface ExpenseService {
    PagedResponse<ExpenseResponse> getAllExpenses(UserPrincipal currentUser, int page, int size);

    ExpenseResponse getExpenseById(Long id, UserPrincipal currentUser);

    ExpenseResponse createExpense(ExpenseRequest expenseRequest, UserPrincipal currentUser);

    ExpenseResponse updateExpense(Long id, ExpenseRequest expenseRequest, UserPrincipal currentUser);

    void deleteExpense(Long id, UserPrincipal currentUser);

    PagedResponse<ExpenseResponse> getExpensesByCategory(String category, UserPrincipal currentUser, int page, int size);

    PagedResponse<ExpenseResponse> getExpensesByDateRange(Instant startDate, Instant endDate, UserPrincipal currentUser, int page, int size);
}