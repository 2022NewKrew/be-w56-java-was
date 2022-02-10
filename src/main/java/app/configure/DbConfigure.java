package app.configure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfigure {

    //Todo 필수 인자들을 생성 시 환경변수를 사용해서 외부에서 주입받도록 구현
    private static final String URL = "jdbc:mysql://database:3306/JAVA_WAS";
    private static final String USER = "root";
    private static final String PASSWORD = "test";

    private final Connection connection;

    public DbConfigure(String host) throws SQLException {
        String curUrl = String.format("jdbc:mysql://%s:3306/JAVA_WAS", host);
        this.connection = DriverManager.getConnection(curUrl, USER, PASSWORD);
    }

    public DbConfigure() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }
}
