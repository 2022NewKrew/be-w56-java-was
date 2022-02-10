package app.user.adapter.in;

import app.user.application.port.in.ListUserUseCase;
import app.user.domain.User;
import app.utils.SessionUtils;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Model;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.servlet.HttpControllable;

public class ListUserController implements HttpControllable {

    public static final String path = "/user/list";
    private static final Logger logger = LoggerFactory.getLogger(ListUserController.class);
    private final ListUserUseCase listUserUseCase;

    public ListUserController(ListUserUseCase listUserUseCase) {
        this.listUserUseCase = listUserUseCase;
    }

    @Override
    public String call(HttpRequest request, HttpResponse response, Model model) {
        if (!SessionUtils.hasSession(request)) {
            logger.info("세션이 존재하지 않습니다");
            return "/user/login";
        }
        logger.info("세션이 있는 유저입니다");
        Collection<User> users = listUserUseCase.listAll();
        model.addAttributes("user", users);
        return "/user/list";
    }
}
