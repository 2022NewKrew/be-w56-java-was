package webserver;

import frontcontroller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyHttpRequest;
import util.MyHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
            doFilter(request, response);

            new FrontController().service(request, response);

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    private void doFilter(MyHttpRequest request, MyHttpResponse response) {

    }


}
