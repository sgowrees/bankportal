package com.app.bankportal.service;

import com.app.bankportal.dto.*;
import com.app.bankportal.exception.*;
import com.app.bankportal.model.Account;
import com.app.bankportal.model.AccountType;
import com.app.bankportal.model.User;
import com.app.bankportal.repository.AccountRepository;
import com.app.bankportal.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository) {

        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Account deposit(DepositRequest request) {

        Optional<Account> result = accountRepository.findById(request.getAccountId());

        if (result.isEmpty()) {
            throw new AccountNotFoundException(request.getAccountId());
        }

        Account account = result.get();

        if (!account.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("Account does not belong to user");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeAmountException();
        }

        account.setBalance(account.getBalance().add(request.getAmount()));

        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(WithdrawalRequest request) {

        Optional<Account> result = accountRepository.findById(request.getAccountId());

        if (result.isEmpty()) {
            throw new AccountNotFoundException(request.getAccountId());
        }

        Account account = result.get();

        if (!account.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("Account does not belong to user");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeAmountException();
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        LocalDate today = LocalDate.now();

        if (account.getLastTransactionDate() == null ||
                !account.getLastTransactionDate().isEqual(today)) {

            account.setDailySpent(BigDecimal.ZERO);
            account.setLastTransactionDate(today);
        }

        if (account.getDailySpent()
                .add(request.getAmount())
                .compareTo(account.getDailylimit()) > 0) {

            throw new RuntimeException("Amount exceeding daily limit");
        }

        account.setDailySpent(
                account.getDailySpent().add(request.getAmount())
        );

        account.setBalance(
                account.getBalance().subtract(request.getAmount())
        );

        return accountRepository.save(account);
    }

    public void removeAccount(DeleteAccountRequest request) {

        Optional<Account> account = accountRepository.findById(request.getAccountId());

        if (account.isEmpty()) {
            throw new AccountNotFoundException(request.getAccountId());
        }

        Account accountFound = account.get();

        if (!accountFound.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("Account does not belong to user");
        }

        if (accountFound.isDefault()) {
            throw new DefaultAccountException();
        }

        accountRepository.delete(accountFound);
    }

    @Transactional
    public ArrayList<Account> transfer(TransferAmount request) {

        Optional<Account> res1 = accountRepository.findById(request.getAccountId());
        Optional<Account> res2 = accountRepository.findById(request.getToAccountId());

        if (res1.isEmpty()) {
            throw new AccountNotFoundException(request.getAccountId());
        }

        if (res2.isEmpty()) {
            throw new AccountNotFoundException(request.getToAccountId());
        }

        Account account1 = res1.get();
        Account account2 = res2.get();

        if (!account1.getUser().getId().equals(request.getUserId())
                || !account2.getUser().getId().equals(request.getUserId())) {

            throw new RuntimeException("Both accounts must belong to user");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeAmountException();
        }

        if (account1.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account1.setBalance(
                account1.getBalance().subtract(request.getAmount())
        );

        account2.setBalance(
                account2.getBalance().add(request.getAmount())
        );

        accountRepository.save(account1);
        accountRepository.save(account2);

        ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(account1);
        accounts.add(account2);

        return accounts;
    }

    public List<Account> getAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account createAccount(CreateAccountRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();

        account.setBalance(BigDecimal.ZERO);
        account.setDailylimit(BigDecimal.valueOf(1000));
        account.setUser(user);

        account.setAccountType(
                AccountType.valueOf(request.getAccountType())
        );

        accountRepository.save(account);

        account.setAccountNumber("ACC-" + account.getId());

        return accountRepository.save(account);
    }
}