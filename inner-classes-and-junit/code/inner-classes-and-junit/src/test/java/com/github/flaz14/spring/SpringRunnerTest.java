package com.github.flaz14.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoConfiguration.class})
public class SpringRunnerTest {

    @Autowired
    @Qualifier("dao")
    private Dao dao;

    @Test
    public void hello() throws Exception {
        String actualGreeting = dao.hello();
        assertThat(actualGreeting, equalTo("Hello! I am DAO."));
    }
}

@Configuration
class DaoConfiguration {
    @Bean
    @Qualifier("dao")
    public Dao dao() {
        return new Dao();
    }
}

class Dao {
    public String hello() {
        return "Hello! I am DAO.";
    }
}