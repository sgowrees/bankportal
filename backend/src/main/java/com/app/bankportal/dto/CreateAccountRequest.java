package com.app.bankportal.dto;

public class CreateAccountRequest {

    private Long userId;
    private String accountType;

    public Long getUserId() {
        return userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}