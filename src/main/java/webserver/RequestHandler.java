package webserver;

import java.io.*;
import java.net.Socket;

import domain.User;
import network.HttpRequest;
import network.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.MyUtill;

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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String headerLine = bufferedReader.readLine();
            String query = headerLine.split(" ")[1];

            HttpRequest httpRequest = new HttpRequest(bufferedReader);

            ResponseHandler responseHandler = new ResponseHandler(httpRequest);
            responseHandler.run();

            if(httpRequest.getQueryString() != null){
                User user = new User(httpRequest.getQueryString());
                return;
            }
            HttpResponse.handleHtmlResponse(httpRequest.getPath(), out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
