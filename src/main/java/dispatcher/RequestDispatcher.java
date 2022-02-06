package dispatcher;

import exception.IllegalContentTypeException;
import exception.InternalErrorException;
import filter.FilterChain;
import handler.HandlerMapping;
import handler.HandlerMethod;
import handler.HandlerResult;
import http.request.HttpRequest;
import http.request.RawHttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestDispatcher extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestDispatcher.class);

    private final Socket connection;

    public RequestDispatcher(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connected! Connected IP : {}, Port : {}",
            connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RawHttpRequest rawHttpRequest = RawHttpRequest.from(in);
            log.info(rawHttpRequest.toString());

            HttpResponse httpResponse;

            try{
                FilterChain filterChain = new FilterChain();
                filterChain.doFilter(rawHttpRequest);
            } catch (IllegalContentTypeException e) {
                log.error(e.getMessage());
                httpResponse = HttpResponse.error();
                httpResponse.send(out);
            }

            HttpRequest httpRequest = rawHttpRequest.parse();

            HandlerMethod handlerMethod = HandlerMapping.findHandlerMethodOf(httpRequest);

            if (handlerMethod == null) {
                httpResponse = HttpResponse.ofStatic(httpRequest);
                log.info(httpResponse.toString());
                httpResponse.send(out);
                return;
            }

            try {
                HandlerResult handlerResult = handlerMethod.invoke(httpRequest);
                httpResponse = HttpResponse.fromHandlerResult(httpRequest, handlerResult);
            } catch (InternalErrorException e) {
                log.error(e.getMessage());
                httpResponse = HttpResponse.error();
            }

            log.info(httpResponse.toString());
            httpResponse.send(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
