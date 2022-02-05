package webserver;

import java.beans.beancontext.BeanContext;
import java.io.*;
import java.net.Socket;
import java.util.Map;

import DTO.HeaderDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import util.RequestPathUtils;

import static util.IOUtils.readHeader;
import static webserver.MemberController.requestMapping;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final boolean GET = true;
    private static final boolean POST = false;

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

            // do GET or POST
            reqeustMethod(headerDTO.checkMethod(), headerDTO);

            String requestUrl = headerDTO.getRequestUrl();
            byte[] body = IOUtils.readHeaderPathFile(requestUrl);
            DataOutputStream dos = new DataOutputStream(out);

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void reqeustMethod(boolean getOrPost, HeaderDTO headerDTO){
        if(getOrPost == GET){
            requestGet(headerDTO);
            return;
        }

        requestPost(headerDTO);

    }
    private void requestGet(HeaderDTO headerDTO){
        String requestUrl = headerDTO.getRequestUrl();
        if (RequestPathUtils.containsParam(requestUrl)){
            log.info("Request Url Contains Parameters");
            getParam(requestUrl);
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

    private void getParam(String requestUrl){

        Map<String, String> requestParam = RequestPathUtils.extractRequestParam(requestUrl);
        String requestUrlOnly = RequestPathUtils.extractRequestUrlOnly(requestUrl);

        requestMapping(requestUrlOnly, requestParam);
    }

    private void requestPost(HeaderDTO headerDTO){
        Map<String, String> requestParam = headerDTO.getBody();
        String requestUrlOnly = headerDTO.getRequestUrl();

        requestMapping(requestUrlOnly, requestParam);
    }




}
