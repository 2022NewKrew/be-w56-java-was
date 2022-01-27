package service;

import db.DataBase;
import model.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static void joinService(Request request) {
        User joinUser = createUserFromRequest(request);
        DataBase.addUser(joinUser);
        logger.info("User(userId = {}) joined.", joinUser);
    }

    public static Boolean loginService(Request request) {
        User loginUser = createUserFromRequest(request);
        logger.info("login request from user({})", loginUser.getUserId());
        User findUser = DataBase.findUserById(loginUser.getUserId());
        return validateLogin(loginUser, findUser);
    }

    private static Boolean validateLogin(User loginUser, User findUser) {
        if (!checkNull(findUser)) {
            logger.info("해당 아이디({})의 가입 정보가 존재하지 않습니다.", loginUser.getUserId());
            return false;
        } else if (!checkPasswordMatch(loginUser, findUser)) {
            logger.info("비밀번호가 일치하지 않습니다.");
            return false;
        }
        logger.info("User({}) login successful", loginUser.getUserId());
        return true;
    }

    private static Boolean checkNull(User findUser) {
        return findUser == null;
    }

    private static Boolean checkPasswordMatch(User loginUser, User findUser) {
        return loginUser.getPassword().equals(findUser.getPassword());
    }

    private static User createUserFromRequest(Request request) {
        String queryString = request.getRequestBody().getBody();
        return new User(HttpRequestUtils.parseQueryString(queryString));
    }
}
