package webserver.handler;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpServletRequestUtils;
import util.IOUtils;
import webserver.controller.StaticController;
import webserver.controller.UserController;
import webserver.request.RequestBody;
import webserver.request.RequestHeaders;
import webserver.request.RequestMsg;
import webserver.request.RequestStartLine;

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

            RequestMsg requestMsg = new RequestMsg();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            RequestStartLine requestStartLine = HttpServletRequestUtils.parseRequestLine(br.readLine());
            RequestHeaders requestHeaders = new RequestHeaders(HttpServletRequestUtils.readHeader(br));
            requestMsg.setRequestStartLine(requestStartLine);
            requestMsg.setRequestHeaders(requestHeaders);
            if (requestHeaders.getHeaderMap().containsKey("Content-Length")) {
                RequestBody requestBody = new RequestBody(IOUtils.
                        readData(br, Integer.parseInt(requestHeaders.getHeaderMap().get("Content-Length"))));
                requestMsg.setRequestBody(requestBody);
            }
            DataOutputStream dos = new DataOutputStream(out);

            //TODO : /user/create 같은 URI 정보들 따로 enum 같은 것으로 만들 것.
            if (requestStartLine.getUrl().contains("/user/create")) {
                UserController.mapping(requestMsg, dos);
            } else {
                StaticController.mapping(requestMsg, dos);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
