package service;

import dao.UserDAO;
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

    public void store(HttpRequest httpRequest) throws SQLException {
        userDAO.storeUser(httpRequest);
    }

    public boolean canLogIn(HttpRequest httpRequest) {
        try {
            Map<String, String> params = httpRequest.getParameters();
            String userId = params.get("userId");
            String password = params.get("password");
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
