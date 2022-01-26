package webserver;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestDecoder;
import webserver.http.request.Method;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseHeaders;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug(
            "New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort()
        );

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequestDecoder.decode(in);
            HttpResponse httpResponse = handle(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);
            ResponseHandler responseHandler = new ResponseHandler(dos, httpResponse);
            responseHandler.sendResponse();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private HttpResponse handle(HttpRequest request) throws IOException {
        HttpResponse response = new HttpResponse(request.getHttpVersion(), new HttpResponseHeaders());
        Method method = request.getMethod();
        MethodHandler methodHandler = method == Method.GET ? new GetMethodHandler() : new PostMethodHandler();

        try {
            methodHandler.handle(request, response);
            return response;
        } catch (FileNotFoundException | AccessDeniedException | PageNotFoundException e) {
            ExceptionHandler.handleException(response, e);
            return response;
        }
    }
}
