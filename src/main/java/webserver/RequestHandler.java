package webserver;

import static util.RequestParser.*;
import static util.ResponseGenerator.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.Request;
import http.Response;

public class RequestHandler extends Thread {
    private final String ENCODING = "UTF-8";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        // Http 클래스 분리
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, ENCODING));

            Request request = getRequest(bufferedReader);
            Response response = new Response();
            String source = processRequest(request, response);
            byte[] responseBytes = responseToBytes(source, response);

            dataOutputStream.write(responseBytes, 0, responseBytes.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getContentType(String url) {
        if (url.contains("js")) {
            return "application/js;";
        }
        if (url.contains("css")) {
            return "text/css;";
        }
        return "text/html;";
    }

    private String getSource(String url) {
        String[] splited = url.split("\\?");
        String method = splited[0];
        log.debug(method);
        if (method.matches("/users(.*)")) {
            log.debug("hi: " + method);
            UserController userController = new UserController(method, HttpRequestUtils.parseQueryString(splited[1]));
            return userController.run(method);
        }
        if (method.matches("index.html")) {
            return "index.html";
        }
        return method;
    }
}
