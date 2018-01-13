package io.github.flaz14;

/**
 *
 */
public class Bootstrapper {
    static {
        System.out.println("Getting path to Java Agent within JAR...");
        final String disabled = System.getProperty("io.github.flaz14.sam-tracer.disabled");
        System.out.println(">>>>>>>>>>> disabled: " + disabled);
//        System.out.printf("Path to Java Agent within JAR [%s]", JavaAgent.pathWithinJar());
    }
}
