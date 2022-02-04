package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();

            log.debug("Request line: " + line);
            String methodType = HttpRequestUtils.parseMethodType(line);
            String uri = HttpRequestUtils.parseLocation(line);
            Map<String, String> headers = new HashMap<>();
            String responseType = "";
            br.readLine();
            while(!"".equals(line)) {
                String[] parsedHeader = line.split(":");
                if (parsedHeader.length == 2) {
                    log.debug("header: " + line);
                    headers.put(parsedHeader[0].trim(), parsedHeader[1].trim());
                }
                line = br.readLine();
            }

            Map<String, String> requestBody = new HashMap<>();
            if (methodType.equals("POST")) {
                String postBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                log.debug("Request Body: " + postBody);
                String[] pairsOfRequestBody = postBody.split("&");
                for (String pair : pairsOfRequestBody) {
                    String[] parsedPair = pair.split("=");
                    requestBody.put(parsedPair[0], parsedPair[1]);
                }
            }

            DataOutputStream dos = new DataOutputStream(out);

            if (new File("./webapp" + uri).exists()) {
                byte[] body = Files.readAllBytes(new File("./webapp" + uri).toPath());
                response200Header(dos, body.length, responseType);
                responseBody(dos, body);
            }
            else {
                Map<String, String> headerInfo = RequestController.uriMatcher(methodType, uri, requestBody);
                response302Header(dos, headerInfo);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String responseType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type:" + responseType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header (DataOutputStream dos, Map<String, String> headerInfo) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + headerInfo.get("redirectUrl") + "\r\n");
            dos.writeBytes("Set-Cookie: " + headerInfo.get("Set-Cookie") + "\r\n");
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
