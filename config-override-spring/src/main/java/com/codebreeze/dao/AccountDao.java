package com.codebreeze.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class AccountDao extends JdbcDaoSupport {

    public Map<String, Object> getAccounts() {
        System.out.println(this.getDataSource());
        return this.getJdbcTemplate().queryForMap("select * from account");
    }

}
