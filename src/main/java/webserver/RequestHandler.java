package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestUtills;
import util.ResponseUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String requestUrl = RequestUtills.readUrlPath(br);

            Map<String, String> headerMap = RequestUtills.readHeader(br);
            String respContextType = "";
            if (headerMap.containsKey("Accept")) {
                respContextType = headerMap.get("Accept").split(",")[0];
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath());
            ResponseUtils.response200Header(respContextType, dos, body.length);
            ResponseUtils.responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
