package webserver;

import java.io.*;
import java.net.Socket;

import DTO.ModelAndView;
import DTO.RequestHeader;

import DTO.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;

import static util.IOUtils.readHeader;

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
            RequestHeader requestHeader = readHeader(in);
            ResponseHeader responseHeader = new ResponseHeader(requestHeader);
            DataOutputStream dos = new DataOutputStream(out);

            ModelAndView mav = DispatcherServlet.handleRequest(requestHeader, responseHeader);

            mav.render(requestHeader, responseHeader, dos);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }






}
