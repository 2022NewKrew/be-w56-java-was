package dispatcher;

import exception.IllegalContentTypeException;
import exception.InternalErrorException;
import filter.FilterChain;
import handler.HandlerMapping;
import handler.HandlerMethod;
import handler.result.HandlerResult;
import http.request.HttpRequest;
import http.request.RawHttpRequest;
import http.response.HttpResponse;
import http.response.Status;
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
                FilterChain filterChain = FilterChain.getInstance();
                filterChain.doFilter(rawHttpRequest);
            } catch (IllegalContentTypeException e) {
                log.error(e.getMessage());
                httpResponse = HttpResponse.error(Status.BAD_REQUEST);
                httpResponse.send(out);
            }

            HttpRequest httpRequest = rawHttpRequest.parse();

            HandlerMethod handlerMethod = HandlerMapping.findHandlerMethodOf(httpRequest);

            if (handlerMethod == null) {
                httpResponse = HttpResponse.fromStaticRequest(httpRequest);
                log.info(httpResponse.toString());
                httpResponse.send(out);
                return;
            }

            try {
                HandlerResult handlerResult = handlerMethod.invoke(httpRequest);
                httpResponse = HttpResponse.fromHandlerResult(handlerResult);
            } catch (InternalErrorException e) {
                log.error(e.getMessage());
                httpResponse = HttpResponse.error(Status.INTERNAL_SERVER_ERROR);
            }

            log.info(httpResponse.toString());
            httpResponse.send(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
