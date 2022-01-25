package webserver.handler;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpServletRequestUtils;
import webserver.controller.UserController;
import webserver.request.RequestInfo;

import static webserver.handler.ResponseHandler.response200Header;
import static webserver.handler.ResponseHandler.responseBody;

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
            String contextType = "";

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            RequestInfo requestInfo = HttpServletRequestUtils.parseRequestLine(line);
            log.debug("Request line = {}", line);
            Map<String, String> headerMap = HttpServletRequestUtils.readHeader(br);
            DataOutputStream dos = new DataOutputStream(out);

            if(requestInfo.getUrl().contains("/user/create"))
            {
                UserController.mapping(requestInfo, headerMap, dos);
            }
            else {
                if(headerMap.containsKey("Accept")){
                    contextType = headerMap.get("Accept").split(",")[0];
                }

                byte[] body = Files.readAllBytes(new File("./webapp" + requestInfo.getUrl()).toPath());
                response200Header(contextType, dos, body.length);
                responseBody(dos, body);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
