package io.github.flaz14.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;

@RunWith(Enclosed.class)
public class JvmTest {

    public static class PrettyPid {
        @Rule
        public final ExpectedException expectedException = none();

        @Test
        public void happyPath() {
            assertThat(Jvm.prettyPid("12345@localhost"), equalTo("12345"));
        }

        @Test
        public void throwsException_whenPidIsNotPresented() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("This JVM implementation doesn't follow " +
                    "Oracle's JVM name format. A kind of '11009@localhost' is expected " +
                    "but got [@localhost]. Please refer to documentation of " +
                    "java.lang.management.RuntimeMXBean.getName method for your virtual machine.");
            Jvm.prettyPid("@localhost");
        }

        @Test
        public void throwsException_whenTheWholeNameIsEmpty() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("This JVM implementation doesn't follow " +
                    "Oracle's JVM name format. A kind of '11009@localhost' is expected " +
                    "but got []. Please refer to documentation of " +
                    "java.lang.management.RuntimeMXBean.getName method for your virtual machine.");
            Jvm.prettyPid("");
        }

        @Test
        public void throwsException_whenNameIsNull() {
            expectedException.expect(IllegalArgumentException.class);
            expectedException.expectMessage("vmName should not be null.");
            Jvm.prettyPid(null);
        }
    }
}

