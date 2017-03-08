package com.github.flaz14.junit4.enclosedrunner;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class TrivialTest {

    public static class Hello {
        @Test
        public void happyPath() throws Exception {
            // ...
        }

        @Test
        public void unhappyPath() throws Exception {
            // ...
        }
    }

    public static class Goodbye {
        @Test
        public void happyPath() throws Exception {
            // ...
        }

        @Test
        public void unhappyPath() throws Exception {
            // ...
        }
    }
}