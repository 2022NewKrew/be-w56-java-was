package controller;

import model.RequestHeader;
import model.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResponseStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static controller.RequestPathMapper.*;

public class GeneralController {
    private static final Logger log = LoggerFactory.getLogger(GeneralController.class);

    protected static void rootPath(DataOutputStream dos) throws IOException {
        log.info("Root Path Go to index.html");
        byte[] body = Files.readAllBytes(new File(LOCAL_PREFIX + "/index.html").toPath());
        responseHeader(ResponseStatus.OK, DEFAULT_CONTENT_TYPE, dos, body.length);
        responseBody(dos, body);
    }

    protected static void defaultPath(RequestLine requestLine, RequestHeader requestHeader, DataOutputStream dos) throws IOException {
        File file = new File(LOCAL_PREFIX + requestLine.getUrl());
        byte[] body;

        if (file.exists()) {
            body = Files.readAllBytes(file.toPath());
            responseHeader(ResponseStatus.OK, requestHeader.getContentType(), dos, body.length);
        } else {
            body = ResponseStatus.NOT_FOUND.name().getBytes(StandardCharsets.UTF_8);
            responseHeader(ResponseStatus.NOT_FOUND, requestHeader.getContentType(), dos, body.length);
        }
        responseBody(dos, body);
    }
}
