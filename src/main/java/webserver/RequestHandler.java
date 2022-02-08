package webserver;

import app.core.MyHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.http.HttpRequest;
import util.http.HttpRequestUtils;
import util.http.HttpResponse;
import util.http.HttpResponseUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        HttpServlet httpServlet = MyHttpServlet.getInstance();

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            // Todo http request 인지 검증하는 부분이 필요?
            HttpRequest httpRequest = HttpRequestUtils.parseRequest(br);
            HttpResponse httpResponse = new HttpResponse();
            DataOutputStream dos = new DataOutputStream(out);
            httpServlet.service(httpRequest, httpResponse);
            log.debug("response header");
            log.debug(httpResponse.headerText());
            HttpResponseUtils.response(httpResponse, dos);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
