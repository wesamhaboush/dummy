package com.codebreeze.config;

import com.codebreeze.dao.AccountDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;


@Configuration
@ComponentScan(basePackages = {"com.codebreeze.dao"})
public class AppConfig {

    @Bean
    public AccountDao accountDao() {
        AccountDao accountDao = new AccountDao();
        accountDao.setJdbcTemplate(jdbcTemplate());
        return accountDao;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(H2)
//                .addScript("my-schema.sql")
//                .addScript("my-test-data.sql")
                .build();
    }

}
