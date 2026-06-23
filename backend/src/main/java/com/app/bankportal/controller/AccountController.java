package com.app.bankportal.controller;

import com.app.bankportal.dto.AccountResponse;
import com.app.bankportal.dto.DepositRequest;
import com.app.bankportal.mapper.AccountMapper;
import com.app.bankportal.model.Account;
import com.app.bankportal.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponse> deposit(@RequestBody DepositRequest request) {
        Account account = accountService.deposit(request);
        AccountResponse response = accountMapper.toResponse(account);
        return ResponseEntity.ok(response);
    }
}