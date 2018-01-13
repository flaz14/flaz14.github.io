package io.github.flaz14.test.unit;

import io.github.flaz14.Jvm;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class JvmTest {
    public static class Pid {
        @Test
        public void oracleJvm() {
            assertThat(Jvm.pid(), is(not(0)));
        }
    }
}

