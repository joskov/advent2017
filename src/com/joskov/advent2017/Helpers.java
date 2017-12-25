package com.joskov.advent2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vasiljoskov on 12/2/17.
 */
public class Helpers {
    public static String read(String name) throws IOException {
        return new String(Files.readAllBytes(Paths.get(name)));
    }

    public static List<String> match(Pattern pattern, String string) {
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < matcher.groupCount(); i++) {
            result.add(matcher.group(i + 1));
        }
        return result;
    }
}
