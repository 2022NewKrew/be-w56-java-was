package webserver;

import java.io.*;
import java.net.Socket;
<<<<<<< HEAD

import controller.Controller;
import http.Request;
import http.Response;
=======
import java.nio.file.Files;

import http.Request;
>>>>>>> 8f589f5 (웹 서버 구현 2단계 (#120))
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

public class RequestHandler extends Thread {
    private static final UserService userService = new UserService();
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
<<<<<<< HEAD
    private final RequestMapper requestMapper = new RequestMapper();
=======
>>>>>>> 8f589f5 (웹 서버 구현 2단계 (#120))

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
<<<<<<< HEAD
            Request request = new Request(in);
            Response response = new Response(out);

            Controller controller = requestMapper.mapping(request.getUrl());
            controller.makeResponse(request, response);

=======
            // request message 분석
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Request request = createRequest(br);
            String path = request.getPath();

            if (request.getQueryString() != null) {
                userService.join(request);
            }

            // response message 만들기
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
            String typeOfBodyContent = "text/html";
            if (path.contains(".css")) {
                typeOfBodyContent = "text/css";
            } else if (path.contains(".js")) {
                typeOfBodyContent = "application/javascript";
            }
            response200Header(dos, typeOfBodyContent, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Request createRequest(BufferedReader br) throws IOException {
        // request line 분석
        String requestLine = br.readLine();
        printRequestLine(requestLine);

        String[] tokens = requestLine.split(" ");
        String httpMethod = tokens[0];
        String url = tokens[1];
        String httpVersion = tokens[2];

        // url 분리
        String[] splitUrl = splitUrl(url);
        // query string 체크
        String path, queryString = null;
        if (splitUrl.length != 1) {
            queryString = splitUrl[1];
        }
        path = splitUrl[0];

        // request headers 분석
        printRequestHeaders(br);

        // request 생성
        return new Request(httpMethod, path, queryString, httpVersion);
    }


    private void printRequestLine(String requestLine) {
        log.debug("request line : {}", requestLine);
    }

    private void printRequestHeaders(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!(line).equals("")) {
            line = br.readLine();
            log.debug("request header : {}", line);
        }
    }

    private String[] splitUrl(String url) {
        return url.split("\\?");
    }

    private void response200Header(DataOutputStream dos, String typeOfBodyContent, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + typeOfBodyContent + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
>>>>>>> 8f589f5 (웹 서버 구현 2단계 (#120))
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
