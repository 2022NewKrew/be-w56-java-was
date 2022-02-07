package webserver;

import frontcontroller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyCookie;
import util.MyHttpRequest;
import util.MyHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Void> {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public Void call() throws Exception {

        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            MyHttpRequest request = new MyHttpRequest(in);
            MyHttpResponse response = new MyHttpResponse(out);

            // FILTER
            try {
                doFilter(request, response);
            } catch (SecurityException e) {
                log.info("권한 없는 사용자 접근");
                throw e;
            }

            new FrontController().service(request, response);

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    private void doFilter(MyHttpRequest request, MyHttpResponse response) throws SecurityException {
        String requestURI = request.getRequestURI();

        // login Filter
        if (requestURI.equals("/user/list")) {
            loginAuth(request);
        }
    }

    private void loginAuth(MyHttpRequest request) throws SecurityException {
        MyCookie cookie = request.getCookie();

        Optional<String> login = Optional.ofNullable(cookie.get("logined"));

        if (!login.orElse("").equals("true")) {
            throw new SecurityException("로그인한 회원만 접근 가능합니다.");
        }
    }


}
