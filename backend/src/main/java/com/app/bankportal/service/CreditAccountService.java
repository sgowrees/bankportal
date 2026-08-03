package com.app.bankportal.service;

import com.app.bankportal.dto.*;
import com.app.bankportal.exception.AccountNotFoundException;
import com.app.bankportal.model.*;
import com.app.bankportal.repository.CreditAccountRepository;
import com.app.bankportal.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class CreditAccountService {

    private final CreditAccountRepository creditAccountRepository;
    private final UserRepository userRepository;


    public CreditAccountService(
            CreditAccountRepository creditAccountRepository,
            UserRepository userRepository) {

        this.creditAccountRepository = creditAccountRepository;
        this.userRepository = userRepository;
    }


    public List<CreditAccount> getCreditAccounts(
            GetCreditAccountsRequest request) {

        return creditAccountRepository.findByUserId(request.getUserId());
    }


    public CreditAccount createCreditCard(
            CreateCreditCardRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User does not exist"));


        CreditAccount account = new CreditAccount();

        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        account.setAmountPaid(BigDecimal.ZERO);
        account.setAccountType(AccountType.valueOf(request.getAccountType()));
        account.setDefault(false);
        account.setInterestRate(request.getInterestRate());
        account.setMinPayment(request.getMinPayment());
        account.setCreditLimit(request.getCreditLimit());
        account.setDailylimit(request.getDailyLimit());


        creditAccountRepository.save(account);

        account.setAccountNumber("ACC-" + account.getId());

        return creditAccountRepository.save(account);
    }


    @Transactional
    public CreditAccount updateInterestRate(
            UpdateInterestRateRequest request) {

        CreditAccount account =
                creditAccountRepository.findById(request.getAccountId())
                        .orElseThrow(() -> new RuntimeException("Account does not exist"));


        if (!account.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("Account does not belong to user");
        }


        account.setInterestRate(request.getInterestRate());

        return creditAccountRepository.save(account);
    }


    @Transactional
    public CreditAccount charge(
            ChargeRequest request) {

        CreditAccount account =
                creditAccountRepository.findById(request.getAccountId())
                        .orElseThrow(() -> new RuntimeException("Account does not exist"));


        if (!account.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("Account does not belong to user");
        }


        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }


        if (account.getBalance()
                .add(request.getAmount())
                .compareTo(account.getCreditLimit()) > 0) {

            throw new RuntimeException("Credit limit exceeded");
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

            throw new RuntimeException("Daily limit exceeded");
        }


        account.setDailySpent(
                account.getDailySpent().add(request.getAmount())
        );

        account.setBalance(
                account.getBalance().add(request.getAmount())
        );


        return creditAccountRepository.save(account);
    }


    @Transactional
    public CreditAccount makePayment(
            PayCreditCardRequest request) {

        CreditAccount account =
                creditAccountRepository.findById(request.getAccountId())
                        .orElseThrow(() -> new RuntimeException("Account does not exist"));


        if (!account.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("Account does not belong to user");
        }


        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }


        account.setBalance(
                account.getBalance().subtract(request.getAmount())
        );


        account.setAmountPaid(
                account.getAmountPaid().add(request.getAmount())
        );


        return creditAccountRepository.save(account);
    }


    @Transactional
    public CreditAccount checkMinPayment(
            MinPaymentCheckRequest request) {

        CreditAccount account =
                creditAccountRepository.findById(request.getAccountId())
                        .orElseThrow(() -> new AccountNotFoundException(request.getAccountId()));


        if (!account.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("Account does not belong to user");
        }


        LocalDate today = LocalDate.now();

        LocalDate endOfMonth =
                today.with(TemporalAdjusters.lastDayOfMonth());


        if (today.isEqual(endOfMonth)
                && account.getAmountPaid()
                .compareTo(account.getMinPayment()) < 0) {


            account.setBalance(
                    account.getBalance()
                            .add(account.getBalance()
                            .multiply(account.getInterestRate()))
            );


            account.setInterestRate(
                    account.getInterestRate()
                            .add(BigDecimal.valueOf(0.01))
            );
        }


        account.setAmountPaid(BigDecimal.ZERO);

        return creditAccountRepository.save(account);
    }
}