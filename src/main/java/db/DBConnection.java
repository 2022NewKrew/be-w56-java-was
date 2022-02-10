package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER_NAME = "mylo";
    private static final String PASSWORD = "0331";
    private static final String DB_URL = "jdbc:mysql://10.202.179.55:3306/wasdb?useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul";

    private static final DBConnection dbConnection = new DBConnection();

    private DBConnection(){}

    public static DBConnection getInstance(){
        return dbConnection;
    }

    public Connection getConnection(){
        Connection con = null;
        try{
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public void close(AutoCloseable... closeables){
        for(AutoCloseable closeable : closeables){
            if(closeable != null){
                try {
                    closeable.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
