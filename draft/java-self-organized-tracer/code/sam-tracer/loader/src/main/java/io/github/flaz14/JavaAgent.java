package io.github.flaz14;

/**
 *
 */
public class JavaAgent {
    public static String extractedJar() {
//        ClassLoader.
        return null;
    }

    /**
     * Path to the agent's JAR is not hardcoded but will be read from the properties file
     * (the path to alienated JAR is provided by Maven via resource filtering mechanism).
     * Actually the path is hardcoded in the corresponding POM. But at least it's hardcoded
     * one time and in one place.
     */
    private static final String JAVA_AGENT_META_FILE = "java-agent.properties";
}
