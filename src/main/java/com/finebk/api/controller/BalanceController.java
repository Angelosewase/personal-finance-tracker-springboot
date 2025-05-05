package com.finebk.api.controller;

import com.finebk.api.model.account.Balance;
import com.finebk.api.payload.ApiResponse;
import com.finebk.api.security.CurrentUser;
import com.finebk.api.security.UserPrincipal;
import com.finebk.api.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balances")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public Page<Balance> getBalancesByAccount(@PathVariable Long accountId,
                                            @CurrentUser UserPrincipal currentUser,
                                            Pageable pageable) {
        return balanceService.getBalancesByAccountId(accountId, currentUser, pageable);
    }

    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasRole('USER')")
    public Balance getBalanceByTransaction(@PathVariable Long transactionId,
                                         @CurrentUser UserPrincipal currentUser) {
        return balanceService.getBalanceByTransactionId(transactionId, currentUser);
    }

    @GetMapping("/goal/{goalId}")
    @PreAuthorize("hasRole('USER')")
    public Balance getBalanceByGoal(@PathVariable Long goalId,
                                   @CurrentUser UserPrincipal currentUser) {
        return balanceService.getBalanceByGoalId(goalId, currentUser);
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTotalBalance(@CurrentUser UserPrincipal currentUser) {
        Double totalBalance = balanceService.getTotalBalance(currentUser);
        return ResponseEntity.ok(totalBalance);
    }

    @GetMapping("/summary/by-account-type")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getBalancesByAccountType(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(balanceService.getBalancesByAccountType(currentUser));
    }
}