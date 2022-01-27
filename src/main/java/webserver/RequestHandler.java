package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import model.RequestHeader;
import model.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import controller.RequestPathMapper;

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
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line = br.readLine();
                if (line == null) {
                    return;
                }

                RequestLine requestLine = HttpRequestUtils.parseRequestLine(line);
                RequestHeader requestHeader = HttpRequestUtils.parseRequestHeader(br);

                Map<String, String> requestBody = new HashMap<>();
                if (requestLine.getMethod().equals("POST")) {
                    requestBody = HttpRequestUtils.parseQueryString(IOUtils.readData(br, requestHeader.getContentLength()));
                }

                // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
                try (DataOutputStream dos = new DataOutputStream(out)) {
                    RequestPathMapper.urlMapping(requestLine, requestHeader, requestBody, dos);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
