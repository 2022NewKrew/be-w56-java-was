package infrastructure.config;

public class DatabaseConfig {

    public static String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static String DB_URL = "jdbc:mysql://10.202.144.56:3306/was?useSSL=false&useUnicode=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul";
    public static String USER_NAME = "hello";
    public static String PASSWORD = "world";

    private DatabaseConfig() {}
}
