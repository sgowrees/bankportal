package com.app.bankportal.model;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@DiscriminatorValue("CREDIT_ACCOUNT")
public class CreditAccount extends Account {


    private BigDecimal interestRate;

    private BigDecimal creditLimit;

    private BigDecimal minPayment;

    private BigDecimal amountPaid;


    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }


    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
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