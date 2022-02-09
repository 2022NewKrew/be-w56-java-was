package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HandlerMapping;
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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String httpRequestHeader = IOUtils.getHttpRequestHeader(bufferedReader);
            String methodName = HttpRequestUtils.getMethodName(httpRequestHeader);
            String url = HttpRequestUtils.getUrlPath(httpRequestHeader);

            Optional<Map<String, String>> infoMap = HttpRequestUtils.getInfoMap(methodName, url);
            String methodPath = HttpRequestUtils.getMethodPath(url);
            String responseUrl = url;
            if (infoMap.isPresent())
                responseUrl = HandlerMapping.getInstance().runMethod(methodPath, infoMap.get());

            log.info("HTTP Request Header Lines : {}", httpRequestHeader);
            log.info("url Path: {}", url);
            log.info("Response url Path : {}", responseUrl);

            response200(new DataOutputStream(out), responseUrl);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200(DataOutputStream dos, String url) throws IOException {
        String contentType = IOUtils.getContentType(new File("./webapp" + url));
        log.info("contentType : {}", contentType);

        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, contentType, body.length);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
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
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
