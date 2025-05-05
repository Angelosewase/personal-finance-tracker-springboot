package com.finebk.api.service.impl;

import com.finebk.api.exception.ResourceNotFoundException;
import com.finebk.api.exception.UnauthorizedException;
import com.finebk.api.model.expense.Expense;
import com.finebk.api.model.user.User;
import com.finebk.api.payload.ApiResponse;
import com.finebk.api.payload.ExpenseRequest;
import com.finebk.api.payload.ExpenseResponse;
import com.finebk.api.repository.ExpenseRepository;
import com.finebk.api.repository.UserRepository;
import com.finebk.api.security.UserPrincipal;
import com.finebk.api.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ExpenseResponse addExpense(ExpenseRequest expenseRequest, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        Expense expense = new Expense(
                user,
                expenseRequest.getAmount(),
                expenseRequest.getCategory(),
                expenseRequest.getDescription(),
                expenseRequest.getDate()
        );

        Expense savedExpense = expenseRepository.save(expense);
        return convertToResponse(savedExpense);
    }

    @Override
    public ExpenseResponse getExpense(Long expenseId, UserPrincipal currentUser) {
        Expense expense = expenseRepository.findByIdAndUserId(expenseId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", expenseId));
        return convertToResponse(expense);
    }

    @Override
    public Page<ExpenseResponse> getAllExpenses(UserPrincipal currentUser, Pageable pageable) {
        return expenseRepository.findByUserId(currentUser.getId(), pageable)
                .map(this::convertToResponse);
    }

    @Override
    public Page<ExpenseResponse> getExpensesByDateRange(
            UserPrincipal currentUser,
            Instant startDate,
            Instant endDate,
            Pageable pageable
    ) {
        return expenseRepository.findByUserIdAndDateBetween(
                currentUser.getId(),
                startDate,
                endDate,
                pageable
        ).map(this::convertToResponse);
    }

    @Override
    public Page<ExpenseResponse> getExpensesByCategory(
            UserPrincipal currentUser,
            String category,
            Pageable pageable
    ) {
        return expenseRepository.findByUserIdAndCategory(
                currentUser.getId(),
                category,
                pageable
        ).map(this::convertToResponse);
    }

    @Override
    public List<String> getUserCategories(UserPrincipal currentUser) {
        return expenseRepository.findDistinctCategoriesByUserId(currentUser.getId());
    }

    @Override
    public ExpenseResponse updateExpense(
            Long expenseId,
            ExpenseRequest expenseRequest,
            UserPrincipal currentUser
    ) {
        Expense expense = expenseRepository.findByIdAndUserId(expenseId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", expenseId));

        expense.setAmount(expenseRequest.getAmount());
        expense.setCategory(expenseRequest.getCategory());
        expense.setDescription(expenseRequest.getDescription());
        expense.setDate(expenseRequest.getDate());

        Expense updatedExpense = expenseRepository.save(expense);
        return convertToResponse(updatedExpense);
    }

    @Override
    public void deleteExpense(Long expenseId, UserPrincipal currentUser) {
        if (!expenseRepository.existsByIdAndUserId(expenseId, currentUser.getId())) {
            throw new ResourceNotFoundException("Expense", "id", expenseId);
        }
        expenseRepository.deleteById(expenseId);
    }

    private ExpenseResponse convertToResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setUserId(expense.getUser().getId());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setDate(expense.getDate());
        response.setCreatedAt(expense.getCreatedAt());
        response.setUpdatedAt(expense.getUpdatedAt());
        return response;
    }
}