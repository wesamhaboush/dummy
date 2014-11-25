package com.codebreeze.rest.client;

import com.codebreeze.rest.client.config.AppConfig;
import com.codebreeze.rest.client.services.AccountService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

public class Driver {
    public static final void main(final String...args){
        final AccountService accountService = new AnnotationConfigApplicationContext(AppConfig.class).getBean(AccountService.class);
        accountService.addAccount("3", "1", new BigDecimal(1.2));
        System.out.println(accountService.getAccount("1"));

    }
}
