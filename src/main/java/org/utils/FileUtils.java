package org.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileUtils {

    public static String readFile(String path) {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(path);

        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(isr)) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
