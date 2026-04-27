package com.github.flaz14.junit4.enclosed;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class OneMoreTimeEnclosedTest {

    @RunWith(Enclosed.class)
    public static class FirstEnclosed {

        public static class SecondEnclosed {
            @Test
            public void test() throws Exception {
                System.out.println("I am test from SecondEnclosed!");
            }
        }
    }

    public static class Foo {
        @Test
        public void test() throws Exception {
            System.out.println("I am test from Foo!");
        }
    }

    public static class Bar {
        @Test
        public void test() throws Exception {
            System.out.println("I am test from Bar!");
        }
    }
}
