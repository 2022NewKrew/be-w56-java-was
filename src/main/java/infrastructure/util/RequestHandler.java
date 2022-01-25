package infrastructure.util;

import adaptor.in.web.FrontController;
import infrastructure.model.HttpRequest;
import infrastructure.model.Pair;
import infrastructure.model.RequestHeader;
import infrastructure.model.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String REQUEST_SEPARATE_TOKEN = " ";
    private final FrontController frontController = FrontController.getINSTANCE();
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            handle(dos, bufferedReader);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handle(DataOutputStream dos, BufferedReader bufferedReader) throws IOException {
        HttpRequest httpRequest = getRequest(bufferedReader);
        log.debug("{}", httpRequest);
        frontController.handle(dos, httpRequest);
    }

    private HttpRequest getRequest(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        RequestLine requestLine = HttpRequestUtils.parseRequestLine(line);

        Set<Pair> headers = new HashSet<>();
        while (!"".equals(line = bufferedReader.readLine())) {
            headers.add(HttpRequestUtils.parseHeader(line));
        }

        RequestHeader requestHeader = new RequestHeader(headers);
        return new HttpRequest(requestLine, requestHeader, null);
    }
}
