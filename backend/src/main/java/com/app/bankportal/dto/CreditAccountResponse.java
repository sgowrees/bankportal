package com.app.bankportal.dto;

import java.math.BigDecimal;
import com.app.bankportal.model.AccountType;
import java.lang.*;


public class CreditAccountResponse {
    
    private Long accountId;
    private BigDecimal balance;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal creditLimit;
    private BigDecimal interestRate;
    private BigDecimal minPayment;
    private BigDecimal amountPaid; 

    public Long getAccountId() {
        return accountId;
    }  
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public BigDecimal getMinPayment() {
        return minPayment;
    }
    public void setMinPayment(BigDecimal minPayment) {
        this.minPayment = minPayment;
    }
    public BigDecimal getAmountPaid() {
        return amountPaid;

    }
    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
    
}