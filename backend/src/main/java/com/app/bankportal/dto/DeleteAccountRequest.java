package com.app.bankportal.dto;

public class DeleteAccountRequest {

    private Long userId;
    private Long accountId;

    public Long getUserId() {
        return userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}