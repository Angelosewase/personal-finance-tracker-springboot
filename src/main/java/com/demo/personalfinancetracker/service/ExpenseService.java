package com.demo.personalfinancetracker.service;

import com.demo.personalfinancetracker.exception.ResourceNotFoundException;
import com.demo.personalfinancetracker.model.Expense;
import com.demo.personalfinancetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpenses(LocalDate startDate, String endDate) {
        if (startDate != null && endDate != null) {
            return expenseRepository.findByDateBetween(startDate, LocalDate.parse(endDate));
        }
        return expenseRepository.findAll();
    }

    public Double getTotalAmount(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByDateBetween(startDate, endDate)
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public boolean deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found for this id :: " + id));
        expenseRepository.delete(expense);
        return true;
    }

    public Expense updateExpense(Long id, Expense expenseDetails) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found for this id :: " + id));
        expense.setDescription(expenseDetails.getDescription());
        expense.setAmount(expenseDetails.getAmount());
        expense.setDate(expenseDetails.getDate());
        return expenseRepository.save(expense);
    }
}
