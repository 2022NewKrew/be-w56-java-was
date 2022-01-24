package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private final static Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final static String STATIC_FILE_DIRECTORY = "./webapp";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader httpRequestHeaderReader = new BufferedReader(new InputStreamReader(in));

            String requestUrl = this.getRequestUrl(httpRequestHeaderReader);
            log.info("request url : {}", requestUrl);

            byte[] body = Files.readAllBytes(new File(STATIC_FILE_DIRECTORY + requestUrl).toPath());

            DataOutputStream dos = new DataOutputStream(out);
            this.response200Header(dos, body.length);
            this.responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getRequestUrl(BufferedReader httpRequestHeaderReader) throws IOException {
        String firstLine = httpRequestHeaderReader.readLine();
        String[] tokens = firstLine.split(" ");

        return tokens[1];
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
