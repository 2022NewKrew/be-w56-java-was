package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import controller.Controller;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import static util.HttpRequestUtils.*;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Map<String, String> requestMap;
    private Socket connection;
    private Controller controller;

    public RequestHandler(Socket connectionSocket) {
        this.requestMap = new HashMap<>();
        this.connection = connectionSocket;
        this.controller = new Controller();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            log.debug(line);
            parseRequestLine(line).forEach(pair -> requestMap.put(pair.getKey(), pair.getValue()));
            while(!line.equals("")) {
                if (line == null) {
                    break;
                }
                line = br.readLine();
//                log.debug(line);
                Pair headerPair = parseHeader(line);
                if (headerPair == null) {
                    continue;
                }
                requestMap.put(headerPair.getKey(), headerPair.getValue());
            }
            log.debug(requestMap.toString());
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            log.debug("Response!");
            DataOutputStream dos = new DataOutputStream(out);
            Response response = controller.route(requestMap);
            dos.writeBytes(response.getHeader());
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
//            response200Header(dos, body.length, requestMap);
//            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(Map<String, String> requestMap) {

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
