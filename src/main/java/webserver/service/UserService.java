package webserver.service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.RequestHandler;
import webserver.mapper.UserMapper;

import java.util.Map;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private UserService() {}

    private static class InnerInstanceClazz {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance() {
        return UserService.InnerInstanceClazz.instance;
    }

    public void save(Map<String, String> userMap) {
        User user = UserMapper.INSTANCE.MapToEntity(userMap);
        DataBase.addUser(user);
        log.info("User Create Success {}",user);
    }
}
