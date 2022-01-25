package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.MyUtill;

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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String headerLine = bufferedReader.readLine();
            String query = headerLine.split(" ")[1];

            if(MyUtill.isGetQuery(query)){
                handleQueryRequest(query);
                return;
            }
            handleHtmlRequest(headerLine, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handleQueryRequest(String headerLine){
        String query = headerLine.split("\\?")[1];
        User user = new User(HttpRequestUtils.parseQueryString(query));
    }

    private void handleHtmlRequest(String headerLine, OutputStream out) throws IOException {
        byte[] body = getHtmlBytes(headerLine);

        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private byte[] getHtmlBytes(String headerLine) throws IOException {

        String url = extractPath(headerLine);
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    private String extractPath(String headerLine) {
        return headerLine.split(" ")[1];
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
