package webserver.view;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.common.FileLocation;
import webserver.common.Status;
import webserver.controller.request.HttpRequest;
import webserver.controller.response.HttpResponse;

public class RenderType {

    private static Logger log = LoggerFactory.getLogger(RenderType.class);

    public static BiConsumer<DataOutputStream, HttpResponse> findRenderer(HttpResponse httpResponse){
        if (httpResponse.getStatus() == Status.OK){
            return RenderType::Response200;
        }
        return RenderType::ResponseX00;
    }

    private static void Response200(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            responseCommonHeader(dos, httpResponse);
            byte[] body = Files.readAllBytes(
                    new File(FileLocation.FILE_DIRECTORY.path + httpResponse.getPath()).toPath());
            response200Header(dos, body.length, httpResponse);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void ResponseX00(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            responseCommonHeader(dos, httpResponse);
            responseNot200Header(dos, httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static void responseCommonHeader(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(String.format("HTTP/1.1 %s \r\n", httpResponse.getStatus().getCodeAndMessage()));
            dos.writeBytes(httpResponse.getHeaderString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent, HttpResponse httpResponse) {
        try {
            // Content-Type 작성 필요
            // dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void responseNot200Header(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes("Location: " + httpResponse.getPath() + "\r\n");
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
