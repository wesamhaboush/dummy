package com.codebreeze.rest.server.model;

import java.math.BigDecimal;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class Account {
    public final static Account NULL_ACCOUNT = new Account(null, null, null);
    private final String number;
    private final String name;
    private final BigDecimal balance;

    public Account(final String number, final String name, final BigDecimal balance) {
        this.number = number;
        this.name = name;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
