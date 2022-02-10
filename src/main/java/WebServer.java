import adaptor.in.web.HomeController;
import adaptor.in.web.StaticResourceController;
import adaptor.in.web.memo.MemoController;
import adaptor.in.web.user.UserController;
import adaptor.out.persistence.inmemory.session.SessionAttributeInMemoryDao;
import adaptor.out.persistence.inmemory.session.SessionAttributesInMemoryPort;
import adaptor.out.persistence.inmemory.session.SessionInMemoryDao;
import adaptor.out.persistence.inmemory.session.SessionInMemoryPort;
import adaptor.out.persistence.memo.ReadMemoAdaptor;
import adaptor.out.persistence.memo.WriteMemoAdaptor;
import adaptor.out.persistence.mysql.QueryBuilder;
import adaptor.out.persistence.mysql.memo.MemoMysqlDao;
import adaptor.out.persistence.mysql.user.UserMysqlDao;
import adaptor.out.persistence.user.FindUserAdaptor;
import adaptor.out.persistence.user.SignUpUserAdaptor;
import application.in.memo.ReadMemoUseCase;
import application.in.memo.WriteMemoUseCase;
import application.in.session.GetSessionUseCase;
import application.in.session.SetSessionUseCase;
import application.in.user.FindUserUseCase;
import application.in.user.LoginUseCase;
import application.in.user.SignUpUserUseCase;
import application.memo.ReadMemoService;
import application.memo.WriteMemoService;
import application.out.memo.MemoDao;
import application.out.memo.ReadMemoPort;
import application.out.memo.WriteMemoPort;
import application.out.session.SessionAttributesDao;
import application.out.session.SessionAttributesPort;
import application.out.session.SessionDao;
import application.out.session.SessionPort;
import application.out.user.FindUserPort;
import application.out.user.SignUpUserPort;
import application.out.user.UserDao;
import application.session.GetSessionService;
import application.session.SetSessionService;
import application.user.FindUserService;
import application.user.LoginService;
import application.user.SignUpUserService;
import infrastructure.config.ServerConfig;
import infrastructure.util.ControllerRouter;
import infrastructure.util.LoginFilter;
import infrastructure.util.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class WebServer {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    public static void main(String args[]) throws Exception {
        final UserDao userDao = new UserMysqlDao(new QueryBuilder("user", List.of("id", "password", "name", "email")));
        final SessionDao sessionDao = new SessionInMemoryDao();
        final SessionAttributesDao sessionAttributesDao = new SessionAttributeInMemoryDao();
        final MemoDao memoDao = new MemoMysqlDao(new QueryBuilder("memo", List.of("id", "writer", "content", "created_at")));
        final SignUpUserPort signUpUserPort = new SignUpUserAdaptor(userDao);
        final FindUserPort findUserPort = new FindUserAdaptor(userDao);
        final ReadMemoPort readMemoPort = new ReadMemoAdaptor(memoDao);
        final WriteMemoPort writeMemoPort = new WriteMemoAdaptor(memoDao);
        final SessionPort sessionPort = new SessionInMemoryPort(sessionDao);
        final SessionAttributesPort sessionAttributesPort = new SessionAttributesInMemoryPort(sessionAttributesDao);
        final SignUpUserUseCase signUpUserUseCase = new SignUpUserService(signUpUserPort, findUserPort);
        final LoginUseCase loginUseCase = new LoginService(findUserPort);
        final ReadMemoUseCase readMemoUseCase = new ReadMemoService(readMemoPort);
        final WriteMemoUseCase writeMemoUseCase = new WriteMemoService(writeMemoPort, findUserPort);
        final FindUserUseCase findUserUseCase = new FindUserService(findUserPort);
        final GetSessionUseCase getSessionUseCase = new GetSessionService(sessionPort, sessionAttributesPort);
        final SetSessionUseCase setSessionUseCase = new SetSessionService(sessionPort, sessionAttributesPort);
        final UserController userController = new UserController(setSessionUseCase, signUpUserUseCase, loginUseCase, findUserUseCase);
        final MemoController memoController = new MemoController(getSessionUseCase, writeMemoUseCase);
        final HomeController homeController = new HomeController(readMemoUseCase);
        final StaticResourceController staticResourceController = new StaticResourceController();
        final LoginFilter loginFilter = new LoginFilter(getSessionUseCase);
        final ControllerRouter controllerRouter = new ControllerRouter(loginFilter, staticResourceController, homeController, userController, memoController);

        run(args, controllerRouter);
    }

    private static void run(String[] args, ControllerRouter controllerRouter) throws IOException {
        int port;
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
