package io.github.flaz14;

/**
 *
 */
public class Loader {
    static {
        if (isDisabled()) {
            System.out.println("WARNING: sam-tracer is disabled.");
        } else {
            System.out.println("Extracting Java Agent JAR...");
            JavaAgent.attachToJvm(Jvm.pid());
        }
    }

    private static boolean isDisabled() {
        final String rawValue = System.getProperty(IS_DISABLED_PROPERTY);
        System.out.printf("[%s]=[%s]%n", IS_DISABLED_PROPERTY, rawValue);
        return Boolean.parseBoolean(rawValue);

    }

    private static final String IS_DISABLED_PROPERTY = "io.github.flaz14.sam-tracer.disabled";
}
