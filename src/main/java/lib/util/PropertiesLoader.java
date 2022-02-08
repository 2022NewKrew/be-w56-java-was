package lib.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private static final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    public static void load() throws IOException {
        Properties properties = new Properties();
        try (InputStream is = loader.getResourceAsStream("application.properties")) {
            properties.load(is);
        }
        properties.forEach((k, v) -> System.setProperty(k.toString(), v.toString()));
    }
}
