package webserver;

import config.DataSourceConfig;
import controller.Controller;
import controller.FrontController;
import controller.LoginController;
import controller.MemoController;
import controller.RegisterController;
import controller.UsersController;
import db.MemoRepository;
import db.UserRepository;
import httpmodel.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import jdbc.JdbcTemplate;
import service.MemoService;
import service.UserService;

public class RequestMapping {

    private static final Map<String, Controller> CONTROLLERS = new HashMap<>();
    private static final String FRONT = "front";

    static {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceConfig.getInstance());
        UserService userService = new UserService(new UserRepository(jdbcTemplate));
        MemoService memoService = new MemoService(new MemoRepository(jdbcTemplate));
        CONTROLLERS.put("/users/login", new LoginController(userService));
        CONTROLLERS.put(FRONT, new FrontController(memoService));
        CONTROLLERS.put("/users", new RegisterController(userService));
        CONTROLLERS.put("/users/list", new UsersController(userService));
        CONTROLLERS.put("/qna/form", new MemoController(memoService));
    }

    public Controller getController(HttpRequest httpRequest) {
        if (httpRequest.isUriFile() || httpRequest.isUriMatch("/")) {
            return CONTROLLERS.get(FRONT);
        }
        return CONTROLLERS.entrySet()
            .stream()
            .filter(entry -> httpRequest.isUriMatch(entry.getKey()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 지원되지 않는 URL입니다."))
            .getValue();
    }
}
