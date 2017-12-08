package io;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class ReadFileLineByLineAndPrintTest {

    private static final String FILE = "io/cp1251.txt";
    private static final Charset ENCODING = Charset.forName("cp1251");

    @Test
    public void java8Stream() throws URISyntaxException {
        final Path inputFile = Paths.get(ClassLoader.getSystemResource(FILE).toURI());
        try (final Stream<String> lines = Files.lines(inputFile, ENCODING)) {
            lines.forEach(line -> System.out.println("[" + line + "]"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void java8StreamWithoutEncodingSpecified() throws URISyntaxException {
        final Path inputFile = Paths.get(ClassLoader.getSystemResource(FILE).toURI());
        try (final Stream<String> lines = Files.lines(inputFile)) {
            lines.forEach(line -> System.out.println("[" + line + "]"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void java7Scanner() throws URISyntaxException {
        final File inputFile = Paths.get(ClassLoader.getSystemResource(FILE).toURI()).toFile();
        try (final Scanner scanner = new Scanner(inputFile, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                System.out.println("[" + line + "]");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void java7ScannerWithoutEncodingSpecified() throws URISyntaxException {
        final File inputFile = Paths.get(ClassLoader.getSystemResource(FILE).toURI()).toFile();
        try (final Scanner scanner = new Scanner(inputFile)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                System.out.println("[" + line + "]");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDefaultCharset() {
        System.out.println(Charset.defaultCharset());
    }
}
