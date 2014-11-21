package com.jpmorgan.otm.mocker.config;

import com.jpmorgan.otm.mocker.rs.AccountRestService;
import com.jpmorgan.otm.mocker.rs.JaxRsApiApplication;
import com.jpmorgan.otm.mocker.rs.ToStringProvider;
import com.jpmorgan.otm.mocker.services.AccountService;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ext.RuntimeDelegate;
import java.util.Arrays;

import static java.util.Arrays.asList;


@Configuration
public class AppConfig {
    @Bean( destroyMethod = "shutdown" )
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    public Server jaxRsServer() {
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint( jaxRsApiApplication(), JAXRSServerFactoryBean.class );
        factory.setServiceBeans(Arrays.<Object>asList(peopleRestService()));
        factory.setAddress(factory.getAddress());
        factory.setProviders(asList(new ToStringProvider()));
        return factory.create();
    }

    @Bean
    public JaxRsApiApplication jaxRsApiApplication() {
        return new JaxRsApiApplication();
    }

    @Bean
    public AccountRestService peopleRestService() {
        return new AccountRestService();
    }

    @Bean
    public AccountService accountService() {
        return new AccountService();
    }
}
