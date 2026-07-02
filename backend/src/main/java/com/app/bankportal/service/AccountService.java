package com.app.bankportal.service;

import com.app.bankportal.dto.DepositRequest;
import com.app.bankportal.model.Account;
import com.app.bankportal.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.app.bankportal.exception.AccountNotFoundException;
import com.app.bankportal.model.AccountType;
import com.app.bankportal.model.User;
import com.app.bankportal.repository.UserRepository;
import java.util.List;
import java.math.BigDecimal;
import com.app.bankportal.dto.*;
import com.app.bankportal.exception.*;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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
    public Account createAccount(CreateAccountRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(!user.isPresent()){
            throw new RuntimeException("User Does not exist");
        }
        User userFound = user.get();
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setUser(userFound);
        account.setAccountType(AccountType.valueOf(request.getAccountType()));
        account.setDefault(false);
        accountRepository.save(account); 
        account.setAccountNumber("ACC-" + account.getId());
        accountRepository.save(account);  

        return account;
    }

    public void removeAccount(Long accountId) {
        Optional <Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()){
            throw new RuntimeException("account Does not exist");
        }
        Account accountFound = account.get();
        if (accountFound.isDefault()){
            throw new DefaultAccountException();
        }
        accountRepository.delete(accountFound);
    }
}