package webserver;

import java.io.*;
import java.net.Socket;

import DTO.RequestHeader;

import DTO.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

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
            ResponseHeader responseHeader = new ResponseHeader();

            // do GET or POST
            DispatcherServlet.handleRequest(requestHeader);

            String requestUrl = requestHeader.getRequestUrl();
            byte[] body = IOUtils.readHeaderPathFile(requestUrl);
            DataOutputStream dos = new DataOutputStream(out);
            ResponseViewer.response(dos, body);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }






}
