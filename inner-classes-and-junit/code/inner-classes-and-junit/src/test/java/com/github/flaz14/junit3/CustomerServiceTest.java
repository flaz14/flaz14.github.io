package com.github.flaz14.junit3;

import junit.framework.TestCase;

public class CustomerServiceTest {

    public static class Create extends TestCase {
        public void test_happyPath() throws Exception {
            System.out.println("create - happyPath");
        }

        public void test_throwsException_whenUnderlyingLayerIsNotAvailable() throws Exception {
            System.out.println("create - throwsException_whenUnderlyingLayerIsNotAvailable");
        }
    }

    public static class Read extends TestCase {
        public void test_happyPath() throws Exception {
            System.out.println("read - happyPath");
        }

        public void test_returnsNull_whenDesiredCustomerDoesNotExist() throws Exception {
            System.out.println("read - throwsException_whenUnderlyingLayerIsNotAvailable");
        }
    }
}
