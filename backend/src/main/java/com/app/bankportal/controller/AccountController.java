package com.app.bankportal.controller;

import com.app.bankportal.dto.*;
import com.app.bankportal.mapper.AccountMapper;
import com.app.bankportal.model.Account;
import com.app.bankportal.model.IdempotencyRecord;
import com.app.bankportal.service.AccountService;
import com.app.bankportal.service.IdempotencyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final IdempotencyService idempotencyService;
    private final ObjectMapper objectMapper;

    public AccountController(
            AccountService accountService,
            AccountMapper accountMapper,
            IdempotencyService idempotencyService,
            ObjectMapper objectMapper) {

        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.idempotencyService = idempotencyService;
        this.objectMapper = objectMapper;
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
            @RequestHeader(
                    value = "Idempotency-Key",
                    required = false
            ) String idempotencyKey,
            @RequestBody DepositRequest request) {

        // Check if this request was already processed
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {

            Optional<IdempotencyRecord> existing =
                    idempotencyService.findByKey(idempotencyKey);

            if (existing.isPresent()) {

                try {
                    AccountResponse cached =
                            objectMapper.readValue(
                                    existing.get().getResponse(),
                                    AccountResponse.class
                            );

                    return ResponseEntity
                            .status(existing.get().getStatusCode())
                            .body(cached);

                } catch (JsonProcessingException e) {
                    return ResponseEntity.internalServerError().build();
                }
            }
        }

        // Set IDs from URL
        request.setUserId(userId);
        request.setAccountId(accountId);

        // Perform deposit
        Account account = accountService.deposit(request);

        AccountResponse response =
                accountMapper.toResponse(account);

        // Save response for idempotency
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {

            try {
                String responseJson =
                        objectMapper.writeValueAsString(response);

                idempotencyService.save(
                        idempotencyKey,
                        responseJson,
                        200
                );

            } catch (JsonProcessingException e) {
                return ResponseEntity.internalServerError().build();
            }
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestHeader(
                    value = "Idempotency-Key",
                    required = false
            ) String idempotencyKey,
            @RequestBody WithdrawalRequest request) {

        // Check if this request was already processed
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {

            Optional<IdempotencyRecord> existing =
                    idempotencyService.findByKey(idempotencyKey);

            if (existing.isPresent()) {

                try {
                    AccountResponse cached =
                            objectMapper.readValue(
                                    existing.get().getResponse(),
                                    AccountResponse.class
                            );

                    return ResponseEntity
                            .status(existing.get().getStatusCode())
                            .body(cached);

                } catch (JsonProcessingException e) {
                    return ResponseEntity.internalServerError().build();
                }
            }
        }

        // Set IDs from URL
        request.setUserId(userId);
        request.setAccountId(accountId);

        // Perform withdrawal
        Account account = accountService.withdraw(request);

        // Convert returned account to response
        AccountResponse response =
                accountMapper.toResponse(account);

        // Save response for idempotency
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {

            try {
                String responseJson =
                        objectMapper.writeValueAsString(response);

                idempotencyService.save(
                        idempotencyKey,
                        responseJson,
                        200
                );

            } catch (JsonProcessingException e) {
                return ResponseEntity.internalServerError().build();
            }
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<List<AccountResponse>> transfer(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestBody TransferAmount request) {

        request.setUserId(userId);
        request.setAccountId(accountId);

        List<Account> accounts =
                accountService.transfer(request);

        List<AccountResponse> response =
                accounts.stream()
                        .map(accountMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> removeAccount(
            @PathVariable Long userId,
            @PathVariable Long accountId) {

        DeleteAccountRequest request =
                new DeleteAccountRequest();

        request.setUserId(userId);
        request.setAccountId(accountId);

        accountService.removeAccount(request);

        return ResponseEntity.ok(
                "Account removed successfully"
        );
    }
}