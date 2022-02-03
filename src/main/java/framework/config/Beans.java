package framework.config;

import domain.UserService;
import controller.UserController;
import db.DataBase;

public class Beans {

    // 의존성 역방향으로 생성 및 주입
    public static DataBase dataBase = new DataBase();
    public static UserService userService = new UserService(dataBase);
    public static UserController userController = new UserController(userService);
}
