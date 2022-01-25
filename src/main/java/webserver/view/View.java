package webserver.view;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.controller.response.HttpResponse;

public class View {

    private static final Logger log = LoggerFactory.getLogger(View.class);

    public static void render(OutputStream out, HttpResponse httpResponse) throws IOException {

        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + httpResponse.getPath()).toPath());

        responseHeader(dos, body.length, httpResponse);
        responseBody(dos, body);
    }
    private static void responseHeader(DataOutputStream dos, int lengthOfBodyContent, HttpResponse httpResponse){
        try {
            dos.writeBytes(String.format("HTTP/1.1 %s \r\n", httpResponse.getStatus().getCodeAndMessage()));
            // Content-Type 작성 필요
            // dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
