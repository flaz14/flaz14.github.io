package com.github.flaz14.grouping;

import org.junit.Test;

public class ConstantsEnumTest {

    private enum CustomerId {
        VALID("12345"),
        INVALID("!!!"),
        NON_EXISTING("000");

        public final String id;

        private CustomerId(String id) {
            this.id = id;
        }
    }

    @Test
    public void someTest() throws Exception {
        System.out.println(CustomerId.VALID.id);
        System.out.println(CustomerId.INVALID.id);
        System.out.println(CustomerId.NON_EXISTING.id);
    }
}
