package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import db.DataBase;
import domain.*;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
             DataOutputStream dos = new DataOutputStream(out)) {

            RequestLine requestLine = new RequestLine(bufferedReader.readLine());
            log.info("HTTP Request Line : {}", requestLine);

            HttpRequest httpRequest = new HttpRequest(requestLine, HttpRequestUtils.parseHeaders(bufferedReader));
            String requestUrl = httpRequest.getRequestPath();

            byte[] body;

            if (requestLine.isGetMethod()) {
                if (requestUrl.startsWith("/user/create")) {
                    String queryString = HttpRequestUtils.getQueryStringByUrl(requestUrl);
                    Map<String, String> queryMap = HttpRequestUtils.parseQueryString(queryString);
                    User user = new User(queryMap.get("userId"), queryMap.get("password"), queryMap.get("name"), queryMap.get("email"));
                    DataBase.addUser(user);
                    log.info("findUser : {}", DataBase.findUserById(queryMap.get("userId")));
                    body = DataBase.findUserById(queryMap.get("userId")).toString().getBytes(StandardCharsets.UTF_8);
                } else {
                    body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath());
                }
                response200(dos, body, HttpRequestUtils.parseContentType(requestUrl));
            } else if (requestLine.isPostMethod()) {
                String httpBodyString = IOUtils.readData(bufferedReader, httpRequest.getContentLength());
                log.info(httpBodyString);

                httpRequest.addHttpBody(HttpRequestUtils.parseQueryString(httpBodyString));
                if (requestUrl.startsWith("/user/create")) {
                    HttpBody httpBody = httpRequest.getHttpBody();
                    User user = new User(httpBody.get("userId"), httpBody.get("password"), httpBody.get("name"), httpBody.get("email"));
                    DataBase.addUser(user);
                    log.info("findUser : {}", DataBase.findUserById(httpBody.get("userId")));
                    response302(dos, "/index.html");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void response200(DataOutputStream dos, byte[] body, ContentType contentType) {
        ResponseHandler responseHandler = new ResponseHandler(dos);
        responseHandler.response200Header(body.length, contentType.getContentType());
        responseHandler.responseBody(body);
    }

    private void response302(DataOutputStream dos, String location) {
        ResponseHandler responseHandler = new ResponseHandler(dos);
        responseHandler.response302Header(location);
    }
}
