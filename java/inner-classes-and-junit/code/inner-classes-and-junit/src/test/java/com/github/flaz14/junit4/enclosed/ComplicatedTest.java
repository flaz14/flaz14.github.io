package com.github.flaz14.junit4.enclosed;

import com.github.flaz14.CustomerService;
import com.github.flaz14.spring.SampleConfiguration;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Enclosed.class)
public class ComplicatedTest {

    @RunWith(Enclosed.class)
    public static class EnclosedSpringRunner {

        @RunWith(SpringJUnit4ClassRunner.class)
        @ContextConfiguration(classes = {SampleConfiguration.class})
        public static class Hello {
            @Autowired
            @Qualifier("customerService")
            private CustomerService service;

            @Test
            public void happyPath() throws Exception {
                final String actualMessage = service.hello();
                assertThat(actualMessage, equalTo("Hello! I am stupid CustomerService."));
            }

            @Test
            public void unhappyPath() throws Exception {
                System.out.println("I am unhappyPath() test for hello() method.");
            }
        }
    }

    @RunWith(Parameterized.class)
    public static class Goodbye {
        @Parameters
        public static Collection<Object[]> data() {
            return unmodifiableCollection(asList(new Object[][]{
                    {1, "1"},
                    {2, "2"}
                    // ...
            }));
        }

        @Parameter(0)
        public int input;

        @Parameter(1)
        public String expected;

        @Test
        public void happyPath() throws Exception {
            final String output = format("%d", input);
            assertThat(output, equalTo(expected));
        }

        @Test
        public void unhappyPath() throws Exception {
            final String message = format(
                    "I am parameterized test. And the parameters are: input=[%s], expected=[%s]",
                    input, expected);
            System.out.println(message);
        }
    }
}
