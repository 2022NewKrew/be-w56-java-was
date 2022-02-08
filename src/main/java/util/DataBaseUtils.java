package util;

import db.DataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class DataBaseUtils {

    private static final Properties properties = new Properties();
    private static final Logger log = LoggerFactory.getLogger(DataBaseUtils.class);

    public static String setUserTable() {
        StringBuilder users = new StringBuilder();
        AtomicInteger index = new AtomicInteger(1);
        DataBase.findAll().forEach(user -> {
            users.append("<tr>\n");
            users.append(String.format("<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n",
                    index.getAndIncrement(), user.getUserId(), user.getName(), user.getEmail()));
            users.append("</tr>\n");
        });
        return users.toString();
    }

    public static Connection connectDB() {
        try {
            properties.load(new FileInputStream("local.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String url = properties.getProperty("DB_HOST");
            String id = properties.getProperty("DB_NAME");
            String password = properties.getProperty("DB_PASSWORD");
            return DriverManager.getConnection(url, id, password);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
