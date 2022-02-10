package app.repository;

import app.configure.DbConfigure;
import app.core.annotation.Autowired;
import app.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserRepository {
    private final Connection connection;

    @Autowired
    public UserRepository(DbConfigure dbConfigure) {
        this.connection = dbConfigure.getConnection();
    }


    public void addUser(User user) throws SQLException {
        String query = "insert into USERS (user_id, password, name, email) values (?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getUserId());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getName());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.execute();
    }

    public User findUserById(String userId) throws SQLException {
        String query = "select user_id, password, name, email from USERS where user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next())
            throw new SQLException("데이터를 읽어오지 못했습니다!");

        return new User(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4)
        );
    }

    // Todo 너무 많은 레코드는 터져버리고 있다... 해결방법을 찾아야함.
    public Collection<User> findAll() throws SQLException {
        String query = "select user_id, password, name, email from USERS order by created_date desc";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(
                    new User(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }
        return users;
    }

    public void deleteAll() throws SQLException {
        String query = "delete from USERS";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.execute();
    }

}
