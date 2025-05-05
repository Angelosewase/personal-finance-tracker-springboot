package com.finebk.api.controller;

import com.finebk.api.model.account.Account;
import com.finebk.api.payload.AccountRequest;
import com.finebk.api.payload.ApiResponse;
import com.finebk.api.security.CurrentUser;
import com.finebk.api.security.UserPrincipal;
import com.finebk.api.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountRequest accountRequest,
                                         @CurrentUser UserPrincipal currentUser) {
        Account account = accountService.createAccount(accountRequest, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(account.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Account created successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<Account> getAccounts(@CurrentUser UserPrincipal currentUser,
                                   Pageable pageable) {
        return accountService.getAccountsByUserId(currentUser, pageable);
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public Account getAccountById(@PathVariable Long accountId,
                                @CurrentUser UserPrincipal currentUser) {
        return accountService.getAccountById(accountId, currentUser);
    }

    @PutMapping("/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public Account updateAccount(@PathVariable Long accountId,
                               @Valid @RequestBody AccountRequest accountRequest,
                               @CurrentUser UserPrincipal currentUser) {
        return accountService.updateAccount(accountId, accountRequest, currentUser);
    }

    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId,
                                         @CurrentUser UserPrincipal currentUser) {
        accountService.deleteAccount(accountId, currentUser);
        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Account deleted successfully"));
    }

    @GetMapping("/{accountId}/balance")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAccountBalance(@PathVariable Long accountId,
                                             @CurrentUser UserPrincipal currentUser) {
        Double balance = accountService.getAccountBalance(accountId, currentUser);
        return ResponseEntity.ok(balance);
    }
}