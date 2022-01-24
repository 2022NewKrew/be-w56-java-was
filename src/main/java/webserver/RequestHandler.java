package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public static final String WEB_ROOT = "./webapp";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort()); //

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in)); DataOutputStream dos = new DataOutputStream(out)) {

            HttpRequest httpRequest = getHttpRequest(br);
            byte[] body = Files.readAllBytes(new File(WEB_ROOT + httpRequest.getUrl()).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpRequest getHttpRequest(BufferedReader br) throws IOException {
        String requestLine = getRequestLine(br);
        List<String> requestHeader = getRequestStrings(br);
        List<String> requestBody = getRequestStrings(br);

        return new HttpRequest(requestLine, requestHeader, requestBody);
    }

    private String getRequestLine(BufferedReader br) throws IOException {
        String line = "";
        if ((line = br.readLine()) == null) {
            throw new IllegalArgumentException("invalid http request");
        }
        log.info("request line = {}", line);
        return line;
    }

    private List<String> getRequestStrings(BufferedReader br) throws IOException {
        if (!br.ready()) {
            return null;
        }
        List<String> requestHeader = new ArrayList<>();
        String line = br.readLine();
        while (line != null && !line.equals("")) {
            log.info("request = {}", line);
            requestHeader.add(line);
            line = br.readLine();
        }

        return requestHeader;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
