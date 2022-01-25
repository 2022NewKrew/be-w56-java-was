package webserver.handler;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpServletRequestUtils;
import webserver.controller.StaticController;
import webserver.controller.UserController;
import webserver.request.RequestInfo;

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
            RequestInfo requestInfo = HttpServletRequestUtils.parseRequestLine(line);
            log.debug("Request line = {}", line);
            Map<String, String> headerMap = HttpServletRequestUtils.readHeader(br);
            DataOutputStream dos = new DataOutputStream(out);

            //TODO : /user/create 같은 URI 정보들 따로 enum 같은 것으로 만들 것.
            if (requestInfo.getUrl().contains("/user/create")) {
                UserController.mapping(requestInfo, headerMap, dos);
            } else {
                StaticController.mapping(requestInfo, headerMap, dos);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
