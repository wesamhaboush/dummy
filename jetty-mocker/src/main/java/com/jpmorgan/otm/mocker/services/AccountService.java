package com.jpmorgan.otm.mocker.services;

import com.google.common.collect.Maps;
import com.jpmorgan.otm.mocker.model.Account;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;


@Service
public class AccountService {
    private final Map<String, Account> accounts = LazyMap.<String, Account>lazyMap(Maps.<String, Account>newHashMap(), new Factory<Account>() {
        @Override
        public Account create() {
            return Account.NULL_ACCOUNT;
        }
    });

    public Account getAccount(final String number) {
        return accounts.get(number);
    }

    public void addAccount(Account account) {
        accounts.put(account.getNumber(), account);
    }
}
