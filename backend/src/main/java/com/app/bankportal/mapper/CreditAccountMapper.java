
package com.app.bankportal.mapper;

import com.app.bankportal.dto.AccountResponse;
import com.app.bankportal.model.Account;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

import com.app.bankportal.model.CreditAccount;
import com.app.bankportal.dto.CreditAccountResponse; 
import com.app.bankportal.model.AccountType;



@Component
public class CreditAccountMapper {
    public CreditAccountResponse toResponse(CreditAccount account) {
        CreditAccountResponse response = new CreditAccountResponse();
        response.setAccountId(account.getId());
        response.setBalance(account.getBalance());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setCreditLimit(account.getCreditLimit());
        response.setInterestRate(account.getInterestRate());
        response.setMinPayment(account.getMinPayment());
        response.setAmountPaid(account.getAmountPaid());
        return response;
    }
}
