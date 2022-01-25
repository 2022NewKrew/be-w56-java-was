package webserver;

import java.io.*;
import java.net.Socket;

import webserver.controller.FrontController;
import webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String requestLine = br.readLine();
            if(requestLine == null){
                throw new IllegalArgumentException("요청을 찾을 수 없습니다..");
            }

            StringBuilder requestHeader = new StringBuilder();
            String line;

            log.info("request : {}", requestLine);
            while(!(line = br.readLine()).equals("")){
                requestHeader.append(line).append("\n");
            }

            HttpRequest request = new HttpRequest(requestLine, requestHeader.toString());
            HttpResponse response = FrontController.getInstance().process(request);

            DataOutputStream dos = new DataOutputStream(out);
            response.send(dos);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }



}
