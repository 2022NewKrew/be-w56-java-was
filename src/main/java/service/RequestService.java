package service;

import com.google.common.base.Strings;
import dao.UserDao;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import util.LoginUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestService {

    private UserDao userDao;

    public RequestService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(Map<String, String> params, Logger log) throws IOException {
        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");
        if(Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(password) ||
                Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(email))
            throw new IOException("Parameter Not Exist");
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        log.debug("User : {}", user);
        userDao.addUser(user);
    }

    public String userLogin(Map<String, String> params, Logger log) throws IOException {
        String userId = params.get("userId");
        String password = params.get("password");
        if(Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(password))
            throw new IOException("Parameter Not Exist");
        log.debug("userId : {}, password : {}", userId, password);
        User user = userDao.findUserById(userId);
        String cookie = LoginUtils.checkLogin(log, user, password);
        return cookie;
    }

    public String getUserList() {
        StringBuilder sb = new StringBuilder();
        List<User> userList = new ArrayList<>(userDao.findAll());
        int index = 1;
        for(User user : userList) {
            sb.append(String.format("<tr>\n<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n</tr>\n", index, user.getUserId(), user.getName(), user.getEmail()));
            index++;
        }
        return sb.toString();
    }

}
