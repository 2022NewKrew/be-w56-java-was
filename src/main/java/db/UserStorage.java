package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import model.User;

public class UserStorage {

    private static final DBConnection dbConn = new DBConnection();

    public static void addUser(User user) throws SQLException, ClassNotFoundException {
        dbConn.connect();
        String sql = "INSERT INTO user VALUES(" +
                     "'" + user.getUserId() + "'" + "," +
                     "'" + user.getPassword() + "'" + "," +
                     "'" + user.getName() + "'" + "," +
                     "'" + user.getEmail() + "'" + ")";
        Statement statement = dbConn.getConnection().createStatement();
        statement.executeUpdate(sql, Statement.NO_GENERATED_KEYS);
        statement.close();
        dbConn.close();
    }

    public static User findUserById(String userId) throws SQLException, ClassNotFoundException {
        dbConn.connect();
        String sql = "SELECT * FROM user WHERE userId='" + userId + "'";
        Statement statement = dbConn.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        if (!rs.next()) {
            return null;
        }

        String password = rs.getString("password");
        String name = rs.getString("name");
        String email = rs.getString("email");

        statement.close();
        dbConn.close();
        return new User(userId, password, name, email);
    }

    public static Collection<User> findAll() throws SQLException, ClassNotFoundException {
        ArrayList<User> userList = new ArrayList<>();
        dbConn.connect();
        String sql = "SELECT * FROM user";
        Statement statement = dbConn.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            String userId = rs.getString("userId");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");
            userList.add(new User(userId, password, name, email));
        }

        statement.close();
        dbConn.close();
        return userList;
    }
}
