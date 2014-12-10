package com.codebreeze.dao;

import com.codebreeze.config.AppConfig;
import com.codebreeze.config.TestAppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
public class AccountDaoTest {

    @Autowired
    private AccountDao accountDao;

    @Test
    public void testDatasourceConfigOverrideWorks(){
        accountDao.getAccounts();
    }

}