package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            String requestLine = br.readLine();
            RequestHeader requestHeader = new RequestHeader();

            if(requestLine != null) {
                log.info("request line : {}", requestLine);
                setHeader(requestHeader, requestLine, br);
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + requestHeader.getPath()).toPath());
            response200Header(dos, body.length, requestHeader);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void setHeader(RequestHeader requestHeader, String requestLine, BufferedReader br) throws IOException {
        String[] tokens = requestLine.split(" ");
        requestHeader.setMethod(tokens[0]);
        requestHeader.setPath(tokens[1]);

        String nextLine;
        Map<String, String> headers = requestHeader.getHeaders();
        while (!(nextLine = br.readLine()).equals("")){
            log.info("header : {}", nextLine);
            String[] header = nextLine.split(": ");
            headers.put(header[0], header[1]);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, RequestHeader requestHeader) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + requestHeader.getContentType() + ";charset=utf-8\r\n");
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
