package framework.config;

import domain.UserService;
import controller.UserController;
import db.DataBase;

public class Beans {

    public static DataBase dataBase = new DataBase();
    public static UserService userService = new UserService(dataBase);
    public static UserController userController = new UserController(userService);
}
