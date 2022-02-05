package webserver;

import java.beans.beancontext.BeanContext;
import java.io.*;
import java.net.Socket;
import java.util.Map;

import DTO.HeaderDTO;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import util.RequestPathUtils;

import static util.IOUtils.readHeader;
import static util.RequestPathUtils.extractRequestURL;

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
            HeaderDTO headerDTO = readHeader(in);
            String requestUrl = extractRequestURL(headerDTO.getFirstLine());

            if (RequestPathUtils.containsParam(requestUrl)){
                log.info("Request Url Contains Parameters");
                responseParam(requestUrl);
            }
            byte[] body = IOUtils.readHeaderPathFile(requestUrl);
            DataOutputStream dos = new DataOutputStream(out);

            response200Header(dos, body.length);
            responseBody(dos, body);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseParam(String requestUrl){
        Map<String, String> param = RequestPathUtils.extractRequestParam(requestUrl);
        // TO DO!!
        // check url (uesr/create) match with a specific function !!
        User user = new User(param);
        log.info("Create New User : {}", user);
    }


}
