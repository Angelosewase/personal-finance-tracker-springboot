package com.finebk.api.service;

import com.finebk.api.exception.ResourceNotFoundException;
import com.finebk.api.model.account.Account;
import com.finebk.api.model.account.Balance;
import com.finebk.api.model.goal.Goal;
import com.finebk.api.model.transaction.Transaction;
import com.finebk.api.repository.AccountRepository;
import com.finebk.api.repository.BalanceRepository;
import com.finebk.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Page<Balance> getBalancesByAccountId(Long accountId, UserPrincipal currentUser, Pageable pageable) {
        Account account = accountRepository.findByIdAndCreatedBy(accountId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));
        return balanceRepository.findByAccount(account, pageable);
    }

    public Balance getBalanceByTransactionId(Long transactionId, UserPrincipal currentUser) {
        return balanceRepository.findByTransactionIdAndCreatedBy(transactionId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Balance", "transactionId", transactionId));
    }

    public Balance getBalanceByGoalId(Long goalId, UserPrincipal currentUser) {
        return balanceRepository.findByGoalIdAndCreatedBy(goalId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Balance", "goalId", goalId));
    }

    @Transactional
    public Balance createTransactionBalance(Transaction transaction, Account account, Double amount) {
        Balance balance = new Balance(amount, account);
        balance.setTransaction(transaction);
        return balanceRepository.save(balance);
    }

    @Transactional
    public Balance createGoalBalance(Goal goal, Account account, Double amount) {
        Balance balance = new Balance(amount, account);
        balance.setGoal(goal);
        return balanceRepository.save(balance);
    }

    public Double getTotalBalance(UserPrincipal currentUser) {
        return balanceRepository.findTotalBalanceByUser(currentUser.getId())
                .orElse(0.0);
    }

    public Map<String, Double> getBalancesByAccountType(UserPrincipal currentUser) {
        Map<String, Double> balancesByType = new HashMap<>();
        accountRepository.findByCreatedBy(currentUser.getId())
                .forEach(account -> {
                    Double latestBalance = balanceRepository.findLatestBalanceByAccount(account)
                            .map(Balance::getAmount)
                            .orElse(account.getInitialBalance());
                    balancesByType.merge(account.getType(), latestBalance, Double::sum);
                });
        return balancesByType;
    }
}