package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.RequestPathController;
import util.ResponseStatus;

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
            Request requestHeader;
            String contentType;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line = br.readLine();
                if (line == null) {
                    return;
                }

                requestHeader = HttpRequestUtils.parseRequestHeader(line);
                contentType = HttpRequestUtils.readHeaderAccept(br);

                // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
                try (DataOutputStream dos = new DataOutputStream(out)) {
                    RequestPathController.urlMapping(requestHeader, contentType, dos);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
