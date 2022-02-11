package application.db;

import java.io.InputStream;
import java.util.Properties;

public class DataSource {

    public final String URL;
    public final String USER;
    public final String PASSWORD;

    public DataSource() {
        Properties properties = loadProperties();
        this.URL = properties.getProperty("url");
        this.USER = properties.getProperty("user");
        this.PASSWORD = properties.getProperty("password");
    }

    // resources 의 DB.properties 파일에서 DB 정보를 읽어옴
    private Properties loadProperties() {
        String resource = "DB.properties";
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource)){
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}
