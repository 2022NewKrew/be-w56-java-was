package infrastructure.util;

import infrastructure.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

import static infrastructure.util.ResponseHandler.response;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final ControllerRouter controllerRouter;
    private final Socket connection;

    public RequestHandler(ControllerRouter controllerRouter, Socket connectionSocket) {
        this.controllerRouter = controllerRouter;
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            response(dos, handle(bufferedReader));
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    private HttpResponse handle(BufferedReader bufferedReader) throws IOException, IllegalArgumentException {
        HttpRequest httpRequest = getRequest(bufferedReader);

        return controllerRouter.handle(httpRequest);
    }

    private HttpRequest getRequest(BufferedReader bufferedReader) throws IOException, IllegalArgumentException {
        String line = bufferedReader.readLine();
        RequestLine requestLine = null;
        try {
            requestLine = HttpRequestUtils.parseRequestLine(line);
        } catch (NullPointerException e) {
            log.error("파싱할 수 없음: {}", line);
        }

        HttpHeader.Builder headerBuilder = HttpHeader.builder();
        while (!"".equals(line = bufferedReader.readLine())) {
            headerBuilder.setHeader(HttpRequestUtils.parseHeader(line));
        }
        HttpHeader httpHeader = headerBuilder.build();

        String contentLength = httpHeader.getHeader("Content-Length");
        if (contentLength != null) {
            String value = IOUtils.readData(bufferedReader, Integer.parseInt(contentLength));
            HttpBody httpBody = new HttpStringBody(value);

            return new HttpRequest(requestLine, httpHeader, httpBody);
        }

        return new HttpRequest(requestLine, httpHeader);
    }
}
