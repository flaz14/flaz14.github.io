package com.github.flaz14.factory;

import org.junit.Test;

public class DtoFactoryTest {

    private static class Customer {
        static CustomerDto withMandatoryFields() {
            return new CustomerDto();
        }

        static class WithBankDetails {
            static CustomerDto apparent() {
                CustomerDto dto = new CustomerDto();
                // set up some credit card number fully
                // ...
                return dto;
            }

            static CustomerDto obscured() {
                CustomerDto dto = new CustomerDto();
                // set up credit card number in the form of XXXX XXXX XXXX 1234
                // ...
                return dto;
            }
        }
    }

    @Test
    public void test() throws Exception {
        CustomerDto expected = Customer.WithBankDetails.obscured();
        // ...
    }
}

class CustomerDto {
    // ...
}
