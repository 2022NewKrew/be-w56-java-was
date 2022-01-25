package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import collections.RequestHeaders;
import collections.RequestStartLine;
import collections.ResponseHeaders;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.HttpRequestUtils.*;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 요청 확인
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            if (line == null) {
                return;
            }

            // 요청 기본 정보 수집
            log.debug("REQ: {}", line);
            String[] tokens = line.split(" ");
            RequestStartLine requestStartLine = new RequestStartLine(new HashMap<>() {{
                put("Method", tokens[0]);
                put("Path", tokens[1]);
                put("Protocol", tokens[2]);
            }});

            // 요청 헤더 수집
            line = br.readLine();
            var tempRequestHeaders = new HashMap<String, String>();
            while (line != null && !line.equals("")) {
                String[] header = line.split(": ");
                Pair pair = parseHeader(line);
                tempRequestHeaders.put(pair.getKey(), pair.getValue());
                log.debug("     {}", line);

                line = br.readLine();
            }
            RequestHeaders requestHeaders = new RequestHeaders(tempRequestHeaders);

            // 응답 준비
            DataOutputStream dos = new DataOutputStream(out);

            // 메서드 별 응답 처리
            switch (requestStartLine.getMethod()) {
                case "GET":
                    responseGet(dos, requestStartLine, requestHeaders);
                    return;
                case "POST":
                    resposePost(dos, requestStartLine, requestHeaders);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void resposePost(DataOutputStream dos, RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        // body 만들기


        // header 만들기


        // 전송

    }

    private void responseGet(DataOutputStream dos, RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        // body 만들기
        byte[] body = Files.readAllBytes(new File("./webapp" + requestStartLine.getPath()).toPath());

        // header 만들기
        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Content-Type", requestHeaders.getHeader("Accept"));
        ResponseHeaders responseHeaders = new ResponseHeaders(temp);

        Map<String, String> parameters = requestStartLine.getParameters();
        if (parameters != null) {
            User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
            log.debug(user.toString());
        }

        // 전송
        response200Header(dos, responseHeaders);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, ResponseHeaders responseHeaders) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", responseHeaders.getHeader("Content-Type")));
            dos.writeBytes(String.format("Content-Length: %s\r\n", responseHeaders.getHeader("Content-Length")));
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
