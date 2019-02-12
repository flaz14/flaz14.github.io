package com.github.flaz14.grouping;

import org.junit.Test;

public class ConstantsPlainOldApproachTest {

    private static final String CUSTOMER_ID = "12345";
    private static final String INVALID_CUSTOMER_ID = "!!!";
    private static final String NON_EXISTING_CUSTOMER_ID = "000";

    @Test
    public void someTest() throws Exception {
        System.out.println(CUSTOMER_ID);
        System.out.println(INVALID_CUSTOMER_ID);
        System.out.println(NON_EXISTING_CUSTOMER_ID);
    }
}