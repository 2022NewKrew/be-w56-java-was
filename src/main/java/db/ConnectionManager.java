package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static webserver.WebServer.DEFAULT_RESOURCES_DIR;

public class ConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    private static final String DB_USERNAME_PROPERTY_KEY = "database.username";
    private static final String DB_PASSWORD_PROPERTY_KEY = "database.password";
    private static final String DB_URL_PROPERTY_KEY = "database.url";
    private static final String DB_PROPERTIES_PATH = DEFAULT_RESOURCES_DIR + "/db.properties";

    private static ConnectionManager instance;
    private final ConnectionPool pool;
    private final String url;
    private final String username;
    private final String password;

    public static synchronized ConnectionManager getInstance() throws SQLException {
        if (instance == null) {
            init();
        }
        return instance;
    }

    private static void init() throws SQLException {
        Properties properties = new Properties();
        try {
            FileInputStream is = new FileInputStream(DB_PROPERTIES_PATH);
            properties.load(is);
        } catch (IOException e) {
            log.error("fail to initialize while load file input stream: {}", e.getMessage());
        }

        String url = properties.getProperty(DB_URL_PROPERTY_KEY);
        String username = properties.getProperty(DB_USERNAME_PROPERTY_KEY);
        String password = properties.getProperty(DB_PASSWORD_PROPERTY_KEY);

        instance = new ConnectionManager(url, username, password);
    }

    private ConnectionManager(String url, String username, String password) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.pool = ConnectionPool.create(url, username, password);
    }

    public void executeSqlScript(String path) {
        File script = new File(path);
        if (!script.isFile()) {
            return;
        }

        Connection connection = null;
        try (BufferedReader br = new BufferedReader(new FileReader(script))){
            connection = getConnection();
            Statement stmt = connection.createStatement();
            executeLines(stmt, br);
            log.info("{} was successfully executed.", script.getName());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            retrieveConnection(connection);
        }
    }

    private void executeLines(Statement stmt, BufferedReader br) throws IOException, SQLException {
        String line;
        while ((line = br.readLine()) != null) {
            stmt.execute(line);
        }
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection(url, username, password);
    }

    public void retrieveConnection(Connection connection) {
        if (connection != null) {
            pool.retrieve(connection);
        }
    }
}
