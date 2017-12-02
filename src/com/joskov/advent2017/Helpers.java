package com.joskov.advent2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by vasiljoskov on 12/2/17.
 */
public class Helpers {
    public static String read(String name) throws IOException {
        return new String(Files.readAllBytes(Paths.get(name)));
    }
}
