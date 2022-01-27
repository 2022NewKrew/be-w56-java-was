package factory;

import webserver.controller.Controller;
import webserver.controller.common.NotFoundController;
import webserver.controller.common.StaticController;
import webserver.controller.user.UserJoinController;
import webserver.controller.user.UserLoginController;

import java.util.List;

public class ControllerFactory {
    private static final StaticController staticController = new StaticController();
    private static final NotFoundController notFoundController = new NotFoundController();

    private static final UserJoinController userJoinController
            = new UserJoinController(RepositoryFactory.getUserRepository());

    private static final UserLoginController userLoginController
            = new UserLoginController(RepositoryFactory.getUserRepository());

    public static Controller<?> getNotFoundController(){
        return notFoundController;
    }

    public static List<Controller<?>> getNormalControllers(){
        return List.of(
                staticController,
                userJoinController,
                userLoginController
        );
    }
}
