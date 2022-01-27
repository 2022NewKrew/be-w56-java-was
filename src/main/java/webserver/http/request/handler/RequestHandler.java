package webserver.http.request.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.response.handler.ExceptionHandler;
import webserver.http.request.exceptions.PageNotFoundException;
import webserver.http.response.handler.ResponseHandler;
import webserver.http.request.HttpRequest;
import webserver.http.request.Method;
import webserver.http.request.exceptions.NullRequestException;
import webserver.http.request.exceptions.RequestBuilderException;
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
            HttpRequest httpRequest = getRequest(in);
            HttpResponse httpResponse = handle(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);
            ResponseHandler responseHandler = new ResponseHandler(dos, httpResponse);
            responseHandler.sendResponse();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private HttpRequest getRequest(InputStream in) throws IOException, RequestBuilderException {
        Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);

        return new HttpRequest.Builder().requestLine(br)
                                        .requestHeaders(br)
                                        .requestBody(br)
                                        .build();
    }

    private HttpResponse handle(HttpRequest request) throws IOException {
        HttpResponse response = new HttpResponse(request.getHttpVersion(), new HttpResponseHeaders());
        Method method = request.getMethod();
        MethodHandler methodHandler = method == Method.GET ? new GetMethodHandler() : new PostMethodHandler();
        try {
            request.checkRequestValidation();
            methodHandler.handle(request, response);
            return response;
        } catch (FileNotFoundException | AccessDeniedException | PageNotFoundException | NullRequestException e) {
            ExceptionHandler.handleException(response, e);
            return response;
        }
    }
}
