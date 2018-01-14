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

import static java.lang.ClassLoader.getSystemResourceAsStream;

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
            throw OutwardException.cannotAttachJavaAgent(pathToJavaAgent, pid, e);
        }
    }

    private static String extractedJar() {
        try (final InputStream sourceJar = getSystemResourceAsStream(
                pathWithinJar()
        )) {
            final Path targetJar = Files.createTempFile("sam-tracer-", ".jar");
            Files.copy(sourceJar, targetJar, StandardCopyOption.REPLACE_EXISTING);
            final String pathToExtractedJar = targetJar.toString();
            System.out.printf("Path to extracted Java Agent JAR [%s]%n", pathToExtractedJar);
            return pathToExtractedJar;
        } catch (IOException e) {
            throw OutwardException.cannotExtractJavaAgentJar(e);
        }
    }

    private static String pathWithinJar() {
        try (final InputStream propertiesFile = getSystemResourceAsStream(PROPERTIES_FILE)) {
            return jarProperty(propertiesFile);
        } catch (IOException e) {
            throw OutwardException.cannotReadPropertiesFile(e);
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
     * only once and in single place.
     */
    private static final String PROPERTIES_FILE = "java-agent.properties";

    private static class OutwardException {
        static IllegalStateException cannotReadPropertiesFile(final IOException cause) {
            String message = String.format("Cannot read Java Agent properties file [%s] " +
                            "from within JAR.",
                    PROPERTIES_FILE);
            return new IllegalStateException(message, cause);
        }

        static IllegalStateException cannotExtractJavaAgentJar(final IOException cause) {
            return new IllegalStateException("Cannot extract Java Agent JAR from this JAR", cause);
        }

        static IllegalStateException cannotAttachJavaAgent(final String pathToJavaAgent,
                                                           final String pid,
                                                           final Exception cause) {
            String message = String.format(
                    "Cannot attach Java Agent [%s] to JVM instance with PID [%s]",
                    pathToJavaAgent, pid);
            throw new IllegalStateException(message, cause);
        }
    }
}
