package webserver;

import frontcontroller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyCookie;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MySession;
import util.exception.UserCookieEmptyException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Void> {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private MyHttpRequest request;
    private MyHttpResponse response;
    private MySession session;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public Void call() throws Exception {

        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            init(in, out);

            // FILTER
            // login 체크
            try {
                doFilter();
            } catch (SecurityException e) {
                log.info("권한 없는 사용자 접근");
                throw e;
            }

            new FrontController().service(request, response, session);

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    private void init(InputStream in, OutputStream out) throws IOException {
        request = new MyHttpRequest(in);
        response = new MyHttpResponse(out);
        sessionInit();
    }

    private void sessionInit() {
        try {
            session = MySession.getInstance(request.getCookie().get("sessionUUID"));
        } catch (UserCookieEmptyException exception) {
            session = MySession.newInstance();
            response.getCookie().set("sessionUUID", session.getSessionUUID());
            log.debug("sessionUUID : {}", session.getSessionUUID());
        }
    }

    private void doFilter() throws SecurityException {
        // 로그인한 유저만 접근 가능함
        if (request.getRequestURI().equals("/user/list")) {
            loginAuth();
        }
    }

    private void loginAuth() throws SecurityException {
        Optional<Object> logined = Optional.ofNullable(session.getAttribute("logined"));

        if(logined.isEmpty()) {
            throw new SecurityException("로그인한 회원만 접근 가능합니다.");
        }
    }


}
