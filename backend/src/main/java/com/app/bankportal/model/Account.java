package com.app.bankportal.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ACCOUNT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "ACCOUNT_CATEGORY",
        discriminatorType = DiscriminatorType.STRING
)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private BigDecimal balance;

    private BigDecimal amount;

    private BigDecimal Dailylimit;

    private BigDecimal dailySpent;

    private LocalDate lastTransactionDate;

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


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public BigDecimal getDailylimit() {
        return Dailylimit;
    }

    public void setDailylimit(BigDecimal dailylimit) {
        Dailylimit = dailylimit;
    }


    public BigDecimal getDailySpent() {
        return dailySpent;
    }

    public void setDailySpent(BigDecimal dailySpent) {
        this.dailySpent = dailySpent;
    }


    public LocalDate getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDate lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }


    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }


    public boolean isDefault() {
        return isDefault;
    }

    public boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}