package io.github.flaz14.util;


import java.lang.management.ManagementFactory;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jvm {
    /**
     * There is no cross-platform and robust way to find a <strong>Process ID</strong> of current JVM
     * instance (until Java 9). Anyway, this approach is better than platform-specific code. But it's
     * fragile and depends on the implementation of the certain JVM.
     * <p/>
     * You can find more details in the answers for
     * <a href="https://stackoverflow.com/questions/35842/how-can-a-java-program-get-its-own-process-id">
     * How can a Java program get its own process ID?
     * </a>
     */
    public static String pid() {
        return prettyPid(
                vmName()
        );
    }

    static String prettyPid(final String vmName) {
        final Matcher pidMatcher = oracleJvmNameFormat(
                Optional.ofNullable(vmName).
                        orElseThrow(
                                () -> new IllegalArgumentException("vmName should not be null.")
                        )
        );
        if (!pidMatcher.matches()) {
            throw unsupportedVmNameFormat(vmName);
        }
        return pidMatcher.group(1);
    }

    private static String vmName() {
        return ManagementFactory.
                getRuntimeMXBean().
                getName();
    }

    private static Matcher oracleJvmNameFormat(final String rawPid) {
        return Pattern.
                compile("(\\d+)@.*").
                matcher(rawPid);
    }

    private static IllegalArgumentException unsupportedVmNameFormat(final String vmName) {
        String message = String.format("This JVM implementation doesn't follow Oracle's JVM name format. " +
                        "A kind of '11009@localhost' is expected but got [%s]. " +
                        "Please refer to documentation of " +
                        "java.lang.management.RuntimeMXBean.getName " +
                        "method for your virtual machine.",
                vmName);
        return new IllegalArgumentException(message);
    }
}
