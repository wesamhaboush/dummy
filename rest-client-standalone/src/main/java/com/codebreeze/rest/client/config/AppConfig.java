package com.codebreeze.rest.client.config;

import com.codebreeze.rest.client.services.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;


@Configuration
public class AppConfig {

    @Bean
    public Jaxp13XPathTemplate xpathTemplate() {
        Jaxp13XPathTemplate xpathTemplate = new Jaxp13XPathTemplate();
        return xpathTemplate;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public AccountService accountService(){
        return new AccountService();
    }

}
