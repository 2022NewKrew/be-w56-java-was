package util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    public static Properties readDBProperties() {
        Properties prop = new Properties();
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("db.properties")) {
            prop.load(inputStream);
            return prop;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
