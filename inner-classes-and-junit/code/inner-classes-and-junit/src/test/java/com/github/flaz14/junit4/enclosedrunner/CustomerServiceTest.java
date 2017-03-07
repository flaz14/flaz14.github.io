package com.github.flaz14.junit4.enclosedrunner;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class CustomerServiceTest {

    public static class Create {
        @Test
        public void happyPath() throws Exception {
            System.out.println("create - happyPath");
        }

        @Test
        public void throwsException_whenUnderlyingLayerIsNotAvailable() throws Exception {
            System.out.println("create - throwsException_whenUnderlyingLayerIsNotAvailable");
        }
    }

    public static class Read {
        @Test
        public void happyPath() throws Exception {
            System.out.println("read - happyPath");
        }

        @Test
        public void returnsNull_whenDesiredCustomerDoesNotExist() throws Exception {
            System.out.println("read - throwsException_whenUnderlyingLayerIsNotAvailable");
        }
    }
}
