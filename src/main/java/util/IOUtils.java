package util;

import http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ConnectionHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);

    /**
     * @param br
     *  BufferedReader 는 Request Body 를 시작하는 시점이어야
     * @param contentLength
     *  contentLength 는 Request Header 의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String readStartLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.debug("request : {}", line);
        return line;
    }

    public static List<String> readHeader(BufferedReader br) throws IOException {
        List<String> header = new ArrayList<>();
        String line;
        while(!(line = br.readLine()).equals("")) {
            header.add(line);
            log.debug("request : {}", line);
        }
        return header;
    }

    public static void writeResponse(DataOutputStream dos, ResponseMessage response) {
        writeStatusLine(dos, response.getStatusLine());
        writeHeaders(dos, response.getHeaders());
        write(dos, "\r\n");
        writeBody(dos, response.getBody());
    }

    private static void writeStatusLine(DataOutputStream dos, StatusLine statusLine) {
        HttpStatus status = statusLine.getStatus();
        IOUtils.write(dos, String.format("%s %s %s\r\n", statusLine.getVersion().getValue(), status.getStatusCode(), status.getReasonPhase()));
    }

    private static void writeHeaders(DataOutputStream dos, Headers headers) {
        headers.getHeaders()
                .forEach((key, value) -> IOUtils.write(dos, String.format("%s: %s\r\n", key, value)));

    }

    private static void write(DataOutputStream dos, String message) {
        try {
            dos.writeBytes(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBody(DataOutputStream dos, Body body) {
        try {
            byte[] bytes = body.getBytes();
            dos.write(bytes, 0, bytes.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
