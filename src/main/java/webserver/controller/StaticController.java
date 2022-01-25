package webserver.controller;

import webserver.request.RequestInfo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static webserver.handler.ResponseHandler.response200Header;
import static webserver.handler.ResponseHandler.responseBody;

public class StaticController {
    private StaticController() {}

    private static class InnerInstanceClazz {
        private static final StaticController instance = new StaticController();
    }

    public static StaticController getInstance() {
        return StaticController.InnerInstanceClazz.instance;
    }

    public static void mapping(RequestInfo requestInfo, Map<String, String> headerMap, DataOutputStream dos) throws IOException {
        String contextType = "";
        if(headerMap.containsKey("Accept")){
            contextType = headerMap.get("Accept").split(",")[0];
        }

        byte[] body = Files.readAllBytes(new File("./webapp" + requestInfo.getUrl()).toPath());
        response200Header(contextType, dos, body.length);
        responseBody(dos, body);
    }

}
