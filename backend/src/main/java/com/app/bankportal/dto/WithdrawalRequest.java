package com.app.bankportal.dto;

import java.math.BigDecimal;

public class WithdrawalRequest {

    private Long userId;
    private Long accountId;
    private BigDecimal amount;

    public Long getUserId() {
        return userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}