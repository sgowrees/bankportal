package com.app.bankportal.controller;

import com.app.bankportal.dto.*;
import com.app.bankportal.mapper.AccountMapper;
import com.app.bankportal.model.Account;
import com.app.bankportal.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(
            AccountService accountService,
            AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(
            @PathVariable Long userId,
            @RequestBody CreateAccountRequest request) {

        request.setUserId(userId);

        Account account = accountService.createAccount(request);

        return ResponseEntity.ok(
                accountMapper.toResponse(account)
        );
    }

    @GetMapping("")
    public ResponseEntity<List<AccountResponse>> getAccounts(
            @PathVariable Long userId) {

        List<Account> accounts = accountService.getAccounts(userId);

        List<AccountResponse> response = accounts.stream()
                .map(accountMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestBody DepositRequest request) {

        request.setUserId(userId);
        request.setAccountId(accountId);

        Account result = accountService.deposit(request);

        return ResponseEntity.ok(
                accountMapper.toResponse(result)
        );
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestBody WithdrawalRequest request) {

        request.setUserId(userId);
        request.setAccountId(accountId);

        Account result = accountService.withdraw(request);

        return ResponseEntity.ok(
                accountMapper.toResponse(result)
        );
    }

    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<List<AccountResponse>> transfer(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestBody TransferAmount request) {

        request.setUserId(userId);
        request.setAccountId(accountId);

        List<Account> accounts = accountService.transfer(request);

        List<AccountResponse> response = accounts.stream()
                .map(accountMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> removeAccount(
            @PathVariable Long userId,
            @PathVariable Long accountId) {

        DeleteAccountRequest request = new DeleteAccountRequest();

        request.setUserId(userId);
        request.setAccountId(accountId);

        accountService.removeAccount(request);

        return ResponseEntity.ok("Account removed successfully");
    }
}