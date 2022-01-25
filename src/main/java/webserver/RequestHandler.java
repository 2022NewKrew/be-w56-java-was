package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import com.google.common.collect.Maps;
import controller.UserController;
import controller.ViewController;
import controller.WebController;
import controller.request.Request;
import controller.request.RequestLine;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContentType;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Map<String, WebController> controllerMap = Maps.newHashMap();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllerMap.put("/user/create", new UserController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = br.readLine();
            if (line == null) {
                return;
            }

            log.debug("request line : {}", line);
            Request request = new Request(RequestLine.from(line));

            log.debug("request path : {}", request.getPath());
            WebController webController = controllerMap.getOrDefault(request.getPath(), new ViewController());

            String contentType = ContentType.of(request.getContentType()).getContentType();
            log.debug("request content type : {}", contentType);
            String url = webController.process(request);

            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, contentType, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 NOT FOUND \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
