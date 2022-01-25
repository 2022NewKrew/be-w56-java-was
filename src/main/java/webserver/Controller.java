package webserver;

import db.DataBase;
import model.RequestHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class Controller {
    private static final String BASE_PATH = "./webapp";
    private static final Logger log = LoggerFactory.getLogger(Controller.class);


    public static String controller(RequestHeader requestHeader) {
        log.info(requestHeader.getUri());

        if (requestHeader.getUri().equals("/user/create")) {
            signUp(requestHeader.getParams());
            return BASE_PATH+"/index.html";
        }
        return BASE_PATH+requestHeader.getUri();
    }

    private static void signUp(Map<String, String> params) {
        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
    }
}
