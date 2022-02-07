package webserver;

import defaultpjt.db.DataBase;
import defaultpjt.model.User;
import webserver.context.Request;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

public class DefaultDispatcher implements Dispatcher {

    private final DataBase dataBase = new DataBase();

    public String createUser(Map<String,String> paramMap) {
        DataBase.addUser(new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("name"), paramMap.get("email")));
        return "/index.html";
    }

    public byte[] handlerOfGet(String url, Map<String,String> paramMap, Map<String,String> dataMap) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    public String handlerOfPost(String url, Map<String,String> paramMap, Map<String,String> dataMap) {
        if ("/user/create".equals(url)) {
            return createUser(paramMap);
        }
        return "/index.html";
    }

    public void dispatch(Request request, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        switch (request.getMethod()) {
            case "GET":
                byte[] body = handlerOfGet(request.getUrl(), request.getParamMap(), request.getHeaderDataMap());
                ResponseHandler.response200Header(dos, body.length, ResponseHandler.getMediaType(request.getUrl()));
                ResponseHandler.responseBody(dos, body);
                break;
            case "POST":
                String redirectUrl = handlerOfPost(request.getUrl(), request.getBodyDataMap(), request.getHeaderDataMap());
                ResponseHandler.redirectHeader(dos, redirectUrl);
                break;
            case "PUT": break;
            case "DELETE": break;
            default: throw new IllegalArgumentException("Invalid Command!");
        }
    }

}
