package com.app.bankportal.controller;

import com.app.bankportal.dto.*;
import com.app.bankportal.mapper.AccountMapper;
import com.app.bankportal.model.Account;
import com.app.bankportal.model.User;
import com.app.bankportal.repository.UserRepository;
import com.app.bankportal.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    public AccountController(
            AccountService accountService,
            AccountMapper accountMapper,
            UserRepository userRepository) {

        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(
            Authentication authentication,
            @RequestBody CreateAccountRequest request) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        request.setUserId(user.getId());

        Account account = accountService.createAccount(request);

        return ResponseEntity.ok(
                accountMapper.toResponse(account)
        );
    }

    @GetMapping("")
    public ResponseEntity<List<AccountResponse>> getAccounts(
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Account> accounts = accountService.getAccounts(user.getId());

        List<AccountResponse> response = accounts.stream()
                .map(accountMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            Authentication authentication,
            @PathVariable Long accountId,
            @RequestBody DepositRequest request) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        request.setUserId(user.getId());
        request.setAccountId(accountId);

        Account result = accountService.deposit(request);

        return ResponseEntity.ok(
                accountMapper.toResponse(result)
        );
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            Authentication authentication,
            @PathVariable Long accountId,
            @RequestBody WithdrawalRequest request) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        request.setUserId(user.getId());
        request.setAccountId(accountId);

        Account result = accountService.withdraw(request);

        return ResponseEntity.ok(
                accountMapper.toResponse(result)
        );
    }

    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<List<AccountResponse>> transfer(
            Authentication authentication,
            @PathVariable Long accountId,
            @RequestBody TransferAmount request) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        request.setUserId(user.getId());
        request.setAccountId(accountId);

        List<Account> accounts = accountService.transfer(request);

        List<AccountResponse> response = accounts.stream()
                .map(accountMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> removeAccount(
            Authentication authentication,
            @PathVariable Long accountId) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        DeleteAccountRequest request = new DeleteAccountRequest();

        request.setUserId(user.getId());
        request.setAccountId(accountId);

        accountService.removeAccount(request);

        return ResponseEntity.ok("Account removed successfully");
    }
}