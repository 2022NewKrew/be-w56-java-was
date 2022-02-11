package service;

import dao.UserDAO;
import dto.UserCreateCommand;
import dto.UserLoginCommand;
import model.User;
import webserver.http.HttpRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public void store(UserCreateCommand ucc) throws SQLException {
        userDAO.storeUser(ucc);
    }

    public boolean canLogIn(UserLoginCommand ulc) {
        try {
            String userId = ulc.getUserId();
            String password = ulc.getPassword();
            User user = userDAO.findUser(userId);

            return user.getUserId().equals(userId) && user.getPassword().equals(password);
        } catch (SQLException e) {
            return false;
        }
    }

    public List<User> findAll() throws SQLException {
        return userDAO.findAllUsers();
    }
}
