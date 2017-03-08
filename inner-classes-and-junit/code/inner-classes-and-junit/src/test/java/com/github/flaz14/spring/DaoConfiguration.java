package com.github.flaz14.spring;

import com.github.flaz14.CustomerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DaoConfiguration {
    @Bean
    @Qualifier("customerService")
    public CustomerService customerService() {
        return new CustomerService();
    }
}