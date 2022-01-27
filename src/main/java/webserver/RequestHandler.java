package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import controller.HttpController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import util.LogUtils;

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

            Map<String, String> requestMap = IOUtils.readRequest(br);
            LogUtils.requestLog(requestMap, log);

            Map<String,String> headerMap = IOUtils.readHeader(br);
            LogUtils.headerLog(headerMap, log);

            HttpController httpController = new HttpController(requestMap, headerMap, log, br, out);
            httpController.run();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
