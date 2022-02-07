package factory;

import webserver.controller.Controller;
import webserver.controller.common.NotFoundController;
import webserver.controller.common.StaticController;
import webserver.controller.user.UserJoinController;
import webserver.controller.user.UserListController;
import webserver.controller.user.UserLoginController;

import java.util.List;

public class ControllerFactory {
    private static final NotFoundController notFoundController = new NotFoundController();
    private static final List<Controller> normalControllers = List.of(
            new StaticController(),
            new UserListController(RepositoryFactory.getUserRepository()),
            new UserJoinController(RepositoryFactory.getUserRepository()),
            new UserLoginController(RepositoryFactory.getUserRepository())
    );

    public static Controller getNotFoundController(){
        return notFoundController;
    }

    public static List<Controller> getNormalControllers(){
        return normalControllers;
    }
}
