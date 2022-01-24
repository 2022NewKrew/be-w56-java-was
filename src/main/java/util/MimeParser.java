package util;

import org.apache.tika.Tika;

public class MimeParser {
    private static final Tika tika = new Tika();

    public static String parseMimeType(String path) {
        return tika.detect(path);
    }
}
