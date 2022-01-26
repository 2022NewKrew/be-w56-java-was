package webserver;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestDecoder;
import webserver.http.request.Method;
import webserver.http.response.EncodedHttpResponse;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseEncoder;
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
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequest httpRequest = HttpRequestDecoder.decode(in);
            HttpResponse httpResponse = new HttpResponse(
                httpRequest.getHttpVersion(),
                new HttpResponseHeaders(),
                dos
            );

            handle(httpRequest, httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void handle(HttpRequest request, HttpResponse response) throws IOException, IllegalAccessException {
        Method method = request.getMethod();
        MethodHandler methodHandler = method == Method.GET ? new GetMethodHandler() : new PostMethodHandler();

        try {
            String filePath = methodHandler.handle(request, response);
            EncodedHttpResponse encodedHttpResponse = HttpResponseEncoder.encode(response, Path.of(filePath));
            response.sendNormalResponse(encodedHttpResponse);
        } catch (FileNotFoundException | AccessDeniedException e) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            ExceptionHandler.handleException(response, e);
        }
    }
}
