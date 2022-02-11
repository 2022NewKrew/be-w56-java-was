package db;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;
import webserver.DuplicateUserException;

public class DataBase {
    private static DataBase instance = new DataBase();
    private Connection con;
    private static final String server = "ed-ever-ubuntu.ay1.krane.9rum.cc";
    private static final String database = "eddb";
    private static final String userName = "ed";
    private static final String password = "root";

    private static final String SELECT_BY_ID = "SELECT * FROM USERS WHERE id = ?";
    private static final String INSERT = "INSERT INTO USERS VALUES(?, ?, ?, ?)";

    private DataBase() {}

    public static DataBase getInstance() {
        return instance;
    }

    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + "?allowPublicKeyRetrieval=true", userName, password);
            con.createStatement().execute("CREATE TABLE IF NOT EXISTS USERS(" +
                    "id VARCHAR(20) NOT NULL," +
                    "password VARCHAR(30) NOT NULL," +
                    "name VARCHAR(50) NOT NULL," +
                    "email VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY(id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, User> users = Maps.newHashMap();

    public void addUser(User user) throws DuplicateUserException {
        try {
            PreparedStatement ps = con.prepareStatement(INSERT);
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getPassword());
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateUserException("유저 ID 중복");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findUserById(String userId) throws IOException {
        try {
            PreparedStatement ps = con.prepareStatement(SELECT_BY_ID);
            ps.setString(1, userId);

            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                return null;
            }
            String id = rs.getString("id");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");
            return new User(id, password, name, email);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("DB 에러");
        }
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
