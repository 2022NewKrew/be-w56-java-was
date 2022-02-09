package webapplication.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertiesReader {

    private static final String PROPERTIES_PATH = "application.properties";

    private static Properties properties = readProperties(PROPERTIES_PATH);

    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);

    private static Properties readProperties(String propertiesPath) {
        Properties properties = new Properties();
        try {
            URL resource = PropertiesReader.class.getClassLoader().getResource(propertiesPath);
            InputStream reader = new FileInputStream(resource.getPath());
            properties.load(reader);
        } catch (IOException e) {
            log.info("{} : {}", PropertiesReader.class, e.getMessage());
        }
        return properties;
    }

    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
