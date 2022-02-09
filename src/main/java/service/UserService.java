package service;

import db.DataBase;
import model.User;

import java.util.Iterator;


public enum UserService {
    INSTANCE;

    public void addUser(User user) {
        DataBase.addUser(user);
    }

    public boolean userLogin(String userId, String password) {
        User user = DataBase.findUserById(userId);
        return user != null && user.getPassword().equals(password);
    }

    public String getUserList() {
        StringBuilder sb = new StringBuilder();
        Iterator<User> userIterator = DataBase.findAll().iterator();
        for (int idx = 1; userIterator.hasNext(); idx++) {
            User user = userIterator.next();
            sb.append("<tr><th scope=\"row\">" + idx + "</th>" +
                    "<td>" + user.getUserId() + "</td>" +
                    "<td>" + user.getName() + "</td>" +
                    "<td>" + user.getEmail() + "</td>" +
                    "<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
        }
        return sb.toString();
    }
}
