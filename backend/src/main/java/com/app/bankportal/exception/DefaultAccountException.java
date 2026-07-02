package com.app.bankportal.exception;

public class DefaultAccountException extends RuntimeException {
    public DefaultAccountException() {
        super("Cannot delete default account");
    }
}