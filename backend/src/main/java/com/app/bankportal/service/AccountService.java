package com.app.bankportal.service;

import com.app.bankportal.dto.DepositRequest;
import com.app.bankportal.model.Account;
import com.app.bankportal.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.app.bankportal.exception.AccountNotFoundException;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account deposit(DepositRequest request) {
        Optional<Account> result = accountRepository.findById(request.getAccountId());

        if (!result.isPresent()) {
            throw new AccountNotFoundException(request.getAccountId());
        }

        Account account = result.get();
        account.setBalance(account.getBalance().add(request.getAmount()));
        return accountRepository.save(account);
    }
}