package com.app.bankportal.service;

import com.app.bankportal.dto.DepositRequest;
import com.app.bankportal.model.Account;
import com.app.bankportal.model.User;
import com.app.bankportal.repository.AccountRepository;
import com.app.bankportal.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;


    @Test
    void deposit_ShouldIncreaseBalance() {

        User user = new User();
        user.setId(1L);


        Account account = new Account();

        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100));
        account.setUser(user);



        DepositRequest request = new DepositRequest();

        request.setUserId(1L);
        request.setAccountId(1L);
        request.setAmount(BigDecimal.valueOf(50));



        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));


        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);



        Account result =
                accountService.deposit(request);



        assertEquals(
                BigDecimal.valueOf(150),
                result.getBalance()
        );


        verify(accountRepository)
                .save(account);
    }
}