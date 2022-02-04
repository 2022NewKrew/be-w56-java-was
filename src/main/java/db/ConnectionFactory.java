package db;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static webserver.WebServer.DEFAULT_RESOURCES_DIR;

public class ConnectionFactory {

    private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);

    private static final String DB_USERNAME_PROPERTY_KEY = "database.username";
    private static final String DB_PASSWORD_PROPERTY_KEY = "database.password";
    private static final String DB_URL_PROPERTY_KEY = "database.url";
    private static final String DB_PROPERTIES_PATH = DEFAULT_RESOURCES_DIR + "/db.properties";
    private static final String DB_SCHEMA_PATH = DEFAULT_RESOURCES_DIR + "/schema.sql";
    private static final String DB_DATA_PATH = DEFAULT_RESOURCES_DIR + "/data.sql";

    private static final MysqlDataSource DATA_SOURCE;

    static {
        Properties properties = new Properties();
        try {
            FileInputStream is = new FileInputStream(DB_PROPERTIES_PATH);
            properties.load(is);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String username = properties.getProperty(DB_USERNAME_PROPERTY_KEY);
        String password = properties.getProperty(DB_PASSWORD_PROPERTY_KEY);
        String url = properties.getProperty(DB_URL_PROPERTY_KEY);
        DATA_SOURCE = new MysqlDataSource();
        DATA_SOURCE.setUser(username);
        DATA_SOURCE.setPassword(password);
        DATA_SOURCE.setUrl(url);

        executeSqlScript(DB_SCHEMA_PATH);
        executeSqlScript(DB_DATA_PATH);
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    private static void executeSqlScript(String path) {
        if (Files.notExists(Path.of(path))) {
            return;
        }
        File script = new File(path);

        try (Connection connection = getConnection(); BufferedReader br = new BufferedReader(new FileReader(script))) {
            Statement stmt = connection.createStatement();
            String line;
            while ((line = br.readLine()) != null) {
                stmt.execute(line);
            }
            log.info("{} was successfully executed.", script.getName());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private ConnectionFactory() {
        throw new UnsupportedOperationException();
    }
}
