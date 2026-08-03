package com.app.bankportal.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import com.app.bankportal.model.AccountType;
import com.app.bankportal.model.User;
import com.app.bankportal.model.CreditAccount;




@Inheritance(strategy = InheritanceType.JOINED)
@Entity
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
    public BigDecimal getDailylimit(){
        return Dailylimit;
    }
    public void setDailylimit(BigDecimal Dailylimit){
        this.Dailylimit = Dailylimit;
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

        public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}