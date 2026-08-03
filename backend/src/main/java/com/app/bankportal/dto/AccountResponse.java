package com.app.bankportal.dto;

import java.math.BigDecimal;

public class AccountResponse {

    private Long accountId;
    private Long userId;
    private BigDecimal balance;
    private BigDecimal Dailylimit;

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
}