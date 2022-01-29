package util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MIME {

    private static Map<String, String> mimeMappings = new HashMap<>() {{
        put("html", "text/html");
        put("css", "text/css");
        put("woff2", "application/font-woff2");
        put("js", "application/javascript");
        put("ico", "image/x-icon");

    }};

    public static String findMimeMapping(String extension) {
        return mimeMappings.get(extension.toLowerCase(Locale.ENGLISH));
    }

    public static boolean isSupportExtension(String path) {
        return mimeMappings.keySet().stream()
                .anyMatch(key -> path.endsWith(key));
    }

    public static String getMediaType(String file) {
        if (file == null) {
            return null;
        }
        int period = file.lastIndexOf('.');
        if (period < 0) {
            return null;
        }
        String extension = file.substring(period + 1);
        if (extension.length() < 1) {
            return null;
        }
        return findMimeMapping(extension);
    }
}
