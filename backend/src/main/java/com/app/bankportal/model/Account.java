package com.app.bankportal.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private BigDecimal balance;
    
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getDefault(){
        return isDefault;
    }

    public void setDefault(Boolean isDefault){
        this.isDefault = isDefault;
    }

    public AccountType getAccountType(){
        return accountType;
    }

    public void setAccountType(AccountType accountType){
        this.accountType = accountType;
    }
    public boolean isDefault() {
        return isDefault;
    }

}