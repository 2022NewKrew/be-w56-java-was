package Service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpService {

    private static final Logger log = LoggerFactory.getLogger(HttpService.class);

    public void signup(Map<String, String> data) {
        User user = new User(data.get("userId"), data.get("password"), data.get("name"), data.get("email"));
        DataBase.addUser(user);

        // 데이터베이스에 잘 입력되었는지 확인
        for (User u : DataBase.findAll()) {
            log.debug(u.toString());
        }
    }

    public boolean validLogin(String userId, String password) {
        User user = DataBase.findUserById(userId);
        if (user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}
