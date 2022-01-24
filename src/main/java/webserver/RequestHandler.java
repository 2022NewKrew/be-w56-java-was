package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.PathVariable.*;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            RequestInfo requestInfo = getRequestInfo(br);
            log.debug("{} request ... {}", connection.getPort(), requestInfo);

            File file = new File(BASEURL.getPath() + requestInfo.getUrl());
            byte[] fileByteCode = Files.readAllBytes(file.toPath());

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, requestInfo.getMimeType(), fileByteCode.length);
            responseBody(dos, fileByteCode);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, String mimeType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
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

    private RequestInfo getRequestInfo(BufferedReader br) throws IOException {
        String[] tokens = br.readLine().split(" ");
        RequestInfo requestInfo = new RequestInfo();

        requestInfo.setMethod(tokens[0]);
        requestInfo.setUrl(tokens[1]);
        File file = new File(BASEURL.getPath() + tokens[1]);
        requestInfo.setMimeType(getMimeType(file));

        return requestInfo;
    }

    private String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }
}
