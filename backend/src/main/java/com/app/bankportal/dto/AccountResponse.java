package com.app.bankportal.dto;

import java.math.BigDecimal;

public class AccountResponse {

    private Long accountId;
    private Long userId;
    private BigDecimal balance;
    private BigDecimal Dailylimit;
    private String accountNumber;
    private com.app.bankportal.model.AccountType accountType;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public BigDecimal getDailylimit() {
        return Dailylimit;
    }

    public void setDailylimit(BigDecimal Dailylimit) {
        this.Dailylimit = Dailylimit;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public com.app.bankportal.model.AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(com.app.bankportal.model.AccountType accountType) {
        this.accountType = accountType;
    }
}