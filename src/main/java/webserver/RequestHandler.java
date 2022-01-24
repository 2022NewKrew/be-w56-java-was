package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import static util.HttpRequestUtils.getUrlExtension;
import static util.HttpRequestUtils.parseRequestLine;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            log.debug(line);
            HttpRequestUtils.Pair urlPair = parseRequestLine(line);
            String url = urlPair.getValue();
            String extension = getUrlExtension(url);
            while(!line.equals("")) {
                if (line == null) {
                    break;
                }
                line = br.readLine();
                log.debug(line);
            }
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            log.debug("Response!");
            response200Header(dos, body.length, extension);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String extension) {
        try {
            String contentType = "";
            List<String> textTypeList = new ArrayList<>(Arrays.asList("css", "html", "js"));
            List<String> imageTypeList = new ArrayList<>(Arrays.asList("ico", "png", "jpeg", "webp"));
            contentType = textTypeList.stream().anyMatch(s -> s.equals(extension)) ? "text/" + extension : contentType;
            contentType = imageTypeList.stream().anyMatch(s -> s.equals(extension)) ? "image/" + extension : contentType;
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
