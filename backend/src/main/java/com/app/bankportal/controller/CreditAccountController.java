package com.app.bankportal.controller;

import com.app.bankportal.dto.*;
import com.app.bankportal.mapper.CreditAccountMapper;
import com.app.bankportal.model.CreditAccount;
import com.app.bankportal.service.CreditAccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/credit-accounts")
public class CreditAccountController {

    private final CreditAccountService creditAccountService;
    private final CreditAccountMapper creditAccountMapper;

    public CreditAccountController(
            CreditAccountService creditAccountService,
            CreditAccountMapper creditAccountMapper) {

        this.creditAccountService = creditAccountService;
        this.creditAccountMapper = creditAccountMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<CreditAccountResponse> createCreditCard(
            @PathVariable Long userId,
            @RequestBody CreateCreditCardRequest request) {

        request.setUserId(userId);

        CreditAccount account =
                creditAccountService.createCreditCard(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }

    @GetMapping("")
    public ResponseEntity<List<CreditAccountResponse>> getCreditAccounts(
            @PathVariable Long userId) {

        GetCreditAccountsRequest request =
                new GetCreditAccountsRequest();

        request.setUserId(userId);

        List<CreditAccount> accounts =
                creditAccountService.getCreditAccounts(request);

        List<CreditAccountResponse> response =
                accounts.stream()
                        .map(creditAccountMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountId}/charge")
    public ResponseEntity<CreditAccountResponse> charge(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestBody ChargeRequest request) {

        request.setUserId(userId);
        request.setAccountId(accountId);

        CreditAccount account =
                creditAccountService.charge(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }

    @PostMapping("/{accountId}/payment")
    public ResponseEntity<CreditAccountResponse> makePayment(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestBody PayCreditCardRequest request) {

        request.setUserId(userId);
        request.setAccountId(accountId);

        CreditAccount account =
                creditAccountService.makePayment(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }

    @PutMapping("/{accountId}/interest")
    public ResponseEntity<CreditAccountResponse> updateInterestRate(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestBody UpdateInterestRateRequest request) {

        request.setUserId(userId);
        request.setAccountId(accountId);

        CreditAccount account =
                creditAccountService.updateInterestRate(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }

    @PostMapping("/{accountId}/check-payment")
    public ResponseEntity<CreditAccountResponse> checkMinPayment(
            @PathVariable Long userId,
            @PathVariable Long accountId) {

        MinPaymentCheckRequest request =
                new MinPaymentCheckRequest();

        request.setUserId(userId);
        request.setAccountId(accountId);

        CreditAccount account =
                creditAccountService.checkMinPayment(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }
}