package io.github.flaz14.util;


import java.lang.management.ManagementFactory;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://stackoverflow.com/questions/35842/how-can-a-java-program-get-its-own-process-id
 * <p/>
 * https://stackoverflow.com/questions/38482839/maven-assembly-plugin-how-to-use-appendassemblyid/38484021
 */
public class Jvm {

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
