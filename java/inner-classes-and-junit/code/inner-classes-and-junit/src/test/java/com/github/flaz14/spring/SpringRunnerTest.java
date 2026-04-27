package com.github.flaz14.spring;

import com.github.flaz14.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SampleConfiguration.class})
public class SpringRunnerTest {

    @Autowired
    @Qualifier("customerService")
    private CustomerService customerService;

    @Test
    public void hello() throws Exception {
        assertNotNull(customerService);
    }
}


