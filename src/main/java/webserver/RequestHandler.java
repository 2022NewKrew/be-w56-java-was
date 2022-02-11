package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import webserver.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpResponse;

public class RequestHandler extends Thread {
    private static final String CRLF = "\r\n";
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequest httpRequest = RequestGenerator.generateHttpRequest(br);
            log.debug("path: {}", httpRequest.getPath());
            log.debug("params: {}", httpRequest.getParameters());

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = Router.route(httpRequest);
            writeBytes(httpResponse, dos);
            flush(dos);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    private static void writeBytes(HttpResponse httpResponse, DataOutputStream dos) {
        try {
            dos.writeBytes(String.format("%s %s %s", httpResponse.getVersion(), httpResponse.getStatusCode(), CRLF));
            writeBytesHeaders(httpResponse, dos);
            dos.writeBytes(String.format("Content-Type: %s %s", String.join(";", httpResponse.getContentType()), CRLF));
            dos.writeBytes(String.format("Content-Length: %s %s", httpResponse.getContentLength(), CRLF));
            dos.writeBytes(CRLF);
            dos.write(httpResponse.getBody(), 0, httpResponse.getContentLength());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void writeBytesHeaders(HttpResponse httpResponse, DataOutputStream dos) throws IOException{
        for (String key : httpResponse.getHeaders().keySet()) {
            dos.writeBytes(String.format("%s: %s%s", key, httpResponse.getHeaders().get(key), CRLF));
        }
    }

    private static void flush(DataOutputStream dos) {
        try {
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
