package io.github.flaz14;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class JavaAgent {
    public static String pathWithinJar() {
        try (final InputStream propertiesFile = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE)) {
            return pathToJar(propertiesFile);
        } catch (IOException onLoadingProperties) {
            throw new IllegalStateException(errorMessage(), onLoadingProperties);
        }
    }

    private static String pathToJar(final InputStream propertiesFile) throws IOException {
        final Properties properties = new Properties();
        properties.load(propertiesFile);
        return properties.getProperty("jar");
    }

    private static String errorMessage() {
        return String.format("Cannot read Java Agent properties file [%s] from within JAR.", PROPERTIES_FILE);
    }

    /**
     * Path to the agent's JAR is not hardcoded but will be read from the properties file
     * (the path to alienated JAR is provided by Maven via resource filtering mechanism).
     * Actually the path is hardcoded in the corresponding POM. But at least it's hardcoded
     * one time and in one place.
     */
    private static final String PROPERTIES_FILE = "java-agent.properties";
}
