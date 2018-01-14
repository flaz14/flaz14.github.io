package io.github.flaz14.util;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/**
 *
 */
public class JavaAgent {

    public static void attachToJvm(final String pid) {
        final String pathToJavaAgent = extractedJar();
        try {
            final VirtualMachine vm = VirtualMachine.attach(pid);
            vm.loadAgent(pathToJavaAgent);
            vm.detach();
        } catch (IOException |
                AttachNotSupportedException |
                AgentLoadException |
                AgentInitializationException e) {
            final String errorMessage = String.format(
                    "Cannot attach Java Agent [%s] to JVM instance with PID [%s]",
                    pathToJavaAgent, pid);
            throw new IllegalStateException(errorMessage, e);
        }
    }

    private static String extractedJar() {
        try (final InputStream sourceJar = ClassLoader.getSystemResourceAsStream(
                pathWithinJar())) {
            final Path targetJar = Files.createTempFile("sam-tracer-", ".jar");
            Files.copy(sourceJar, targetJar, StandardCopyOption.REPLACE_EXISTING);
            final String pathToExtractedJar = targetJar.toString();
            System.out.printf("Path to extracted Java Agent JAR [%s]%n", pathToExtractedJar);
            return pathToExtractedJar;
        } catch (IOException e) {
            throw Exceptions.cannotExtractJavaAgentJar(e);
        }
    }

    private static String pathWithinJar() {
        try (final InputStream propertiesFile = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE)) {
            return jarProperty(propertiesFile);
        } catch (IOException e) {
            throw Exceptions.cannotReadPropertiesFile(e);
        }
    }

    private static String jarProperty(final InputStream propertiesFile) throws IOException {
        final Properties properties = new Properties();
        properties.load(propertiesFile);
        final String path = properties.getProperty("jar");
        System.out.printf("Path to Java Agent within JAR [%s]%n", path);
        return path;
    }

    /**
     * Path to the agent's JAR is not hardcoded but will be read from the properties file
     * (the path to alienated JAR is provided by Maven via resource filtering mechanism).
     * Actually the path is hardcoded in the corresponding POM. But at least it's hardcoded
     * one time and in one place.
     */
    private static final String PROPERTIES_FILE = "java-agent.properties";

    private static class Exceptions {
        static IllegalStateException cannotReadPropertiesFile(final IOException cause) {
            final String errorMessage = String.format("Cannot read Java Agent properties file [%s] " +
                    "from within JAR.", PROPERTIES_FILE);
            return new IllegalStateException(errorMessage, cause);

        }

        static IllegalStateException cannotExtractJavaAgentJar(final IOException cause) {
            return new IllegalStateException("Cannot extract Java Agent JAR from this JAR", cause);
        }
    }
}
