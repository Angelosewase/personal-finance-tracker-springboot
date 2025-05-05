package com.finebk.api.service;

import com.finebk.api.exception.ResourceNotFoundException;
import com.finebk.api.model.account.Account;
import com.finebk.api.model.account.Balance;
import com.finebk.api.payload.AccountRequest;
import com.finebk.api.repository.AccountRepository;
import com.finebk.api.repository.BalanceRepository;
import com.finebk.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserService userService;

    public Account createAccount(AccountRequest accountRequest, UserPrincipal currentUser) {
        Account account = new Account(
                accountRequest.getName(),
                accountRequest.getType(),
                accountRequest.getInitialBalance()
        );

        account.setUser(userService.getUser(currentUser));
        Account savedAccount = accountRepository.save(account);

        // Create initial balance record
        Balance balance = new Balance(accountRequest.getInitialBalance(), savedAccount);
        balanceRepository.save(balance);

        return savedAccount;
    }

    public Page<Account> getAccountsByUserId(UserPrincipal currentUser, Pageable pageable) {
        return accountRepository.findByCreatedBy(currentUser.getId(), pageable);
    }

    public Account getAccountById(Long accountId, UserPrincipal currentUser) {
        return accountRepository.findByIdAndCreatedBy(accountId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));
    }

    @Transactional
    public Account updateAccount(Long accountId, AccountRequest accountRequest, UserPrincipal currentUser) {
        Account account = getAccountById(accountId, currentUser);
        
        account.setName(accountRequest.getName());
        account.setType(accountRequest.getType());
        
        if (!account.getInitialBalance().equals(accountRequest.getInitialBalance())) {
            account.setInitialBalance(accountRequest.getInitialBalance());
            // Update the initial balance record
            Balance initialBalance = balanceRepository.findFirstByAccountOrderByCreatedAtAsc(account)
                    .orElseThrow(() -> new ResourceNotFoundException("Balance", "accountId", accountId));
            initialBalance.setAmount(accountRequest.getInitialBalance());
            balanceRepository.save(initialBalance);
        }
        
        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long accountId, UserPrincipal currentUser) {
        Account account = getAccountById(accountId, currentUser);
        balanceRepository.deleteByAccount(account);
        accountRepository.delete(account);
    }

    public Double getAccountBalance(Long accountId, UserPrincipal currentUser) {
        Account account = getAccountById(accountId, currentUser);
        return balanceRepository.findLatestBalanceByAccount(account)
                .map(Balance::getAmount)
                .orElse(account.getInitialBalance());
    }
}