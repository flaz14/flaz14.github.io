package com.github.flaz14.grouping;

import org.junit.Test;

public class ConstantsEnhancedInnerClassTest {

    private static class CustomerId {
        static final String VALID = "12345";
        static final String INVALID = "!!!";
        static final String NON_EXISTING = "000";
    }

    @Test
    public void someTest() throws Exception {
        System.out.println(CustomerId.VALID);
        System.out.println(CustomerId.INVALID);
        System.out.println(CustomerId.NON_EXISTING);
    }
}
