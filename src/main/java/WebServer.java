import adaptor.in.web.HomeController;
import adaptor.in.web.StaticResourceController;
import adaptor.in.web.user.UserController;
import adaptor.out.persistence.user.FindUserInMemoryAdaptor;
import adaptor.out.persistence.user.SignUpUserInMemoryAdaptor;
import adaptor.out.persistence.user.UserInMemoryDao;
import application.in.user.LoginUseCase;
import application.in.user.SignUpUserUseCase;
import application.out.user.FindUserPort;
import application.out.user.SignUpUserPort;
import application.out.user.UserDao;
import application.user.LoginService;
import application.user.SignUpUserService;
import infrastructure.config.ServerConfig;
import infrastructure.util.ControllerRouter;
import infrastructure.util.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    public static void main(String args[]) throws Exception {
        int port;
        final UserDao userDao = new UserInMemoryDao();
        final SignUpUserPort signUpUserPort = new SignUpUserInMemoryAdaptor(userDao);
        final FindUserPort findUserPort = new FindUserInMemoryAdaptor(userDao);
        final SignUpUserUseCase signUpUserUseCase = new SignUpUserService(signUpUserPort);
        final LoginUseCase loginUseCase = new LoginService(findUserPort);
        final UserController userController = new UserController(signUpUserUseCase, loginUseCase);
        final HomeController homeController = new HomeController();
        final StaticResourceController staticResourceController = new StaticResourceController();
        final ControllerRouter controllerRouter = new ControllerRouter(staticResourceController, homeController, userController);

        if (args == null || args.length == 0) {
            port = ServerConfig.DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(controllerRouter, connection);
                requestHandler.start();
            }
        }
    }
}
