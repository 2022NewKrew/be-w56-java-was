package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import response.HttpResponse;

/**
 * HTTP request 마다 생성되는 스레드 기반의 Servlet.
 * <p>요청을 받아 응답을 전달하는 역할까지 수행한다.
 */
public class HttpServlet extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(HttpServlet.class);

    private final Socket connection;

    public HttpServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        LOG.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream()) {

            // define proper input/output stream
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            final DataOutputStream dos = new DataOutputStream(out);

            // receive a request from INPUT stream
            HttpRequest httpRequest = HttpRequest.of(br);

            // make a response from BUSINESS LOGIC
            HttpResponse httpResponse = DispatcherServlet.dispatch(httpRequest);
            LOG.debug("{} {} -> {}", httpRequest.getMethod(), httpRequest.getUrl(), httpResponse.getResponseStatusLine());

            // transport a response to OUTPUT stream
            ViewResolver viewResolver = new ViewResolver(dos);
            viewResolver.render(httpResponse);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
