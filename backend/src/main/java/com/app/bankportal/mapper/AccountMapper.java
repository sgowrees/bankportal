package com.app.bankportal.mapper;

import com.app.bankportal.dto.AccountResponse;
import com.app.bankportal.model.Account;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;


@Component
public class AccountMapper {
    public AccountResponse toResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getId());
        response.setBalance(account.getBalance());
        response.setDailylimit(account.getDailylimit());
        response.setUserId(account.getUser().getId());
        return response;
    
    }
}