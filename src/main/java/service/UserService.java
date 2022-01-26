package service;

import db.DataBase;
import httpmodel.HttpRequest;
import httpmodel.HttpSession;
import httpmodel.HttpSessions;
import java.util.Objects;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public User login(User user) {
        User expectedUser = DataBase
            .findUserById(user.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 유저입니다."));

        if (expectedUser.checkPassword(user.getPassword())) {
            LOG.info("{} login success", user.getUserId());
            return user;
        }
        throw new IllegalArgumentException("[ERROR] 로그인 실패");
    }

    public boolean isLogin(HttpRequest httpRequest) {
        HttpSession session = httpRequest.getHttpSession();
        return Objects.nonNull(HttpSessions.getSession(session.getId()));
    }

    public void save(User user) {
        DataBase.addUser(user);
        LOG.info("{} user create success", user.getUserId());
    }
}
