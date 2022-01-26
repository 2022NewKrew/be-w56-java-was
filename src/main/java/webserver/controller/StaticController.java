package webserver.controller;

import webserver.request.RequestBody;
import webserver.request.RequestMsg;
import webserver.request.RequestStartLine;

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

    public static void mapping(RequestMsg requestMsg, DataOutputStream dos) throws IOException {
        String contextType = "";
        Map<String, String> headerMap = requestMsg.getRequestHeaders().getHeaderMap();
        if(headerMap.containsKey("Accept")){
            contextType = headerMap.get("Accept").split(",")[0];
        }

        byte[] body = Files.readAllBytes(new File("./webapp" + requestMsg.getRequestStartLine().getUrl()).toPath());
        response200Header(contextType, dos, body.length);
        responseBody(dos, body);
    }

}
