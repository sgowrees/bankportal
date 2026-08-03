package com.app.bankportal.controller;

import com.app.bankportal.dto.*;
import com.app.bankportal.mapper.CreditAccountMapper;
import com.app.bankportal.model.CreditAccount;
import com.app.bankportal.model.User;
import com.app.bankportal.repository.UserRepository;
import com.app.bankportal.service.CreditAccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-accounts")
public class CreditAccountController {

    private final CreditAccountService creditAccountService;
    private final CreditAccountMapper creditAccountMapper;
    private final UserRepository userRepository;

    public CreditAccountController(
            CreditAccountService creditAccountService,
            CreditAccountMapper creditAccountMapper,
            UserRepository userRepository) {

        this.creditAccountService = creditAccountService;
        this.creditAccountMapper = creditAccountMapper;
        this.userRepository = userRepository;
    }


    @PostMapping("/create")
    public ResponseEntity<CreditAccountResponse> createCreditCard(
            Authentication authentication,
            @RequestBody CreateCreditCardRequest request) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        request.setUserId(user.getId());

        CreditAccount account =
                creditAccountService.createCreditCard(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }


    @GetMapping("")
    public ResponseEntity<List<CreditAccountResponse>> getCreditAccounts(
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        GetCreditAccountsRequest request =
                new GetCreditAccountsRequest();

        request.setUserId(user.getId());

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
            Authentication authentication,
            @PathVariable Long accountId,
            @RequestBody ChargeRequest request) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        request.setUserId(user.getId());
        request.setAccountId(accountId);

        CreditAccount account =
                creditAccountService.charge(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }


    @PostMapping("/{accountId}/payment")
    public ResponseEntity<CreditAccountResponse> makePayment(
            Authentication authentication,
            @PathVariable Long accountId,
            @RequestBody PayCreditCardRequest request) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        request.setUserId(user.getId());
        request.setAccountId(accountId);

        CreditAccount account =
                creditAccountService.makePayment(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }


    @PutMapping("/{accountId}/interest")
    public ResponseEntity<CreditAccountResponse> updateInterestRate(
            Authentication authentication,
            @PathVariable Long accountId,
            @RequestBody UpdateInterestRateRequest request) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        request.setUserId(user.getId());
        request.setAccountId(accountId);

        CreditAccount account =
                creditAccountService.updateInterestRate(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }


    @PostMapping("/{accountId}/check-payment")
    public ResponseEntity<CreditAccountResponse> checkMinPayment(
            Authentication authentication,
            @PathVariable Long accountId) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        MinPaymentCheckRequest request =
                new MinPaymentCheckRequest();

        request.setUserId(user.getId());
        request.setAccountId(accountId);

        CreditAccount account =
                creditAccountService.checkMinPayment(request);

        return ResponseEntity.ok(
                creditAccountMapper.toResponse(account)
        );
    }
}